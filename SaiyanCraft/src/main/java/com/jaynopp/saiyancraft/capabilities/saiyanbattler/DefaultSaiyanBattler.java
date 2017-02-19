package com.jaynopp.saiyancraft.capabilities.saiyanbattler;

import java.util.ArrayList;
import java.util.List;

import com.jaynopp.saiyancraft.player.SaiyanPlayer;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class DefaultSaiyanBattler implements ISaiyanBattler {
	protected float cooldown;
	protected float stunTime;
	protected List<ISaiyanMove> moves;
	
	
	public void UpdateFrom(ISaiyanBattler other) {
		cooldown = other.GetCooldown();
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

	public void Update() {
		if (cooldown > 0f)
			cooldown -= 1f / SaiyanPlayer.DT;
		else if (cooldown < 0f)
			cooldown = 0f;	
		
		if (stunTime > 0f)
			stunTime -= 1f / SaiyanPlayer.DT;
		else if (stunTime < 0f)
			stunTime = 0f;	
	}

	public void AddCooldown(float cooldown) {
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
	
}
