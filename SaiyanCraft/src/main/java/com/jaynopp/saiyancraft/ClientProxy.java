package com.jaynopp.saiyancraft;

import com.jaynopp.saiyancraft.init.ModBlocks;
import com.jaynopp.saiyancraft.init.ModItems;
import com.jaynopp.saiyancraft.input.KeyBindings;
import com.jaynopp.saiyancraft.input.KeyInputHandler;
import com.jaynopp.saiyancraft.item.ItemEventHandler;
import com.jaynopp.saiyancraft.player.SaiyanPlayerClientEventHandler;
import com.jaynopp.saiyancraft.player.StatusBarEventHandler;
import com.jaynopp.saiyancraft.player.gui.SaiyanHud;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	SaiyanHud hud;
	
	
	@Override
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
	}
	
	@SuppressWarnings("deprecation")
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
		MinecraftForge.EVENT_BUS.register(new ItemEventHandler());
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event){

	}
}
