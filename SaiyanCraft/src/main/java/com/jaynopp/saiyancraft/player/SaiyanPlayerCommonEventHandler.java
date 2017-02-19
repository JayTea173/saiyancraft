package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;
import com.jaynopp.saiyancraft.damagesources.SaiyanDamageSource;
import com.jaynopp.saiyancraft.init.ModSounds;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import scala.util.Random;

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
			float curr = data.GetVitality();
			float bonus = (float) (Math.pow(event.getAmount() / curr, 1.4d) * .0025d);
			System.out.println("Player was hurt! Vitality increased by " + bonus);
			data.SetVitality(curr + bonus);
			DefaultSaiyanData.UpdateStats(player);
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
		
	}
	
	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event){
		if (event.getEntity() instanceof EntityPlayer){
			DefaultSaiyanData data = DefaultSaiyanData.Get((EntityPlayer)event.getEntity());
			float curr = data.GetVitality();
			float bonus = (float) (Math.pow(event.getAmount() / curr, 1.4d) * .0025d);
			System.out.println("Player was hurt! Vitality increased by " + bonus);
			data.SetVitality(curr + bonus);
		}
	}
	
	
	private void HandleAttackingPlayer(AttackEntityEvent event){
		EntityPlayer player = event.getEntityPlayer();
		Entity target = event.getTarget();
		if (SaiyanPlayer.isPlayerEntityUsingFists(player)){
			player.swingProgressInt = 0;
            player.isSwingInProgress = false;
            player.swingProgress = 0f;
            System.out.println("FIST EM DADDY");
            event.setCanceled(true);
			DefaultSaiyanData data = DefaultSaiyanData.Get(player);
			float curr = data.GetStrength();
			float damage = 1f + (float)Math.pow(curr-1d, 0.925d);
			event.setCanceled(true);
			
			if (target instanceof EntityLivingBase){
				EntityLivingBase targetLiving = (EntityLivingBase) target;
				if (targetLiving.hurtResistantTime <= 0){
					float bonus = (float) (1d / Math.pow(curr, .25d) * .0025d);
					System.out.println("Player is attacking! Strength increased by " + bonus);
					Random rand = new Random();
					targetLiving.playSound(ModSounds.PUNCH_LIGHT, 1f, .9f + rand.nextFloat() * .2f);
					data.SetStrength(curr + bonus);
					event.getTarget().attackEntityFrom(new SaiyanDamageSource("saiyandamage.entity", event.getEntityPlayer(), null), damage);
					targetLiving.hurtResistantTime = ((EntityLivingBase) target).maxHurtResistantTime / 16;
				}
			}
		} else {
			System.out.println("Player attacked with an item!");
		}
	}
	
	private void HandleAttackedPlayer(AttackEntityEvent event){
		EntityPlayer player = (EntityPlayer)event.getTarget();
		event.getTarget();
		DefaultSaiyanData.Get(player);
		
		
	}
}
