package com.jaynopp.core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public interface JNBlockInitializer {
	public static List<Block> initializedBlocks = new ArrayList<Block>();
	
	public default void Initialize(Block block, String modResourcePrefix, String blockName){
		System.out.println("Registering new block: " + modResourcePrefix + blockName);
		String resourcePath = modResourcePrefix + blockName;
		ResourceLocation location = new ResourceLocation(resourcePath);
		block.setRegistryName(location);
		block.setUnlocalizedName(resourcePath); 
		GameRegistry.register(block);
		GameRegistry.register(new ItemBlock(block), location);
		initializedBlocks.add(block);
	}
	
}
