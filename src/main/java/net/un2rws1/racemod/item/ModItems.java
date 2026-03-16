package net.un2rws1.racemod.item;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;

public class ModItems {
    public static final Item POOP = registerItem("poop", new Item(new Item.Settings()));
    public static final Item RABBI_TOTEM = registerItem("rabbi_totem", new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, Identifier.of(Racemod.MOD_ID, name), item);
    } //helper method

    public static void registerModItems(){
        Racemod.LOGGER.info("Registering Mod Items for " + Racemod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(fabricItemGroupEntries -> {
            fabricItemGroupEntries.add(POOP);
            fabricItemGroupEntries.add(RABBI_TOTEM);
        });
    }
}
