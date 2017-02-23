package com.jaynopp.saiyancraft.capabilities.saiyandata;

import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SaiyanDataMessageHandler implements IMessageHandler<SyncSaiyanDataMessage, IMessage> {

	@Override
	public IMessage onMessage(SyncSaiyanDataMessage message, MessageContext ctx) {
		System.out.println("Sync SAIYANDATA!");
		if (ctx.side.isServer()){
			EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
			
			IThreadListener mainThread = (WorldServer) sender.world;
			mainThread.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					sender.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null).UpdateFrom(message.sd);
					DefaultSaiyanData.UpdateStats(sender);
				}
			});
			
		} else {
			
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					EntityPlayerSP player = Minecraft.getMinecraft().player;
					
					ISaiyanData data = player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
					data.UpdateFrom(message.sd);
					System.out.println("new PL: " + message.sd.GetPowerLevel() + " -> " + data.GetPowerLevel());
					DefaultSaiyanData.UpdateStats(player);
				}
			});

		}


		return null;
	}
}
