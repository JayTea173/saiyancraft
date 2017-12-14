package com.jaynopp.saiyancraft.player.moves;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.DefaultSaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SaiyanBattlerProvider;
import com.jaynopp.saiyancraft.damagesources.SaiyanDamageSource;
import com.jaynopp.saiyancraft.init.ModSounds;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import scala.util.Random;

public class BaseMove implements ISaiyanMove {
	protected float cooldownTime, stunTime, knockBack, power, chargeTime, chargePowerModifier;
	protected Type type;

	

	
	public BaseMove(float cooldownTime, float stunTime, Type type, float power, float knockBack){
		this.cooldownTime = cooldownTime;
		this.stunTime = stunTime;
		this.type = type;
		this.power = power;
		this.knockBack = knockBack;
		this.chargeTime = 0f;
		this.chargePowerModifier = 0f;
	}
	
	public BaseMove(float cooldownTime, float stunTime, Type type, float power, float knockBack, float chargeTime, float chargePowerModifier){
		this.cooldownTime = cooldownTime;
		this.stunTime = stunTime;
		this.type = type;
		this.power = power;
		this.knockBack = knockBack;
		this.chargeTime = chargeTime;
		this.chargePowerModifier = chargePowerModifier;
	}
	
	
	public static void UseClient(BaseMove move, SaiyanPlayer user, Entity entityHit){
		ISaiyanBattler battler = user.GetBattler();
		battler.AddCooldown(move.cooldownTime);
		//System.out.println("Adding cooldown +" + move.cooldownTime + " cooldown is now " + battler.GetCooldown());
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
	
	public static void KnockBackEntity(Entity attacker, Entity entityHit, float amount, boolean applyY){
		Vec3d dir = new Vec3d(entityHit.posX, entityHit.posY, entityHit.posZ).subtract(new Vec3d(attacker.posX, attacker.posY, attacker.posZ)).normalize();
		Vec3d knock = dir.scale(amount);
		entityHit.motionX += knock.xCoord;
		if (applyY)
			entityHit.motionY += knock.yCoord;
		entityHit.motionZ += knock.zCoord;
		entityHit.velocityChanged = true;
	}
	
	public void KnockBack(SaiyanPlayer user, Entity entityHit, float statBonus){
		KnockBackEntity(user.player, entityHit, knockBack * statBonus, true);
	}
	
	@Override
	public String toString(){
		return this.type.toString() + " move";
		
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
			float chargeMul = 1f + ((move.IsChargeable()) ? (user.attackCharge * move.chargePowerModifier) : 1f);
			float damage = (1f + (float)Math.pow(curr-1d, 0.925d)) * move.GetPower() * chargeMul * .5f;
			float bonus = (float) (1d / Math.pow(curr, .25d) * .0025d) * move.GetPower() * chargeMul;
			System.out.println("Player is attacking! Strength increased by " + bonus + ", damage dealt: " + (damage / 2f) + " hearts. (power: " + move.power + ")");
			Random rand = new Random();
			entityHit.playSound(ModSounds.PUNCH_LIGHT, 1f, .9f + rand.nextFloat() * .2f);
			user.GetStats().SetStrength(curr + bonus);
			entityHit.attackEntityFrom(new SaiyanDamageSource("saiyandamage.entity", user.player, null), damage);
			if (entityHit instanceof EntityLivingBase){
				EntityLivingBase livingEntity = ((EntityLivingBase)entityHit);
				
				DefaultSaiyanBattler.AddStun(livingEntity);
				
				if (move.IsChargeable())
					move.KnockBack(user, entityHit, (.25f + (chargeMul / move.chargePowerModifier) * .75f) * (float) Math.pow(damage, .3f));
				else
					move.KnockBack(user, entityHit, (float) Math.pow(damage, .3f));
				
			}
			entityHit.hurtResistantTime = 0;
			
		}

	}
	
	public static void UseCommon(BaseMove move, SaiyanPlayer user, Entity entityHit){
		//if (entityHit != null)
		//	System.out.println("we hit " + entityHit.getName());
		//else
		//	System.out.println("we hit nothing.");	
		
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
		return GetChargeTime() > 0f;
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
		BaseMove move = new BaseMove(buf.readFloat(), buf.readFloat(), ISaiyanMove.Type.values()[buf.readInt()], buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat());
		return move;
	}
	
	public static void WriteFromBytes(ByteBuf buf, ISaiyanMove move){
		buf.writeFloat(move.GetCooldown());
		buf.writeFloat(move.GetStunTime());
		buf.writeInt(move.GetType().ordinal());
		buf.writeFloat(move.GetPower());
		buf.writeFloat(move.GetKnockback());
		buf.writeFloat(move.GetChargeTime());
		buf.writeFloat(move.GetChargePowerModifier());
		System.out.println("Writing move kb: " + move.GetKnockback());
	}

	public float GetChargePowerModifier() {
		return chargePowerModifier;
	}




}
