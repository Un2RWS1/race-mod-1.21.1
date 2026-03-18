package net.un2rws1.racemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
import net.un2rws1.racemod.block.ModBlocks;
import net.un2rws1.racemod.classsystem.ClassAttachmentTypes;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.un2rws1.racemod.command.ClassCommand;
import net.un2rws1.racemod.entity.ModEntities;
import net.un2rws1.racemod.event.PlayerJoinHandler;
import net.un2rws1.racemod.item.ModItemGroups;
import net.un2rws1.racemod.item.ModItems;
import net.un2rws1.racemod.networking.ModNetworking;
import net.un2rws1.racemod.sound.ModSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.un2rws1.racemod.event.PlayerRespawnHandler;

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
		//classes food, you cant eat certain food
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getStackInHand(hand);
			PlayerClass playerClass = ClassManager.getPlayerClass(player);
			Item item = stack.getItem();
			if (stack.get(DataComponentTypes.FOOD) == null) {
				return TypedActionResult.pass(stack);
			}
			if (playerClass == null) {
					return TypedActionResult.pass(stack);
			}

			if (playerClass == PlayerClass.JEW) {
				if(		item == Items.PORKCHOP ||
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


		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				ClassCommand.register(dispatcher));
	}
}





