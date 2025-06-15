package com.example.block;

import com.example.TutorialMod;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    // This class would typically contain block definitions and registration methods.
    // For example, you might define blocks like this:
    public static final Block PINK_GARNET_BLOCK = registerBlock("pink_garnet_block", new Block(
            AbstractBlock.Settings.create()
            .strength(4f)
            .requiresTool()
            .sounds(BlockSoundGroup.AMETHYST_BLOCK)
    ));

    public static final Block RAW_PINK_GARNET_BLOCK = registerBlock("raw_pink_garnet_block", new Block(
            AbstractBlock.Settings.create()
            .strength(3f)
            .requiresTool()
    ));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(TutorialMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(TutorialMod.MOD_ID, name), new BlockItem(block, new Item.Settings()));
    }

    public static void registerBlocks() {
        TutorialMod.LOGGER.info("Registering blocks for " + TutorialMod.MOD_ID);
         // Register your blocks here

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entity -> {
            entity.add(PINK_GARNET_BLOCK);
            entity.add(RAW_PINK_GARNET_BLOCK);
        });
    }
}
