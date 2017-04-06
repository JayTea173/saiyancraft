package com.jaynopp.saiyancraft.init;

import java.util.ArrayList;
import java.util.List;

import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.player.BaseLearnable;
import com.jaynopp.saiyancraft.player.ILearnable;
import com.jaynopp.saiyancraft.player.moves.BaseMeleeMove;
import com.jaynopp.saiyancraft.player.moves.BaseToggleableMove;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove.Type;

public class ModLearnables {
	public static List<ILearnable> learnables;
	
	public enum PlayerClass {
		DEFAULT
	}
	
	public static void Init(){
		learnables = new ArrayList<ILearnable>();
		//Register(new BaseLearnable(null, null))
		
		RegisterMoveAsLearnable(new BaseMeleeMove(1.8f, 1.2f, Type.HEAVY_MELEE, 1.7f, 1.7f, 3f, 3f),
				new DefaultSaiyanData(0f, 0f, 0f, 2f, 0f, 0f));
		
		RegisterMoveAsLearnable(new BaseToggleableMove(Type.STATUS), new DefaultSaiyanData(0f, 0f, 0f, 0f, 2f, 0f));
		
	}
	
	private static ILearnable RegisterMoveAsLearnable(ISaiyanMove move, ISaiyanData requirements){
		ModMoves.Register(move);
		ILearnable l = new BaseLearnable(move, requirements);
		learnables.add(l);
		return l;
	}
	
	public static void Register(ILearnable learnable){
		if (learnables.contains(learnable)){
			System.err.println("Duplicate learnable entry!");
			return;
		}
		learnables.add(learnable);
	}
}
