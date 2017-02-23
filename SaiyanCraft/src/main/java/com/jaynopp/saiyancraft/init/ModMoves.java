package com.jaynopp.saiyancraft.init;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.player.moves.BaseMove;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove.Type;

public class ModMoves {
	public static void CreateDefaultMoves(ISaiyanBattler battler){
		battler.GetMoves().add(new BaseMove(0.32f, 0.42f, Type.LIGHT_MELEE, 1.1f, 0.25f));
		battler.GetMoves().add(new BaseMove(0.36f, 0.44f, Type.LIGHT_MELEE, 1.0f, 0.28f));
		battler.GetMoves().add(new BaseMove(0.27f, 0.43f, Type.LIGHT_MELEE, 0.8f, 0.22f));
		battler.GetMoves().add(new BaseMove(1.35f, 0.52f, Type.LIGHT_MELEE, 1.8f, 0.46f, 1f, 2f));
		battler.GetMoves().add(new BaseMove(1.8f, 0.65f, Type.HEAVY_MELEE, 2f, 0.78f, 1.5f, 2f));
		
	}
}
