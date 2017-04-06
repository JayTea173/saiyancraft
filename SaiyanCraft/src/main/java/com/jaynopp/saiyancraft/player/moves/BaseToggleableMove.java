package com.jaynopp.saiyancraft.player.moves;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import net.minecraft.entity.Entity;

public class BaseToggleableMove implements ISaiyanMove {

	protected Type type;
	protected boolean active;
	
	
	public BaseToggleableMove(Type type){
		this.type = type;
		active = false;
	}
	
	@Override
	public void Use(SaiyanPlayer user, Entity entityHit) {
		active = !active;
	}
	
	public boolean IsActive(){
		return active;
	}

	@Override
	public boolean CanBeUsed(ISaiyanBattler user) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public float GetCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float GetStunTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Type GetType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public boolean IsChargeable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float GetChargePowerModifier() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float GetChargeTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float GetPower() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float GetKnockback() {
		// TODO Auto-generated method stub
		return 0;
	}

}
