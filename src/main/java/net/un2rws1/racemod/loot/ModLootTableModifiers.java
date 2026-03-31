package net.un2rws1.racemod.loot;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.KilledByPlayerLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.loot.LootTable;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.item.ModItems;

public class ModLootTableModifiers {

    private static final RegistryKey<LootTable> WOLF_LOOT =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "entities/wolf"));
    private static final RegistryKey<LootTable> OCELOT_LOOT =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "entities/ocelot"));
    private static final RegistryKey<LootTable> VILLAGER_LOOT =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "entities/villager"));
    private static final RegistryKey<LootTable> BAT_LOOT =
            RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of("minecraft", "entities/bat"));

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (WOLF_LOOT.equals(key)) {
                LootPool.Builder pool = LootPool.builder()
                        .rolls(UniformLootNumberProvider.create(1.0f, 2.0f))
                        .with(ItemEntry.builder(ModItems.WOLF_MEAT))
                        .conditionally(KilledByPlayerLootCondition.builder());

                tableBuilder.pool(pool);
            }
            if (OCELOT_LOOT.equals(key)) {
                tableBuilder.pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0f, 1.0f))
                                .with(ItemEntry.builder(ModItems.CAT_MEAT))
                );
            }
            if (VILLAGER_LOOT.equals(key)) {
                tableBuilder.pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0f, 5.0f))
                                .with(ItemEntry.builder(ModItems.GOLDEN_COINS))
                );
            }
            if(BAT_LOOT.equals(key)){
                tableBuilder.pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0f, 1.0f))
                                .with(ItemEntry.builder(ModItems.BAT_MEAT))
                );
            }
        });
    }

}