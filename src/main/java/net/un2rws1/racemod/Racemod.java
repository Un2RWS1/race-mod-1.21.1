package net.un2rws1.racemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.un2rws1.racemod.block.ModBlocks;
import net.un2rws1.racemod.classsystem.ClassAttachmentTypes;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.ClassState;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.command.ClassCommand;
import net.un2rws1.racemod.effect.ModEffects;
import net.un2rws1.racemod.entity.ModEntities;
import net.un2rws1.racemod.event.PlayerJoinHandler;
import net.un2rws1.racemod.item.ModItemGroups;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.loot.ModLootTableModifiers;
import net.un2rws1.racemod.networking.ModNetworking;
import net.un2rws1.racemod.networking.StealAttemptPayload;
import net.un2rws1.racemod.networking.SyncClassPayload;
import net.un2rws1.racemod.sound.ModSounds;
import net.un2rws1.racemod.villager.ModPointOfInterestTypes;
import net.un2rws1.racemod.villager.ModVillagerProfessions;
import net.un2rws1.racemod.villager.ModVillagerTrades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.un2rws1.racemod.event.PlayerRespawnHandler;
import org.slf4j.MDC;

import java.util.*;

import static net.un2rws1.racemod.classsystem.ClassManager.*;


public class Racemod implements ModInitializer {
	public static final String MOD_ID = "race-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ModBlocks.registerModBlocks();
		ModItems.registerModItems();

		ModItemGroups.registerItemGroups();
		ModSounds.registerSounds();
		ModEntities.register();
		ModEffects.registerEffects();
		ModLootTableModifiers.modifyLootTables();

		ModPointOfInterestTypes.registerModPoiTypes();
		ModVillagerProfessions.registerVillagerProfessions();
		ModVillagerTrades.registerVillagerTrades();
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
		//====================== stealing ==============
		PayloadTypeRegistry.playC2S().register(
				StealAttemptPayload.ID,
				StealAttemptPayload.CODEC
		);
		ServerPlayNetworking.registerGlobalReceiver(
				StealAttemptPayload.ID,
				(payload, context) -> {
					ServerPlayerEntity player = context.player();
					context.server().execute(() -> {
						handleStealAttempt(player, payload.targetEntityId());
					});
				}
		);

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

