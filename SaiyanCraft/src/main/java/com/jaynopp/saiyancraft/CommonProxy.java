package com.jaynopp.saiyancraft;

import java.io.IOException;

import com.jaynopp.saiyancraft.capabilities.CapabilityHandler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.DefaultSaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.eventhandlers.EventHandler;
import com.jaynopp.saiyancraft.init.ModBlocks;
import com.jaynopp.saiyancraft.init.ModItems;
import com.jaynopp.saiyancraft.init.ModSounds;
import com.jaynopp.saiyancraft.item.ItemValueManager;
import com.jaynopp.saiyancraft.player.SaiyanPlayerCommonEventHandler;
import com.jaynopp.saiyancraft.storage.SaiyanBattlerStorage;
import com.jaynopp.saiyancraft.storage.SayanDataStorage;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	
	public void preInit(FMLPreInitializationEvent event){	
		SaiyanCraft.itemValueManager = new ItemValueManager();
		try {
			SaiyanCraft.itemValueManager.Initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CapabilityManager.INSTANCE.register(ISaiyanData.class, new SayanDataStorage(), DefaultSaiyanData.class);
		CapabilityManager.INSTANCE.register(ISaiyanBattler.class, new SaiyanBattlerStorage() , DefaultSaiyanBattler.class);
		MinecraftForge.EVENT_BUS.register(new CapabilityHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new SaiyanPlayerCommonEventHandler());
		ModItems.init();
		ModBlocks.init();
		ModSounds.RegisterSounds();
	}
	
	public void init(FMLInitializationEvent event){

	}
	
	public void postInit(FMLPostInitializationEvent event){
		
		SaiyanCraft.itemValueManager.GetValue(Blocks.PLANKS);
	}
	

}
