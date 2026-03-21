package net.un2rws1.racemod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.un2rws1.racemod.block.ModBlocks;
import net.un2rws1.racemod.damage.ModDamageTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin {

    @Shadow public abstract BlockState getBlockState();

    @Shadow private boolean hurtEntities;
    @Shadow private float fallHurtAmount;
    @Shadow private int fallHurtMax;

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void racemod$handleCustomBlockFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        FallingBlockEntity self = (FallingBlockEntity) (Object) this;

        if (!self.getBlockState().isOf(ModBlocks.BRICK_POOP_BLOCK)) {
            return;
        }

        if (!this.hurtEntities) {
            cir.setReturnValue(false);
            return;
        }

        int i = MathHelper.ceil(fallDistance - 1.0F);
        if (i <= 0) {
            cir.setReturnValue(false);
            return;
        }

        World world = self.getWorld();
        RegistryEntry<DamageType> damageTypeEntry = world.getRegistryManager()
                .get(RegistryKeys.DAMAGE_TYPE)
                .entryOf(ModDamageTypes.CRUSHED_BY_BRICK_POOP_BLOCK);

        DamageSource customSource = new DamageSource(damageTypeEntry);

        int damage = Math.min(MathHelper.floor((float) i * this.fallHurtAmount), this.fallHurtMax);

        for (Entity entity : world.getOtherEntities(self, self.getBoundingBox())) {
            entity.damage(customSource, damage);
        }

        cir.setReturnValue(false);
    }
}