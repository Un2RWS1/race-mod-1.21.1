package net.un2rws1.racemod.villager;

import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.un2rws1.racemod.Racemod;

public class ModVillagerProfessions {

    public static final VillagerProfession RABBI = Registry.register(
            Registries.VILLAGER_PROFESSION,
            Identifier.of(Racemod.MOD_ID, "rabbi"),
            VillagerProfessionBuilder.create()
                    .id(Identifier.of(Racemod.MOD_ID, "rabbi"))
                    .workstation(ModPointOfInterestTypes.SLOT_MACHINE_POI_KEY)
                    .workSound(SoundEvents.ENTITY_VILLAGER_WORK_TOOLSMITH)
                    .build()
    );

    public static void registerVillagerProfessions() {
        Racemod.LOGGER.info("Registering casino dealer profession: {}",
                Registries.VILLAGER_PROFESSION.getId(RABBI));
    }
}