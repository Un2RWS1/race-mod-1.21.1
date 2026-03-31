package net.un2rws1.racemod.villager;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.poi.PointOfInterestType;
import net.un2rws1.racemod.Racemod;
import net.un2rws1.racemod.block.ModBlocks;
import net.un2rws1.racemod.mixin.PointOfInterestTypesAccessor;

import java.util.Set;

public class ModPointOfInterestTypes {

    public static final RegistryKey<PointOfInterestType> SLOT_MACHINE_POI_KEY =
            RegistryKey.of(
                    Registries.POINT_OF_INTEREST_TYPE.getKey(),
                    Identifier.of(Racemod.MOD_ID, "slot_machine_poi")
            );

    public static final PointOfInterestType SLOT_MACHINE_POI = Registry.register(
            Registries.POINT_OF_INTEREST_TYPE,
            Identifier.of(Racemod.MOD_ID, "slot_machine_poi"),
            new PointOfInterestType(
                    Set.copyOf(ModBlocks.SLOT_MACHINE.getStateManager().getStates()),
                    1,
                    1
            )
    );

    public static void registerModPoiTypes() {
        Racemod.LOGGER.info("Registering casino dealer POI");

        RegistryEntry.Reference<PointOfInterestType> entry =
                Registries.POINT_OF_INTEREST_TYPE.getEntry(SLOT_MACHINE_POI_KEY).orElseThrow();

        PointOfInterestTypesAccessor.racemod$registerStates(
                entry,
                Set.copyOf(ModBlocks.SLOT_MACHINE.getStateManager().getStates())
        );
    }
}