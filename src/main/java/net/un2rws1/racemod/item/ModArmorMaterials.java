package net.un2rws1.racemod.item;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;

import java.util.List;
import java.util.Map;


public class ModArmorMaterials {
    public static final RegistryEntry<ArmorMaterial> KIPPAH = Registry.registerReference(
            Registries.ARMOR_MATERIAL,
            Identifier.of(Racemod.MOD_ID, "jew"),
            new ArmorMaterial(
                    Map.of(
                            ArmorItem.Type.BOOTS, 0,
                            ArmorItem.Type.LEGGINGS, 0,
                            ArmorItem.Type.CHESTPLATE, 0,
                            ArmorItem.Type.HELMET, 0,
                            ArmorItem.Type.BODY, 0
                    ),
                    0,
                    (SoundEvents.ITEM_ARMOR_EQUIP_GENERIC),
                    () -> Ingredient.EMPTY,
                    List.of(new ArmorMaterial.Layer(Identifier.of(Racemod.MOD_ID, "kippah"))),
                    0.0F,
                    0.0F
            )
    );
    public static final RegistryEntry<ArmorMaterial> GLASSES = Registry.registerReference(
            Registries.ARMOR_MATERIAL,
            Identifier.of(Racemod.MOD_ID, "chinese"),
            new ArmorMaterial(
            Map.of(
                    ArmorItem.Type.BOOTS, 0,
                    ArmorItem.Type.LEGGINGS, 0,
                    ArmorItem.Type.CHESTPLATE, 0,
                    ArmorItem.Type.HELMET, 0
            ),
            10,
            (SoundEvents.ITEM_ARMOR_EQUIP_CHAIN),
            () -> Ingredient.ofItems(Items.GLASS),
            List.of(new ArmorMaterial.Layer(Identifier.of(Racemod.MOD_ID, "glasses"))),
            0F,
            0f
            )
    );
}