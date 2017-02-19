package com.jaynopp.saiyancraft.eventhandlers;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event){
		System.out.println("PLAYER CLONE!");
		ISaiyanData originalCap = event.getOriginal().getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		ISaiyanData newCap = event.getEntityPlayer().getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		newCap.UpdateFrom(originalCap);
		SaiyanCraft.network.sendTo(new SyncSaiyanDataMessage(newCap), (EntityPlayerMP) event.getEntityPlayer());
	}
}
