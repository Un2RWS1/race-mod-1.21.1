package net.un2rws1.racemod.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<PoopEntity> POOP =
            Registry.register(Registries.ENTITY_TYPE,
                    Identifier.of("race-mod", "poop_projectile"),
                    EntityType.Builder.<PoopEntity>create(PoopEntity::new, SpawnGroup.MISC)
                            .dimensions(0.25f, 0.25f)
                            .maxTrackingRange(4)
                            .trackingTickInterval(10)
                            .build()
            );
    public static void register() {
        System.out.println("Registering Mod Entities for race-mod");
    }
}