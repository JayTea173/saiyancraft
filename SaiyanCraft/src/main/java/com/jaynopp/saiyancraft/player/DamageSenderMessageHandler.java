package com.jaynopp.saiyancraft.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DamageSenderMessageHandler  implements IMessageHandler<DamageSenderMessage, IMessage> {
	@Override
	public IMessage onMessage(DamageSenderMessage message, MessageContext ctx) {
		EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
		SaiyanPlayer player = SaiyanPlayer.Get(sender);
		Entity attacker = sender.world.getEntityByID(message.attackerEntityID);
		float amount = message.amount;
		IThreadListener mainThread = (WorldServer) sender.world;
		mainThread.addScheduledTask(new Runnable(){
			@Override
			public void run(){
				//System.out.println("dealing " + amount + " damage to " + sender.getDisplayNameString());
			
				player.Damage(sender, attacker, amount);
			}
		});
		return null;
	}
}
