package net.un2rws1.racemod;

import net.fabricmc.api.ModInitializer;

import net.un2rws1.racemod.item.ModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Racemod implements ModInitializer {
	public static final String MOD_ID = "race-mod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();

	}
}