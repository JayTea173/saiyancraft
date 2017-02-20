package com.jaynopp.saiyancraft.player.moves;

import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UseMoveMessageHandler implements IMessageHandler<UseMoveMessage, IMessage> {

	@Override
	public IMessage onMessage(UseMoveMessage message, MessageContext ctx) {
		System.out.println("Sync SAIYANDATA!");
		if (ctx.side.isServer()){
			EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
			
			IThreadListener mainThread = (WorldServer) sender.world;
			mainThread.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					//sender.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null).UpdateFrom(message.sd);
					//DefaultSaiyanData.UpdateStats(sender);
					
					BaseMove.UseServer(message.move, SaiyanPlayer.Get(sender), message.target);
					BaseMove.UseCommon(message.move, SaiyanPlayer.Get(sender), message.target);
				}
			});
			
		} else {
			
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					EntityPlayerSP player = Minecraft.getMinecraft().player;
					/*ISaiyanBattler data = player.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
					data.UpdateFrom(message.sd);
					SaiyanPlayer splayer = SaiyanPlayer.Get((EntityPlayer)player);
					if (splayer != null){
						if (splayer.comboManager == null){
							splayer.SetupComboManager();
						} else {
							System.out.println("Already have combomanager for " + player.getName());
						}
					} else
						System.out.println(player.getName() + " is no SaiyanPlayer.");
					//DefaultSaiyanData.UpdateStats(player);*/
				}
			});

		}


		return null;
	}
}
