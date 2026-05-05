package net.un2rws1.racemod.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.un2rws1.racemod.item.ModItems;

public class PoopEntity extends ThrownItemEntity {

    public PoopEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public PoopEntity(World world, LivingEntity owner) {
        super(ModEntities.POOP, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.POOP;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity target = entityHitResult.getEntity();

        if (target instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.POISON,
                    60,
                    1
            ));
        }

    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (!this.getWorld().isClient) {
            ((ServerWorld) this.getWorld()).spawnParticles(
                    ParticleTypes.WITCH, //make it brown
                    this.getX(), this.getY(), this.getZ(),
                    20, // count
                    0.3, 0.3, 0.3, // spread
                    0.1 // speed
            );
            this.getWorld().playSound(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    SoundEvents.BLOCK_SLIME_BLOCK_STEP,
                    SoundCategory.PLAYERS,
                    1.0f,
                    1.0f
            );
            this.discard();
        }
    }
}