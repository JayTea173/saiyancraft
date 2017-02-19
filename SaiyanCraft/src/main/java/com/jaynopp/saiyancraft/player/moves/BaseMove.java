package com.jaynopp.saiyancraft.player.moves;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;

public class BaseMove implements ISaiyanMove {
	protected float cooldownTime, stunTime;
	
	public BaseMove(float cooldownTime, float stunTime){
		this.cooldownTime = cooldownTime;
		this.stunTime = stunTime;
	}
	
	
	public void Use(ISaiyanBattler user, ISaiyanBattler target) {
		user.AddCooldown(cooldownTime);
		target.AddStunTime(stunTime);
	}

	public boolean CanBeUsed(ISaiyanBattler user) {
		return !user.IsOnCooldown() && !user.IsStunned();
	}

}