			if (playerClass != PlayerClass.INDIAN){
				if(item == ModItems.COOKED_POOP){
					if(!world.isClient) {
						player.sendMessage(Text.literal("Only Indians can eat this"), true);
					}
					return TypedActionResult.fail(stack);
				}

			}
			if (playerClass != PlayerClass.BLACK){
				if(item == ModItems.KOOL_AID ||
					item == ModItems.KFC_BUCKET){
					if(!world.isClient) {
						player.sendMessage(Text.literal("Only Blacks can consume this"), true);
					}
					return TypedActionResult.fail(stack);
				}
			}
			if (playerClass != PlayerClass.CHINESE){
				if(item == ModItems.WOLF_MEAT ||
					item == ModItems.COOKED_WOLF_MEAT ||
					item == ModItems.CAT_MEAT ||
					item == ModItems.COOKED_CAT_MEAT){
					if(!world.isClient) {
						player.sendMessage(Text.literal("china china china, you're not Chinese"), true);
					}
					return TypedActionResult.fail(stack);
				}

			}
			if (playerClass == PlayerClass.JEW) {
				if (item == Items.PORKCHOP ||
						item == Items.COOKED_PORKCHOP){
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
						item != Items.MELON_SLICE &&
						item != ModItems.KOOL_AID &&
						item != ModItems.KFC_BUCKET) {
					if (!world.isClient) {
						player.sendMessage(Text.literal("Yea you're black, stick to chicken, watermelon and Kool aid"), true);
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
		//==================================brekaing blocks mechanics==================
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (world.isClient()) return;
			if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

			PlayerClass playerClass = ClassManager.getPlayerClass(serverPlayer);
			if (playerClass == PlayerClass.JEW) {
				serverPlayer.getHungerManager().addExhaustion(0.1f);
			}
			//==================================blacks break bedrock (its cooked, maybe patch 2.0)===============================
	/*		if (state.isOf(Blocks.BEDROCK) && getPlayerClass(player) == PlayerClass.BLACK) {
				player.getHungerManager().setFoodLevel(0);
				player.getHungerManager().setSaturationLevel(0.0f);
				player.addStatusEffect(new StatusEffectInstance(StatusEffects.MINING_FATIGUE, 2400, 2, false, true));
				player.sendMessage(Text.literal("Breaking bedrock drained all your energy, go eat some watermelon and chicken."), true);
			}

	*/
		});

		// stealing
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			ClassState oldState = getState(oldPlayer);
			ClassState newState = getState(newPlayer);
			newState.setSelectedClassId(oldState.getSelectedClassId());
			newState.setLastStealTime(oldState.getLastStealTime());
			newState.clearStealAttempt();
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
		ClassState state = getState(player);
		if (playerClass == PlayerClass.JEW) {
			handleJewInterestReward(player);
		}
		if (playerClass == PlayerClass.BLACK) {
			tickThiefSteal(player, state);
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
		int fullStacks = diamondCount / 32;
		int emeraldCount = countItem(player, Items.EMERALD);
		fullStacks += emeraldCount / 64;
		int goldCount = countItem(player, Items.GOLD_INGOT);
		fullStacks += goldCount / 64;
		handleOreReward(player, Items.DIAMOND, Items.DIAMOND);
		handleOreReward(player, Items.GOLD_INGOT, Items.GOLD_INGOT);
		handleOreReward(player, Items.EMERALD, Items.EMERALD);

			player.sendMessage(Text.literal("Your interest came in to " + fullStacks + " valuable ores. Congrats on being a JEW!"), true);


		state.setLastJewsIntersetDay(currentDay);
	}
	// ===========================stealing=============================
	public static final long STEAL_COOLDOWN_TICKS = 24000L;
	public static final long STEAL_CHANNEL_TICKS = 40L;
	public static final double STEAL_RANGE = 3.0;
	public static void handleStealAttempt(ServerPlayerEntity thief, int targetEntityId) {
		if (thief.getServerWorld().isClient()) return;
		ClassState thiefState = getState(thief);
		PlayerClass thiefClass = PlayerClass.fromId(thiefState.getSelectedClassId());
		if (thiefClass != PlayerClass.BLACK) {
			return;
		}
		long now = thief.getServerWorld().getTime();
		long lastSteal = thiefState.getLastStealTime();
		long remaining = STEAL_COOLDOWN_TICKS - (now - lastSteal);
		if (remaining > 0) {
			thief.sendMessage(Text.literal("Calm down, we know you can't control your impulse but just hold it for " + formatStealCooldown(remaining) + "."), true);
			return;
		}
		Entity entity = thief.getServerWorld().getEntityById(targetEntityId);
		if (!(entity instanceof ServerPlayerEntity target) || target == thief) {
			thief.sendMessage(Text.literal("You can't steal from nothing, go rob a player"), true);
			return;
		}
		if (target.isSpectator() || target.isCreative()) {
			thief.sendMessage(Text.literal("You can't steal from Master"), true);
			return;
		}
		if (thief.squaredDistanceTo(target) > STEAL_RANGE * STEAL_RANGE) {
			thief.sendMessage(Text.literal("You are too far away to steal."), true);
			return;
		}
		if (thiefState.getStealTargetEntityId() != -1) {
			thief.sendMessage(Text.literal("You are already stealing, chill out"), true);
			return;
		}
		thiefState.setStealTargetEntityId(target.getId());
		thiefState.setStealStartTick(now);
		thiefState.setStealTargetStartPos(target.getBlockPos());
		thief.sendMessage(Text.literal("Doing what you do best, now make sure they don't move"), true);
	}


	private static void tickThiefSteal(ServerPlayerEntity thief, ClassState state) {
		int targetId = state.getStealTargetEntityId();
		if (targetId == -1) return;

		Entity entity = thief.getServerWorld().getEntityById(targetId);
		if (!(entity instanceof ServerPlayerEntity target)) {
			state.clearStealAttempt();
			return;
		}
		long now = thief.getServerWorld().getTime();
		if (thief.squaredDistanceTo(target) > STEAL_RANGE * STEAL_RANGE) {
			thief.sendMessage(Text.literal("Steal failed: you moved too far"), true);
			state.clearStealAttempt();
			return;
		}
		BlockPos startPos = state.getStealTargetStartPos();
		if (startPos == null || !target.getBlockPos().equals(startPos)) {
			thief.sendMessage(Text.literal("Steal failed: target moved."), true);
			state.clearStealAttempt();
			return;
		}
		if (thief.hurtTime > 0) {
			thief.sendMessage(Text.literal("Steal failed: you were interrupted."), true);
			state.clearStealAttempt();
			return;
		}
		if (now - state.getStealStartTick() >= STEAL_CHANNEL_TICKS) {
			boolean success = stealRandomItem(thief, target);
			if (success) {
				state.setLastStealTime(now);
			}
			state.clearStealAttempt();
		}

	}
	private static boolean stealRandomItem(ServerPlayerEntity thief, ServerPlayerEntity target) {
		PlayerInventory inv = target.getInventory();
		List<Integer> validSlots = new ArrayList<>();
		for (int slot = 0; slot < 36; slot++) {
			ItemStack stack = inv.getStack(slot);
			if (stack.isEmpty()) continue;
			validSlots.add(slot);
		}
		if (validSlots.isEmpty()) {
			thief.sendMessage(Text.literal("Steal failed: target is broke, you're not Jewish so taking pennies isn't your thing"), true);
			return false;
		}
		int chosenSlot = validSlots.get(thief.getRandom().nextInt(validSlots.size()));
		ItemStack targetStack = inv.getStack(chosenSlot);
	//	ItemStack stolen = targetStack.split(1);
		ItemStack stolen = targetStack.copy();
		ItemStack displayStack = stolen.copy();
		boolean inserted = thief.getInventory().insertStack(stolen);
		if (!inserted) {
			thief.dropItem(stolen, false);
		}
		String itemName = displayStack.getName().getString();
		String targetName = target.getName().getString();
		thief.sendMessage(Text.literal("You stole ")
				.append(Text.literal(itemName).formatted(Formatting.GOLD))
				.append(Text.literal(" from "))
				.append(Text.literal(targetName).formatted(Formatting.DARK_GRAY))
				.append(Text.literal("!")), true);
		target.sendMessage(Text.literal("A ")
				.append(Text.literal("BLACK").formatted(Formatting.BLACK))
				.append(Text.literal(" stole from you! CALL THE IRON GOLEM!!")), true);
		return true;
	}
	private static String formatStealCooldown(long ticks) {
		int totalSeconds = (int) Math.ceil(ticks / 20.0);
		int minutes = totalSeconds / 60;
		int seconds = totalSeconds % 60;

		if (minutes > 0) {
			return minutes + "m " + seconds + "s";
		}
		return seconds + "s";
	}

}




