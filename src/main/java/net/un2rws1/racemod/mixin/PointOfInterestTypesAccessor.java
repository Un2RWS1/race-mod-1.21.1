package net.un2rws1.racemod.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.poi.PointOfInterestType;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@Mixin(PointOfInterestTypes.class)
public interface PointOfInterestTypesAccessor {

    @Invoker("registerStates")
    static void racemod$registerStates(RegistryEntry<PointOfInterestType> poiEntry, Set<BlockState> states) {
        throw new AssertionError();
    }
}