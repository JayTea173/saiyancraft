package com.jaynopp.saiyancraft.player.moves;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SaiyanBattlerProvider;
import com.jaynopp.saiyancraft.damagesources.SaiyanDamageSource;
import com.jaynopp.saiyancraft.init.ModSounds;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import scala.util.Random;

public class BaseMove implements ISaiyanMove {
	protected float cooldownTime, stunTime, chargeTime, knockBack, power;
	protected Type type;
	protected boolean isChargeable;
	
	public static final AttributeModifier MODIFIER_STUN = new AttributeModifier("Stun", 0d, 0);
	
	public BaseMove(float cooldownTime, float stunTime, Type type, float power, float knockBack, boolean chargeable){
		this.cooldownTime = cooldownTime;
		this.stunTime = stunTime;
		this.type = type;
		this.power = power;
		this.knockBack = knockBack;
		this.isChargeable = chargeable;
	}
	
	
	public static void UseClient(BaseMove move, SaiyanPlayer user, Entity entityHit){
		ISaiyanBattler battler = user.GetBattler();
		battler.AddCooldown(move.cooldownTime);
		System.out.println("Adding cooldown +" + move.cooldownTime + " cooldown is now " + battler.GetCooldown());
		user.player.swingArm(EnumHand.MAIN_HAND);
		if (entityHit != null) {
			Random rand = new Random();
			entityHit.playSound(ModSounds.PUNCH_LIGHT, 1f, .9f + rand.nextFloat() * .2f);
			float curr = user.GetStats().GetStrength();
			float bonus = (float) (1d / Math.pow(curr, .25d) * .0025d);
			System.out.println("Player is attacking! Strength increased by " + bonus);
	
			user.GetStats().SetStrength(curr + bonus);
		}
		
	}
	
	public void KnockBack(SaiyanPlayer user, Entity entityHit){
		EntityPlayer player = user.player;
		Vec3d dir = new Vec3d(player.posX, player.posY, player.posZ).subtract(new Vec3d(entityHit.posX, entityHit.posY, entityHit.posZ)).normalize();
		Vec3d knock = dir.scale(knockBack);
		entityHit.addVelocity(knock.xCoord, knock.yCoord, knock.zCoord);
		entityHit.velocityChanged = true;
	}
	
	public static void UseServer(BaseMove move, SaiyanPlayer user, Entity entityHit){
		user.player.swingArm(EnumHand.MAIN_HAND);
		
		ISaiyanBattler target = null;
		if (entityHit != null)
			target = entityHit.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);

		if (target != null)
			target.SetStunTime(move.stunTime);

		//DAMAGE
		if (entityHit != null) {
			float curr = user.GetStats().GetStrength();
			float damage = 1f + (float)Math.pow(curr-1d, 0.925d);
			float bonus = (float) (1d / Math.pow(curr, .25d) * .0025d) * move.GetPower();
			System.out.println("Player is attacking! Strength increased by " + bonus + ", damage dealt: " + (damage / 2f) + " hearts.");
			Random rand = new Random();
			entityHit.playSound(ModSounds.PUNCH_LIGHT, 1f, .9f + rand.nextFloat() * .2f);
			user.GetStats().SetStrength(curr + bonus);
			entityHit.attackEntityFrom(new SaiyanDamageSource("saiyandamage.entity", user.player, null), damage);
			if (entityHit instanceof EntityLivingBase){
				EntityLivingBase livingEntity = ((EntityLivingBase)entityHit);
				move.KnockBack(user, entityHit);
				//livingEntity.knockBack(user.player, move.knockBack, 1d, 1d);
				IAttributeInstance speedAttrib = livingEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
				IAttributeInstance damageAttrib = livingEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

				System.out.println("Applying stun modifiers...");
				if (speedAttrib != null)
					if (!speedAttrib.hasModifier(MODIFIER_STUN))
						speedAttrib.applyModifier(MODIFIER_STUN);
				if (damageAttrib != null)
					if (!damageAttrib.hasModifier(MODIFIER_STUN))
						damageAttrib.applyModifier(MODIFIER_STUN);
				
				
				
			}
			entityHit.hurtResistantTime = 0;
			
		}

	}
	
	private static void RemoveStun(Entity entityHit, ISaiyanBattler entityBattler, float stunTime, IAttributeInstance speedAttrib, IAttributeInstance damageAttrib){
		if (entityBattler.GetStunTimeLeft() <= 0f){
			if (speedAttrib != null)
				speedAttrib.removeModifier(MODIFIER_STUN);
			if (damageAttrib != null)
				damageAttrib.removeModifier(MODIFIER_STUN);
			System.out.println("STUN REMOVED!");
		}
	}
	
	public static void UseCommon(BaseMove move, SaiyanPlayer user, Entity entityHit){
		if (entityHit != null)
			System.out.println("we hit " + entityHit.getName());
		else
			System.out.println("we hit nothing.");	
		
	}
	
	public void Use(SaiyanPlayer user, Entity entityHit) {
		if (entityHit != null)
			SaiyanCraft.network.sendToServer(new UseMoveMessage(this, entityHit, user.player.world.provider.getDimension()));
		BaseMove.UseClient(this, user, entityHit);
		BaseMove.UseCommon(this, user, entityHit);
		
		/*DefaultSaiyanData data = DefaultSaiyanData.Get(player);
		float curr = data.GetStrength();
		float damage = 1f + (float)Math.pow(curr-1d, 0.925d);
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
		}*/

	}

	public boolean CanBeUsed(ISaiyanBattler user) {
		return !user.IsOnCooldown() && !user.IsStunned();
	}


	public float GetCooldown() {
		return cooldownTime;
	}


	public float GetStunTime() {
		return stunTime;
	}


	public Type GetType() {
		return type;
	}
	
	public boolean IsChargeable(){
		return isChargeable;
	}
	
	public float GetChargeTime() {
		
		return chargeTime;
	}

	public float GetPower() {
		return power;
	}

	public float GetKnockback() {
		return knockBack;
	}
	
	public static BaseMove ReadFromBytes(ByteBuf buf){
		BaseMove move = new BaseMove(buf.readFloat(), buf.readFloat(), ISaiyanMove.Type.values()[buf.readInt()], buf.readFloat(), buf.readFloat(), buf.readBoolean());
		return move;
	}
	
	public static void WriteFromBytes(ByteBuf buf, ISaiyanMove move){
		buf.writeFloat(move.GetCooldown());
		buf.writeFloat(move.GetStunTime());
		buf.writeInt(move.GetType().ordinal());
		buf.writeFloat(move.GetPower());
		buf.writeFloat(move.GetKnockback());
		buf.writeBoolean(move.IsChargeable());
	}




}
