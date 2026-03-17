package net.un2rws1.racemod.block;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;

import java.util.UUID;
import java.util.HashMap;

public class Poop_Block extends Block {
    private static final HashMap<UUID, Integer> STANDING_TIME = new HashMap<>();

    public Poop_Block(Settings settings) {
        super(settings);
    }

    private static final HashMap<UUID, Long> LAST_TOUCHED_TICK = new HashMap<>();

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {

        if (!(entity instanceof LivingEntity livingEntity)) return;
        if (!world.isClient && entity instanceof ServerPlayerEntity player) {
            if (ClassManager.getPlayerClass(player) != PlayerClass.INDIAN) {

                boolean hasFullArmor = !livingEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty() &&
                        !livingEntity.getEquippedStack(EquipmentSlot.CHEST).isEmpty() &&
                        !livingEntity.getEquippedStack(EquipmentSlot.LEGS).isEmpty() &&
                        !livingEntity.getEquippedStack(EquipmentSlot.FEET).isEmpty();

                if (!hasFullArmor) {
                    entity.slowMovement(state, new Vec3d(0.9D, 1.5D, 0.9D));
                    UUID uuid = entity.getUuid();
                    long currentTick = world.getTime();
                    long lastTick = LAST_TOUCHED_TICK.getOrDefault(uuid, 0L);
                    if (currentTick - lastTick > 3) {
                            STANDING_TIME.put(uuid, 0);
                        }
                        LAST_TOUCHED_TICK.put(uuid, currentTick);
                        int time = STANDING_TIME.getOrDefault(uuid, 0);
                        if (time > 40 && time < 100) {
                            ((ServerWorld) world).spawnParticles(
                                    ParticleTypes.SNEEZE,
                                    entity.getX(), entity.getY() + 0.2, entity.getZ(),
                                    3,
                                    0.3, 0.1, 0.3,
                                    0.02
                            );
                        }


                        if (time >= 100) {
                            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 120, 0));
                            livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 120, 4));
                            ((ServerWorld) world).spawnParticles(
                                    ParticleTypes.SMOKE,
                                    entity.getX(), entity.getY() + 0.8, entity.getZ(),
                                    20, 0.4, 0.4, 0.4, 0.1
                            );
                            STANDING_TIME.put(uuid, 0);
                        } else {
                            STANDING_TIME.put(uuid, time + 1);
                        }
                    }
                }
            }
        }
    }
