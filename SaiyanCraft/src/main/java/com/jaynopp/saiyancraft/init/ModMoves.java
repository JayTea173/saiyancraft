package com.jaynopp.saiyancraft.init;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.player.moves.BaseMove;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove.Type;

public class ModMoves {
	public static void CreateDefaultMoves(ISaiyanBattler battler){
		battler.GetMoves().add(new BaseMove(0.32f, 0.42f, Type.LIGHT_MELEE, 1.1f, 0.1f, false));
		battler.GetMoves().add(new BaseMove(0.42f, 0.52f, Type.LIGHT_MELEE, 1.0f, 0.1f, false));
		battler.GetMoves().add(new BaseMove(0.18f, 0.28f, Type.LIGHT_MELEE, 0.8f, 0.05f, false));
		battler.GetMoves().add(new BaseMove(0.9f, 0.6f, Type.LIGHT_MELEE, 1.8f, 0.6f, true));
		battler.GetMoves().add(new BaseMove(3f, 1f, Type.HEAVY_MELEE, 2f, 1f, true));
		
	}
}
