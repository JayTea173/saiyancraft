package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class SaiyanPlayerCommonEventHandler {
	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event){

		if ( event.getEntityPlayer() != null){
			HandleAttackingPlayer(event);
		} else if (event.getTarget() instanceof EntityPlayer){
			HandleAttackedPlayer(event);
		}

	}
	
	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event){
		if (event.getEntity() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntity();
			DefaultSaiyanData data = DefaultSaiyanData.Get(player);
			float curr = data.GetAgility();
			float bonus = (float) (Math.pow(curr, -.25d) * .00085d);
			data.SetAgility(curr + bonus);
			DefaultSaiyanData.UpdateSpeed(DefaultSaiyanData.Get(player), player);
		}
	}
	
	@SubscribeEvent
	public void onEntityFall(LivingFallEvent event){
		if (event.getEntity() instanceof EntityPlayer){
			event.setDamageMultiplier(0f);
		}
	}
	
	@SubscribeEvent
	public void onPlayerCraft(ItemCraftedEvent event){
		DefaultSaiyanData.Get(event.player);
		System.out.println("value of crafted: " + SaiyanCraft.itemValueManager.GetValue(event.crafting.getItem()));
	}
	
	@SubscribeEvent
	public void onEntityAttack(LivingAttackEvent event){
		if (event.getEntity() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntity();
			DefaultSaiyanData data = DefaultSaiyanData.Get(player);
			SaiyanPlayer splayer = SaiyanPlayer.Get(player);
			
			DamageSource source = event.getSource();
			Entity sourceEntity = source.getEntity();
			if (event.getSource() == DamageSource.FALL || event.getSource() == DamageSource.DROWN || event.getSource() == DamageSource.IN_FIRE || event.getSource() == DamageSource.LAVA || event.getSource() == DamageSource.OUT_OF_WORLD || event.getSource() == DamageSource.STARVE || event.getSource() == DamageSource.WITHER || event.getSource() == DamageSource.HOT_FLOOR){
			
			} else {
				SaiyanCraft.network.sendTo(new BlockableDamageMessage(event.getAmount(), (sourceEntity == null) ? -1 : sourceEntity.getEntityId()), (EntityPlayerMP) player);
				//SaiyanCraft.network.sendToAll(new BlockableDamageMessage(event.getAmount(), event.getEntity().getEntityId()));
				//System.out.println("ATTACKEVENT: Sending damage message to " + player.getDisplayNameString());
				event.setCanceled(true);
			}
			
			
			/*
			if (splayer.isBlocking()){
				splayer.HandleBlockingEvent(event);
				System.out.println("Player is blocking.");
			} else {
				System.out.println("Player is NOT blocking.");
				
			}*/
		}
	}
	
	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event){
		if (event.getPlayer() == null)
			return;
		DefaultSaiyanData data = DefaultSaiyanData.Get(event.getPlayer());
		float curr = data.GetStrength();
		float blockDiff = event.getWorld().getBlockState(event.getPos()).getPlayerRelativeBlockHardness(event.getPlayer(), event.getWorld(), event.getPos());
		if (blockDiff == Float.NaN || blockDiff == Float.POSITIVE_INFINITY || blockDiff == Float.NEGATIVE_INFINITY)
			blockDiff = 0f;
		float bonus = (float) (1d / Math.pow(curr, .25d) * .01d * blockDiff);
		System.out.println("Player mined a block! Strength increased by " + bonus);
		data.SetStrength(curr + bonus);
		DefaultSaiyanData.UpdateStats(event.getPlayer());
		SaiyanCraft.network.sendToServer(new SyncSaiyanDataMessage(data));
		//event.setCanceled(true);
		
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event){
		if (event.getEntity() instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.getEntity();
			DefaultSaiyanData data = DefaultSaiyanData.Get(player);
			//SaiyanCraft.network.sendTo(new BlockableDamageMessage(event.getAmount(), event.getEntity().getEntityId()), (EntityPlayerMP) player);
			//SaiyanCraft.network.sendToAll(new BlockableDamageMessage(event.getAmount(), event.getEntity().getEntityId()));
			//System.out.println("HURTEVENT: Sending damage message to " + player.getDisplayNameString());
			float curr = data.GetVitality();
			float bonus = 1f;
			if (!(Float.isInfinite(bonus) && curr == 0f))
				bonus = (float) (Math.pow(event.getAmount() / curr, 1.4d) * .0025d);
			System.out.println("Player was hurt! Vitality increased by " + bonus);
			data.SetVitality(curr + bonus);
		}
	}
	
	
	private void HandleAttackingPlayer(AttackEntityEvent event){

	}
	
	private void HandleAttackedPlayer(AttackEntityEvent event){
		EntityPlayer player = (EntityPlayer)event.getTarget();
		event.getTarget();
		DefaultSaiyanData.Get(player);
		
		
	}
}
