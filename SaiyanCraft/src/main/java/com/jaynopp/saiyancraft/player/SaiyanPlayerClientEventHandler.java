package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.SaiyanCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class SaiyanPlayerClientEventHandler {
	@SubscribeEvent
    public void onClonePlayer(PlayerEvent.Clone event) {
		System.out.println("PLAYER CLONE");
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof EntityPlayer) {
        	if (event.getEntity().world.isRemote)
        		SaiyanPlayer.Initialize((EntityPlayer)event.getEntity());
        }
    }
	
	
	@SubscribeEvent
	public void onPlayerTick (PlayerTickEvent event){ //only on client
		if (event.player == Minecraft.getMinecraft().player){
			SaiyanPlayer.local.Update();
		}
		//event.player.moveEntityWithHeading(0f, forward);
	}
	
	@SubscribeEvent
	public void onPlayerJump (LivingJumpEvent event){ //only on client
		if (event.getEntity() == Minecraft.getMinecraft().player){
			if (SaiyanPlayer.local.UseStamina(2f))
				SaiyanPlayer.local.OnJump();
			

		}
	}
	
	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event){
		if (event.getEntity() == Minecraft.getMinecraft().player){
			EntityPlayer player = (EntityPlayer)event.getEntity();
			Vec3d speed = new Vec3d(player.motionX, player.motionY, player.motionZ);
			double speedMag = speed.lengthVector();
			SaiyanCraft.network.sendToServer(new ClientPlayerFallMessage((float)speedMag));
		} 
	}
	
	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event){

	}
}
