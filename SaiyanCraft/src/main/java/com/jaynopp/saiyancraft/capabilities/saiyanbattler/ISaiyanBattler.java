package com.jaynopp.saiyancraft.capabilities.saiyanbattler;

import java.util.List;

import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.entity.Entity;

public interface ISaiyanBattler {
	
	public float GetCooldown();
	public boolean IsOnCooldown();
	public void AddCooldown(float cooldown);
	public void SetCooldown(float cooldown);
	public void Update();
	public float GetStunTimeLeft();
	public boolean IsStunned();
	public void AddStunTime(float stunTime);
	public void SetStunTime(float stunTime);
	public boolean CanAttack();
	public void UpdateFrom(ISaiyanBattler other);
	public List<ISaiyanMove> GetMoves();
	

	public static ISaiyanBattler Get(Entity entity){
		if (entity.hasCapability(SaiyanBattlerProvider.BATTLER_CAP, null))
			return entity.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
		
		return null;
	}
}
