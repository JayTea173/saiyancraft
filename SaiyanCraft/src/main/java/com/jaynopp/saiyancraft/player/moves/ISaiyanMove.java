package com.jaynopp.saiyancraft.player.moves;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import net.minecraft.entity.Entity;

public interface ISaiyanMove {
	public enum Type {
		LIGHT_MELEE,
		HEAVY_MELEE,
		STATUS,
		TRANSFORMATION,
		KI_BLAST,
		KI_BEAM,
		MOVEMENT,
		EVASION,
		DEFENSIVE
	}
	
	public void Use(SaiyanPlayer user, Entity entityHit);
	public boolean CanBeUsed(ISaiyanBattler user);
	
	public float GetCooldown();
	public float GetStunTime();
	public Type GetType();
	public boolean IsChargeable();
	public float GetChargePowerModifier();
	public float GetChargeTime();
	public float GetPower();
	public float GetKnockback();
	
	
}
