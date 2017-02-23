package com.jaynopp.saiyancraft.capabilities;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SaiyanBattlerProvider;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SyncSaiyanBattlerMessage;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;
import com.jaynopp.saiyancraft.init.ModMoves;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class CapabilityHandler {
	public static final ResourceLocation POWERLEVEL_CAP = new ResourceLocation(SaiyanCraft.modId, "powerlevel");
	public static final ResourceLocation BATTLER_CAP = new ResourceLocation(SaiyanCraft.modId, "battler");
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void attachCapability(AttachCapabilitiesEvent.Entity event){
		Entity entity = event.getEntity();
		if ((event.getEntity() instanceof EntityPlayer)){
			if (!entity.hasCapability(SaiyanDataProvider.POWERLEVEL_CAP, null)){
				event.addCapability(POWERLEVEL_CAP, new SaiyanDataProvider());
				ISaiyanData.carriers.add(event.getEntity());
			}
		}
		if (!entity.hasCapability(SaiyanBattlerProvider.BATTLER_CAP, null)){
			event.addCapability(BATTLER_CAP, new SaiyanBattlerProvider());
			ISaiyanBattler.carriers.add(event.getEntity());
		}
		
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
		if (player.hasCapability(SaiyanBattlerProvider.BATTLER_CAP, null)){
			ISaiyanBattler cap = player.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
			if (cap.GetMoves().size() == 0){
				ModMoves.CreateDefaultMoves(cap);
				System.out.println("Created default moves for " + event.player.getName());
			}
			cap.SetOwner(event.player);
			System.out.println("Has saiyanbattler for " + event.player.getName() + " moves: " + cap.GetMoves().size());
			//DefaultSaiyanData.UpdateStats(player);
			//System.out.println("SaiyanData on Server: " + cap.GetPowerLevel());
			SaiyanCraft.network.sendTo(new SyncSaiyanBattlerMessage(cap), (EntityPlayerMP) player);
		} else {
			System.out.println("No saiyanbattler for " + event.player.getName());
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event){
		if (!event.getWorld().isRemote){
			Entity entity = event.getEntity();
			if (entity instanceof EntityPlayer){
				EntityPlayer player = (EntityPlayer) entity;
				if (player.hasCapability(SaiyanDataProvider.POWERLEVEL_CAP, null)){
					ISaiyanData cap = player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
					DefaultSaiyanData.UpdateStats(player);
					//System.out.println("SaiyanData on Server (WorldJoin): " + cap.GetPowerLevel());
					SaiyanCraft.network.sendTo(new SyncSaiyanDataMessage(cap), (EntityPlayerMP) player);
				}
			}
		}
	}
	
	
	
	
}
