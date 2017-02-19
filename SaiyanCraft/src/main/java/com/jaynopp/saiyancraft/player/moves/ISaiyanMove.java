package com.jaynopp.saiyancraft.player.moves;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;

public interface ISaiyanMove {
	public void Use(ISaiyanBattler user, ISaiyanBattler target);
	public boolean CanBeUsed(ISaiyanBattler user);
}
