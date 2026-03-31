package net.un2rws1.racemod.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CovidFoodItem extends Item {

    public CovidFoodItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        ItemStack result = super.finishUsing(stack, world, user);

        if (!world.isClient) {
            user.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.POISON, Integer.MAX_VALUE, 0)
            );
        }

        return result;
    }
}