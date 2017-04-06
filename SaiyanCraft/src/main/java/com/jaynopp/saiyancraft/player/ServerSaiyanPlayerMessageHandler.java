package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerSaiyanPlayerMessageHandler  implements IMessageHandler<ClientPlayerFallMessage, IMessage> {
	@Override
	public IMessage onMessage(ClientPlayerFallMessage message, MessageContext ctx) {
		EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
		DefaultSaiyanData data = DefaultSaiyanData.Get(sender);
		IThreadListener mainThread = (WorldServer) sender.world;
		mainThread.addScheduledTask(new Runnable(){
			@Override
			public void run(){
				
				double fallNegation =  Math.pow((data.GetPowerLevel() - 3d) * 0.001d, .25d);
				System.out.println("Player fell, speed on impact: " + message.speed + " negation: " + fallNegation + " pl: " + data.GetPowerLevel());
				if (message.speed > .8d + fallNegation){
					//event.setDistance(8f);
					float dmg = (float) (message.speed - .8d + fallNegation) * 10f;
					sender.attackEntityFrom(DamageSource.FALL, dmg);
					System.out.println("Damage: " + dmg);	
					SaiyanCraft.network.sendTo(new SyncSaiyanDataMessage(data), sender);
				}
				

			}
		});
		return null;
		
		
		
	}
}
