package com.jaynopp.saiyancraft.player;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientPlayerPerfectBlockMessageHandler  implements IMessageHandler<ClientPlayerPerfectBlockMessage, IMessage> {
	@Override
	public IMessage onMessage(ClientPlayerPerfectBlockMessage message, MessageContext ctx) {
		EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
		SaiyanPlayer player = SaiyanPlayer.Get(sender);
		Entity attacker = sender.world.getEntityByID(message.attackerEntityID);
		Entity attackingSource = sender.world.getEntityByID(message.sourceEntityID);
		IThreadListener mainThread = (WorldServer) sender.world;
		mainThread.addScheduledTask(new Runnable(){
			@Override
			public void run(){
				
				if (attackingSource instanceof EntityArrow){
					float pi180 = 180.0F * (float)Math.PI;
					float faceX = -MathHelper.sin(player.player.cameraYaw / pi180) * MathHelper.cos(player.player.rotationPitch * pi180);
					float faceY = -MathHelper.sin(player.player.cameraPitch * pi180);
					float faceZ = MathHelper.cos(player.player.cameraYaw * pi180) * MathHelper.cos(player.player.cameraPitch * pi180);
					
					if (player.player.world.isRemote)
						Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(attackingSource, EnumParticleTypes.CRIT);
					
					Vec3d dir = new Vec3d(faceX, faceY, faceZ).normalize();
					Vec3d vel = dir.scale(400f);
					attackingSource.setVelocity(vel.xCoord, vel.yCoord, vel.zCoord);
				} 
				

			}
		});
		return null;
		
		
		
	}
}
