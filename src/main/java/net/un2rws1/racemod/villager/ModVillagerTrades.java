package net.un2rws1.racemod.villager;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.un2rws1.racemod.item.ModItems;

public class ModVillagerTrades {

    public static void registerVillagerTrades() {

        // Level 1 - Novice
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.RABBI, 1, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 7),
                    new ItemStack(Items.COAL, 6),
                    16, 2, 0.05f
            ));

            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 8),
                    new ItemStack(Items.CHARCOAL, 6),
                    16, 2, 0.05f
            ));
        });

        // Level 2 - Apprentice
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.RABBI, 2, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 16),
                    new ItemStack(Items.IRON_BLOCK, 2),
                    5, 15, 0.05f
            ));

            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 32),
                    new ItemStack(Items.EMERALD, 16),
                    7, 15, 0.05f
            ));
        });

        // Level 3 - Journeyman
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.RABBI, 3, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 16),
                    new ItemStack(Items.GOLDEN_APPLE, 1),
                    8, 20, 0.05f
            ));
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 32),
                    new ItemStack(Items.EXPERIENCE_BOTTLE, 8),
                    8, 20, 0.05f
            ));
        });

        // Level 4 - Expert
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.RABBI, 4, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 32),
                    new ItemStack(Items.ENDER_PEARL, 3),
                    6, 30, 0.05f
            ));
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 64),
                    new ItemStack(Items.DIAMOND_BLOCK, 2),
                    6, 30, 0.05f
            ));
        });

        // Level 5 - Master
        TradeOfferHelper.registerVillagerOffers(ModVillagerProfessions.RABBI, 5, factories -> {
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 64),
                    new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1),
                    3, 50, 0.05f
            ));
            factories.add((entity, random) -> new TradeOffer(
                    new TradedItem(ModItems.GOLDEN_COINS, 64),
                    new ItemStack(Items.TOTEM_OF_UNDYING, 1),
                    3, 50, 0.05f
            ));

        });
    }
}