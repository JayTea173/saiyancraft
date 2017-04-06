package com.jaynopp.saiyancraft.init;

import java.util.ArrayList;
import java.util.List;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.player.moves.BaseMeleeMove;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove.Type;

public class ModMoves {
	public static List<ISaiyanMove> moves;
	public static List<ISaiyanMove> defaultMoves;
	

	
	public static void Init(){
		moves = new ArrayList<ISaiyanMove>();
		defaultMoves = new ArrayList<ISaiyanMove>();
		RegisterDefault(new BaseMeleeMove(0.32f, 0.42f, Type.LIGHT_MELEE, 1.1f, 0.25f));
		RegisterDefault(new BaseMeleeMove(0.36f, 0.44f, Type.LIGHT_MELEE, 1.0f, 0.28f));
		RegisterDefault(new BaseMeleeMove(0.27f, 0.43f, Type.LIGHT_MELEE, 0.8f, 0.22f));
		RegisterDefault(new BaseMeleeMove(1.35f, 0.52f, Type.LIGHT_MELEE, 1.8f, 0.46f, 1f, 2f));
		RegisterDefault(new BaseMeleeMove(1.8f, 0.65f, Type.HEAVY_MELEE, 2f, 0.78f, 1.5f, 2f));
		//battler.GetMoves().add(new BaseMove(1.8f, 0.65f, Type.HEAVY_MELEE, 2f, 0.78f, 1.5f, 2f));
		
		
	}
	
	public static int Register(ISaiyanMove move){
		moves.add(move);
		return moves.size()-1;
	}
	
	public static int RegisterDefault(ISaiyanMove move){
		defaultMoves.add(move);
		return Register(move);
	}
	
	public static ISaiyanMove GetByID(int id){
		return moves.get(id);
	}
	
	public static void CreateDefaultMoves(ISaiyanBattler battler){
		battler.GetMoves().addAll(defaultMoves);
	}
}
