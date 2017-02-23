package com.jaynopp.saiyancraft.capabilities.saiyanbattler;

import java.util.ArrayList;
import java.util.List;

import com.jaynopp.saiyancraft.player.SaiyanPlayer;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

public class DefaultSaiyanBattler implements ISaiyanBattler {
	protected float cooldown;
	protected float stunTime;
	protected List<ISaiyanMove> moves;
	protected EntityLiving ownerEntity;
	protected EntityPlayer owner;
	
	public static final AttributeModifier MODIFIER_STUN = new AttributeModifier("Stun", -100000d, 0);
	
	public void UpdateFrom(ISaiyanBattler other) {
		cooldown = other.GetCooldown();
		stunTime = other.GetStunTimeLeft();
		moves = other.GetMoves();
		System.out.println("Copied other Saiyanbattler, we now have " + moves.size() + " moves.");
	}

	public float GetCooldown() {
		return cooldown;
	}

	public boolean IsOnCooldown() {
		return cooldown > 0f;
	}
	
	public float GetStunTimeLeft() {
		return stunTime;
	}

	public boolean IsStunned() {
		return stunTime > 0f;
	}

	public void Update(Entity entity, float dt) {
		if (entity instanceof EntityPlayer){
			EntityPlayer playerEntity = (EntityPlayer)entity;
			if (playerEntity.isDead){
				DefaultSaiyanBattler.carriers.remove(entity);
				return;
			}
			SaiyanPlayer player = SaiyanPlayer.Get(playerEntity);
			if (player != null){
				if (cooldown > -.65f){
					cooldown -= dt;
				}else if (cooldown > -100f){
					cooldown = -1000f;	
					if (player.comboManager != null){
						if ((player.comboManager.currentCombo != null && !player.isChargingHeavy())){
							player.comboManager.AbortCurrentCombo();	
						}
					}
				}
			} else
				System.out.println("Unable to find SaiyanPlayer: " + entity.getName());
		}

		if (stunTime > 0f && dt > 0f){
			stunTime -= dt;
		}
		else if (stunTime < 0f){
			if (entity instanceof EntityLivingBase)
				DefaultSaiyanBattler.RemoveStun((EntityLivingBase)entity);
			stunTime = 0f;	
		}
	}
	
	private static void RemoveStun(EntityLivingBase entity) {
		IAttributeInstance speedAttrib = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		IAttributeInstance damageAttrib = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

		if (speedAttrib != null)
			if (speedAttrib.hasModifier(MODIFIER_STUN))
				speedAttrib.removeModifier(MODIFIER_STUN);
		if (damageAttrib != null)
			if (damageAttrib.hasModifier(MODIFIER_STUN))
				damageAttrib.removeModifier(MODIFIER_STUN);
	}

	public void AddCooldown(float cooldown) {
		if (this.cooldown < 0f)
			this.cooldown = 0f;
		this.cooldown += cooldown;
	}

	public void AddStunTime(float stunTime) {
		this.stunTime += stunTime;
	}

	public void SetCooldown(float cooldown) {
		this.cooldown = cooldown;
	}

	public void SetStunTime(float stunTime) {
		this.stunTime = stunTime;
	}

	
	public boolean CanAttack() {
		return !IsStunned() && !IsOnCooldown();
	}

	public List<ISaiyanMove> GetMoves() {
		if (moves == null)
			moves = new ArrayList<ISaiyanMove>();
		return moves;
	}
	
	public static DefaultSaiyanBattler Get(EntityLiving entity){
		if (!entity.hasCapability(SaiyanBattlerProvider.BATTLER_CAP, null))
			return null;
		return (DefaultSaiyanBattler)entity.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
	}

	public static DefaultSaiyanBattler Get(EntityPlayer entity){
		if (!entity.hasCapability(SaiyanBattlerProvider.BATTLER_CAP, null))
			return null;
		return (DefaultSaiyanBattler)entity.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
	}

	public EntityLiving GetOwningEntity() {
		return ownerEntity;
	}
	public void SetOwner(EntityLiving owner) {
		ownerEntity = owner;
	}
	public EntityPlayer GetOwningPlayer() {
		return owner;
	}
	public void SetOwner(EntityPlayer owner) {
		System.out.println("Setting owner of cap to " + owner.getName());
		this.owner = owner;
	}

	public static void AddStun(EntityLivingBase entity) {
		IAttributeInstance speedAttrib = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		IAttributeInstance damageAttrib = entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

		if (speedAttrib != null)
			if (!speedAttrib.hasModifier(MODIFIER_STUN))
				speedAttrib.applyModifier(MODIFIER_STUN);
		if (damageAttrib != null)
			if (!damageAttrib.hasModifier(MODIFIER_STUN))
				damageAttrib.applyModifier(MODIFIER_STUN);
		
	}
	
	
}
