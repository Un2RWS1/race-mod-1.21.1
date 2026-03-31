package net.un2rws1.racemod.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup INDIAN_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Racemod.MOD_ID, "indian_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.POOP))
                    .displayName(Text.translatable("itemgroup.race-mod.indian_items"))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.POOP);
                        entries.add(ModBlocks.POOP_BLOCK);
                        entries.add(ModItems.COOKED_POOP);
                        entries.add(ModBlocks.POOP_COOKED_BLOCK);
                        entries.add(ModItems.POOP_BRICK);
                        entries.add(ModBlocks.BRICK_POOP_BLOCK);
                    }))
                    .build());

    public static final ItemGroup JEWISH_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Racemod.MOD_ID, "jewish_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.RABBI_TOTEM))
                    .displayName(Text.translatable("itemgroup.race-mod.jewish_items"))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.RABBI_TOTEM);
                        entries.add(ModItems.HAVA_NAGILA_MUSIC_DISC);
                        entries.add(ModItems.KIPPAH);
                        entries.add(ModItems.GOLDEN_COINS);
                        entries.add(ModItems.DAVID_STAR);
                    }))
                    .build());
    public static final ItemGroup BLACK_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Racemod.MOD_ID, "black_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.KOOL_AID))
                    .displayName(Text.translatable("itemgroup.race-mod.black_items"))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.KOOL_AID);
                        entries.add(ModItems.KFC_BUCKET);
                    }))
                    .build());
    public static final ItemGroup CHINESE_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
                    Identifier.of(Racemod.MOD_ID, "chinese_items"),
                    FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.GLASSES))
                    .displayName(Text.translatable("itemgroup.race-mod.chinese_items"))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.GLASSES);
                        entries.add(ModItems.CAT_MEAT);
                        entries.add(ModItems.WOLF_MEAT);
                        entries.add(ModItems.COOKED_CAT_MEAT);
                        entries.add(ModItems.COOKED_WOLF_MEAT);
            }))
        .build());
    public static final ItemGroup MEXICAN_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Racemod.MOD_ID, "mexican_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.GREEN_CARD))
                    .displayName(Text.translatable("itemgroup.race-mod.mexican_items"))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.GREEN_CARD);

                    }))
                    .build());
    public static final ItemGroup RACE_MOD_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(Racemod.MOD_ID, "race_mod_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.MUSIC_DISC_BASE))
                    .displayName(Text.translatable("itemgroup.race-mod.race_mod_items"))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.MUSIC_DISC_BASE);
                        entries.add(ModBlocks.SLOT_MACHINE);
                    }))
                    .build());

    public static void registerItemGroups() {
        Racemod.LOGGER.info("Registering Item Groups for " + Racemod.MOD_ID);
    }
}
