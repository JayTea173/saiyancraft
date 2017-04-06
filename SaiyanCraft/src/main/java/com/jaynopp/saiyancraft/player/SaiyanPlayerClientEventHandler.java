package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.SaiyanCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderHandEvent;
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
        	if (event.getEntity().world.isRemote){
        		if (!Minecraft.getMinecraft().player.isDead){
	        		System.out.println("Player joined world.");
	        		
	        		SaiyanPlayer.Initialize((EntityPlayer)event.getEntity(), SaiyanPlayer.local);
        		}
        	}
        }
    }
    
    @SubscribeEvent
    public void onRenderHand(RenderHandEvent event){
    	if (SaiyanPlayer.local != null){
    		if (SaiyanPlayer.isPlayerEntityUsingFists()){
    			//event.setCanceled(true);
    			//SaiyanPlayer.local.RenderHands();
    		}
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
			if (SaiyanPlayer.local.UseStamina(4f))
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
