package com.jaynopp.saiyancraft.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class KnockBackLocalMessageHandler  implements IMessageHandler<KnockBackLocalMessage, IMessage> {
	@Override
	public IMessage onMessage(KnockBackLocalMessage message, MessageContext ctx) {
		SaiyanPlayer player = SaiyanPlayer.local;
		float amount = message.amount;
		
		Entity attacker = (message.attackerEntityID == -1) ? null : Minecraft.getMinecraft().world.getEntityByID(message.attackerEntityID);

		
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable(){
				@Override
				public void run(){
					
					player.movement.KnockAirborne(1f);
					Vec3d dir = new Vec3d(player.player.posX, player.player.posY, player.player.posZ).subtract(new Vec3d(attacker.posX, attacker.posY, attacker.posZ)).normalize();
					Vec3d knock = dir.scale(5f);
					player.player.motionX += knock.xCoord;
					player.player.motionY += knock.yCoord;
					player.player.motionZ += knock.zCoord;
					player.player.velocityChanged = true;
					//System.out.println("KNOCK ME BACK! - " + knock.xCoord + ", " + knock.yCoord + ", " + knock.zCoord);
				}
			});
		
		return null;
		
		
	}
}
