package com.jaynopp.saiyancraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.init.ModBlocks;
import com.jaynopp.saiyancraft.init.ModItems;
import com.jaynopp.saiyancraft.input.KeyBindings;
import com.jaynopp.saiyancraft.input.KeyInputHandler;
import com.jaynopp.saiyancraft.player.SaiyanPlayerClientEventHandler;
import com.jaynopp.saiyancraft.player.StatusBarEventHandler;
import com.jaynopp.saiyancraft.player.gui.SaiyanHud;

public class ClientProxy extends CommonProxy {
	SaiyanHud hud;
	
	
	@Override
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event){
		super.init(event);
		
		hud = new SaiyanHud();
		ModItems.initClient(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		ModBlocks.initClient(Minecraft.getMinecraft().getRenderItem().getItemModelMesher());
		KeyBindings keyBindings = new KeyBindings();
		keyBindings.Init();
		FMLCommonHandler.instance().bus().register(new KeyInputHandler());
		MinecraftForge.EVENT_BUS.register(new SaiyanPlayerClientEventHandler());
		MinecraftForge.EVENT_BUS.register(new StatusBarEventHandler());
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){
		super.postInit(event);
	}
}
