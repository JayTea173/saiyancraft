package com.jaynopp.saiyancraft.capabilities.saiyanbattler;

import java.util.ArrayList;
import java.util.List;

import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public interface ISaiyanBattler {
	
	public List<Entity> carriers = new ArrayList<Entity>();
	
	public float GetCooldown();
	public boolean IsOnCooldown();
	public void AddCooldown(float cooldown);
	public void SetCooldown(float cooldown);
	public void Update(Entity entity, float dt);
	public float GetStunTimeLeft();
	public boolean IsStunned();
	public void AddStunTime(float stunTime);
	public void SetStunTime(float stunTime);
	public boolean CanAttack();
	public void UpdateFrom(ISaiyanBattler other);
	public EntityLiving GetOwningEntity();
	public EntityPlayer GetOwningPlayer();
	public void SetOwner(EntityLiving owner);
	public void SetOwner(EntityPlayer owner);
	public List<ISaiyanMove> GetMoves();
	

	public static ISaiyanBattler Get(Entity entity){
		if (entity.hasCapability(SaiyanBattlerProvider.BATTLER_CAP, null))
			return entity.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
		
		return null;
	}
	
	public static void Update(float dt){
		for (int i = 0; i < carriers.size(); i++ ){
			Entity carrier = carriers.get(i);
			if (carrier.hasCapability(SaiyanBattlerProvider.BATTLER_CAP, null)){
				carrier.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null).Update(carrier, dt);
			}
		}
	}
}
