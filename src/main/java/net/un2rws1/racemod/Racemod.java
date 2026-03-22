package net.un2rws1.racemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import net.un2rws1.racemod.block.ModBlocks;
import net.un2rws1.racemod.classsystem.ClassAttachmentTypes;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.ClassState;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.command.ClassCommand;
import net.un2rws1.racemod.entity.ModEntities;
import net.un2rws1.racemod.event.PlayerJoinHandler;
import net.un2rws1.racemod.item.ModItemGroups;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.networking.ModNetworking;
import net.un2rws1.racemod.networking.SyncClassPayload;
import net.un2rws1.racemod.sound.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.un2rws1.racemod.event.PlayerRespawnHandler;

import static net.un2rws1.racemod.classsystem.ClassManager.*;


public class Racemod implements ModInitializer {
	public static final String MOD_ID = "race-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
		ModBlocks.registerModBlocks();
		ModSounds.registerSounds();
		ModEntities.register();

		//Classes (races)
		ClassAttachmentTypes.init();
		ModNetworking.register();
		PlayerJoinHandler.register();
		PlayerRespawnHandler.register();

		//classes (races) buffs debuffs and whatnot
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				ClassManager.tickPlayer(player);
			}
		});
		ServerTickEvents.START_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				tickPlayer(player);
			}
		});
		//==============coin slot eye==============
		PayloadTypeRegistry.playS2C().register(
				SyncClassPayload.ID,
				SyncClassPayload.CODEC
		);
		//classes food, you cant eat certain food
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getStackInHand(hand);
			PlayerClass playerClass = getPlayerClass(player);
			Item item = stack.getItem();
			if (stack.get(DataComponentTypes.FOOD) == null) {
				return TypedActionResult.pass(stack);
			}
			if (playerClass == null) {
				return TypedActionResult.pass(stack);
			}

			if (playerClass == PlayerClass.JEW) {
				if (item == Items.PORKCHOP ||
						item == Items.COOKED_PORKCHOP) {
					if (!world.isClient) {
						player.sendMessage(Text.literal("You're a Jew"), true);
					}
					return TypedActionResult.fail(stack);
				}
			}
			if (playerClass == PlayerClass.INDIAN) {
				if (item == Items.BEEF ||
						item == Items.COOKED_BEEF) {
					if (!world.isClient) {
						player.sendMessage(Text.literal("You're Indian, put that away"), true);
					}
					return TypedActionResult.fail(stack);
				}
			}
			if (playerClass == PlayerClass.BLACK) {
				if (item != Items.CHICKEN &&
						item != Items.COOKED_CHICKEN &&
						item != Items.MELON_SLICE) {
					if (!world.isClient) {
						player.sendMessage(Text.literal("Yea you're black, stick to chicken and watermelon"), true);
					}
					return TypedActionResult.fail(stack);
				}
			}

			return TypedActionResult.pass(stack);
		});
		//=============================shabbat====================================
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			tickPlayers(server);
		});
		//==================================breaking blocks hungry jews==================
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (world.isClient()) return;
			if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

			PlayerClass playerClass = ClassManager.getPlayerClass(serverPlayer);
			if (playerClass == PlayerClass.JEW) {
				serverPlayer.getHungerManager().addExhaustion(0.1f);
			}
		});
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				ClassCommand.register(dispatcher));
	}


	// ===============================shabbat================================
	public static void tickPlayers(MinecraftServer server) {
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
			ClassState state = getState(player);
			PlayerClass playerClass = getPlayerClass(player);

			if (playerClass != PlayerClass.JEW) {
				continue;
			}

			long day = (player.getServerWorld().getTimeOfDay() / 24000L) + 1;
			boolean shouldBeAdventure = (day % 7 == 0);

			if (shouldBeAdventure) {
				if (player.interactionManager.getGameMode() != GameMode.ADVENTURE) {
					player.changeGameMode(GameMode.ADVENTURE);
					player.sendMessage(Text.literal("It is the Shabbot, you can't work."), false);
				}
			} else {
				if (player.interactionManager.getGameMode() != GameMode.SURVIVAL) {
					player.changeGameMode(GameMode.SURVIVAL);
					player.sendMessage(Text.literal("The Shabbot has passed, you can work again"), false);
				}
			}

		}
	}

	// ==============================interest rate======================
	//==================tick player serverentity player
	private static void tickPlayer(ServerPlayerEntity player) {
		PlayerClass playerClass = ClassManager.getPlayerClass(player);
		if (playerClass == PlayerClass.JEW) {
			handleJewInterestReward(player);
		}
	}
	private static int countItem(ServerPlayerEntity player, Item item) {
		int count = 0;
		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack stack = player.getInventory().getStack(i);
			if (stack.isOf(item)) {
				count += stack.getCount();
			}
		}
		return count;
	}
	private static void handleOreReward(ServerPlayerEntity player, Item input, Item output) {
		int count = countItem(player, input);
		int fullStacks = count / 64;

		if (fullStacks > 0) {
			ItemStack reward = new ItemStack(output, fullStacks);
			giveOrDrop(player, reward);
		}
	}
	private static void giveOrDrop(ServerPlayerEntity player, ItemStack stack) {
		boolean inserted = player.getInventory().insertStack(stack);

		if (!inserted || !stack.isEmpty()) {
			player.dropItem(stack, false);
		}
	}
	private static void handleJewInterestReward(ServerPlayerEntity player) {
		ServerWorld world = player.getServerWorld();
		long currentDay = world.getTimeOfDay() / 24000L;
		ClassState state = ClassManager.getState(player);
		if (state.getLastJewsInterestDay() == -1L) {
			state.setLastJewsIntersetDay(currentDay);
			return;
		}
		if (state.getLastJewsInterestDay() == currentDay) {
			return;
		}
		int diamondCount = countItem(player, Items.DIAMOND);
		int fullStacks = diamondCount / 64;
		handleOreReward(player, Items.DIAMOND, Items.DIAMOND);
		handleOreReward(player, Items.GOLD_INGOT, Items.GOLD_INGOT);
		handleOreReward(player, Items.EMERALD, Items.EMERALD);

			player.sendMessage(Text.literal("Your interest came in to " + fullStacks + " valuable ores. Congrats on being a JEW!"), true);


		state.setLastJewsIntersetDay(currentDay);
	}


}




