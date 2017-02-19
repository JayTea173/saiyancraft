package com.jaynopp.saiyancraft.init;

import com.jaynopp.core.item.JNBlockInitializer;
import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.block.BlockTest;
import com.jaynopp.saiyancraft.lib.Names;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
	
	public static void init(){
		//Add all Blocks here
		BlockTest.Initialize();
	}
	
	@SideOnly(Side.CLIENT)
	public static void initClient(ItemModelMesher mesher){
		System.out.println("Initializing SayanCraft Client!");
		for ( Block i: JNBlockInitializer.initializedBlocks)
			registerModel(Item.getItemFromBlock(i), mesher);		
	}
	
	private static void registerModel(Item item, ItemModelMesher mesher){
		String itemName = item.getRegistryName().toString();
		System.out.println("Registering item: " + item.getUnlocalizedName() + "@" + itemName + ", should be " + SaiyanCraft.resourcePrefix + Names.Items.DRAGON_BALL);
		ModelResourceLocation model = new ModelResourceLocation(itemName, "inventory");
		ModelLoader.registerItemVariants(item, model);
		mesher.register(item, 0, model);
	}
}
