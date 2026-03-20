package net.un2rws1.racemod.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.un2rws1.racemod.block.ModBlocks;
import net.un2rws1.racemod.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output){
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator){
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.POOP_COOKED_BLOCK);
    blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.POOP_BLOCK);

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.RABBI_TOTEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.POOP, Models.GENERATED);
        itemModelGenerator.register(ModItems.HAVA_NAGILA_MUSIC_DISC, Models.GENERATED);
    }
}
