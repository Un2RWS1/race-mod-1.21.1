package net.un2rws1.racemod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> BLURRY_VISION =
            Registry.registerReference(
                    Registries.STATUS_EFFECT,
                    Identifier.of(Racemod.MOD_ID, "blurry_vision"),
                    new BlurryVisionEffect()
            );

    public static void registerEffects() {
        Racemod.LOGGER.info("Registering effects for " + Racemod.MOD_ID);
    }
}