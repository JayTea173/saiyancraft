package com.jaynopp.saiyancraft.eventhandlers;

import org.w3c.dom.Entity;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.*;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

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
