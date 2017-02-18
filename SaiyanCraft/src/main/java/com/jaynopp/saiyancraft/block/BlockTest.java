package com.jaynopp.saiyancraft.block;

import com.jaynopp.core.item.JNBlockInitializer;
import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.item.DragonBall;
import com.jaynopp.saiyancraft.lib.Names;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockTest extends Block implements JNBlockInitializer{
	public static BlockTest instance;
	
	public static void Initialize(){
		instance = new BlockTest();
	}
	
	public BlockTest() {
		super(Material.ROCK);
		JNBlockInitializer.super.Initialize((Block)this, SaiyanCraft.resourcePrefix, Names.Tiles.TEST_BLOCK);
		setHardness(500.0f);
		setResistance(15.0f);
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 1);
		setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		// TODO Auto-generated constructor stub
		

	}
}
