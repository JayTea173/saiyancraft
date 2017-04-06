package com.jaynopp.saiyancraft.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class BlockableDamageMessageHandler  implements IMessageHandler<BlockableDamageMessage, IMessage> {
	@Override
	public IMessage onMessage(BlockableDamageMessage message, MessageContext ctx) {
		SaiyanPlayer player = SaiyanPlayer.local;
		float amount = message.amount;
		
		Entity entity = (message.attackerEntityID == -1) ? null : Minecraft.getMinecraft().world.getEntityByID(message.attackerEntityID);

		if (SaiyanPlayer.local.isBlocking()){
			System.out.println("Received blockable damage! - blocked");
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					player.player.performHurtAnimation();
					player.HandleBlockingDamage(entity, amount);
				}
			});
			
		} else {
			//System.out.println("Received damage!");
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					player.player.performHurtAnimation();
					//player.Damage(Minecraft.getMinecraft().world.getEntityByID(message.attackerEntityID), amount);
				}
			});
			
			return new DamageSenderMessage(message.amount, message.attackerEntityID);
		}
		
		
		
		
		return null;
		
		
	}
}
