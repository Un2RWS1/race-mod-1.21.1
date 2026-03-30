package net.un2rws1.racemod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BlurryVisionEffect extends StatusEffect {
    public BlurryVisionEffect() {
        super(StatusEffectCategory.HARMFUL, 0xA9A9A9);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return false;
    }
}