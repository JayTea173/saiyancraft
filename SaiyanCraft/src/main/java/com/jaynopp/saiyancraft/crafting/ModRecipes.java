package com.jaynopp.saiyancraft.crafting;

import com.jaynopp.saiyancraft.block.BlockTest;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {
	
	public static void addRecipes() {
		//shapeless crafting recipes
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.cornSeed), new ItemStack(ModItems.corn));
	
		//shaped crafting recipes
		GameRegistry.addRecipe(new ItemStack(BlockTest.instance), "###", "###", "###", '#', new ItemStack(Blocks.COBBLESTONE));
		}
		
		//smelting recipes
		//GameRegistry.addSmelting(ModBlocks.oreCopper, new ItemStack(ModItems.ingotCopper), 1.0f);
		
	}
