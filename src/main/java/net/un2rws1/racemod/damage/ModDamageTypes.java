package net.un2rws1.racemod.damage;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> CRUSHED_BY_BRICK_POOP_BLOCK =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE,
                    Identifier.of(Racemod.MOD_ID, "crushed_by_brick_poop_block"));
}