package net.un2rws1.racemod.block;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.un2rws1.racemod.Racemod;

public class ModBlocks {

    public static final Block SLOT_MACHINE = registerBlock(
            "slot_machine",
            new Slot_Machine(AbstractBlock.Settings.create()
                    .mapColor(MapColor.IRON_GRAY)
                    .strength(4.0f)
                    .sounds(BlockSoundGroup.METAL)
                    .requiresTool())
    );
    public static final Block POOP_BLOCK =
           registerBlock("poop_block",
                   new Poop_Block(AbstractBlock.Settings.create()
                           .strength(2f, 3f).requiresTool()
                          .sounds(BlockSoundGroup.MUD)
                           .noCollision()
                           .allowsSpawning((state, world, pos, type) -> false)
                           .dynamicBounds()
                   )
           );
   public static final Block POOP_COOKED_BLOCK = registerBlock("poop_cooked_block",
           new Block(AbstractBlock.Settings.create()
                   .strength(5f, 3f).requiresTool()
                   .sounds(BlockSoundGroup.MUD_BRICKS)
           )
   );
    public static final Block BRICK_POOP_BLOCK = registerBlock(
            "brick_poop_block",
            new Brick_Poop_Block(Block.Settings.create()
                    .strength(3.0f, 3.0f)
                    .sounds(BlockSoundGroup.TUFF_BRICKS)
                    .requiresTool())
    );


    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
       return Registry.register(Registries.BLOCK, Identifier.of(Racemod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Racemod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }



    public static void registerModBlocks() {
        Racemod.LOGGER.info("Registering Mod Blocks for " + Racemod.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
                entries.add(ModBlocks.POOP_BLOCK);
        });
    }
}
