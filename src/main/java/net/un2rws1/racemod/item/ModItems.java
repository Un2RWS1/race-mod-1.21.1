package net.un2rws1.racemod.item;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.sound.ModSounds;

import static net.un2rws1.racemod.entity.ModEntities.POOP;

public class ModItems {
    public static final Item POOP = Registry.register(
            Registries.ITEM,
            Identifier.of("race-mod", "poop"),
            new PoopItem(new Item.Settings().maxCount(16))
    );
    public static final Item KIPPAH = registerItem("kippah", new ArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET,
            new Item.Settings().maxCount(1).maxDamage(0)));
    public static final Item RABBI_TOTEM = registerItem("rabbi_totem", new Item(new Item.Settings().maxCount(1)));
    public static final Item HAVA_NAGILA_MUSIC_DISC = registerItem("hava_nagila_music_disc",
            new Item(new Item.Settings().jukeboxPlayable(ModSounds.HAVA_NAGILA_KEY).maxCount(1)));

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
