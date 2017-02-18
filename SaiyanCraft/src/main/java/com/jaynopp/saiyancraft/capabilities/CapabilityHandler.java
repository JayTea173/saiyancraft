package com.jaynopp.saiyancraft.capabilities;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SaiyanBattlerProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CapabilityHandler {
	public static final ResourceLocation POWERLEVEL_CAP = new ResourceLocation(SaiyanCraft.modId, "powerlevel");
	public static final ResourceLocation BATTLER_CAP = new ResourceLocation(SaiyanCraft.modId, "battler");
	
	private static final String SaiyanPlayerEntity = null;
	
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent.Entity event){
		if (!(event.getEntity() instanceof EntityPlayer)) return;
		EntityPlayer player = (EntityPlayer)event.getEntity();
		if (!player.hasCapability(SaiyanDataProvider.POWERLEVEL_CAP, null))
			event.addCapability(POWERLEVEL_CAP, new SaiyanDataProvider());
		
		if (!player.hasCapability(SaiyanBattlerProvider.BATTLER_CAP, null))
			event.addCapability(BATTLER_CAP, new SaiyanBattlerProvider());
		
	}
	
	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event){
		EntityPlayer player = event.player;
		if (player.hasCapability(SaiyanDataProvider.POWERLEVEL_CAP, null)){
			ISaiyanData cap = player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
			DefaultSaiyanData.UpdateStats(player);
			//System.out.println("SaiyanData on Server: " + cap.GetPowerLevel());
			SaiyanCraft.network.sendTo(new SyncSaiyanDataMessage(cap), (EntityPlayerMP) player);
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event){
		if (!event.getWorld().isRemote){
			if (!(event.getEntity() instanceof EntityPlayer)) return;
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if (player.hasCapability(SaiyanDataProvider.POWERLEVEL_CAP, null)){
				ISaiyanData cap = player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
				DefaultSaiyanData.UpdateStats(player);
				//System.out.println("SaiyanData on Server (WorldJoin): " + cap.GetPowerLevel());
				SaiyanCraft.network.sendTo(new SyncSaiyanDataMessage(cap), (EntityPlayerMP) player);
			}
		}
	}
	
	
	
	
}
