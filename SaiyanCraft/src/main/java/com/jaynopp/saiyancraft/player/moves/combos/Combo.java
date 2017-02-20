package com.jaynopp.saiyancraft.player.moves.combos;

import java.util.ArrayList;
import java.util.List;

import com.jaynopp.saiyancraft.input.KeyBindings;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;

public class Combo {
	public class ComboItem {
		public ISaiyanMove move;
		public KeyBinding input;
		
		public ComboItem(ISaiyanMove move){
			this.move = move;
			input = move.GetType() == ISaiyanMove.Type.LIGHT_MELEE ? KeyBindings.light_attack.binding : KeyBindings.heavy_attack.binding;
		}
	}
	
	public List<ComboItem> moves;
	protected int current;
	
	public Combo(ISaiyanMove move){
		moves = new ArrayList<ComboItem>();
		moves.add(new ComboItem(move));
	}
	public Combo(List<ISaiyanMove> moves){
		this.moves = new ArrayList<ComboItem>();
		for (int i = 0; i < moves.size(); i++){
			this.moves.add(new ComboItem(moves.get(i)));
		}
	}
	public int GetCurrent(){
		return current;
	}
	public ComboItem GetNext(){
		if (HasNext()){
			current++;
		} else {
			current = 0;
		}
		return moves.get(current);
	}
	
	public ComboItem PeekNext(){
		if (HasNext())
			return moves.get(current+1);
		else 
			return null;
		
	}
	
	public boolean HasNext(){
		return (current + 1) < moves.size();
	}
	
	public void Reset(){
		current = 0;
	}
	
	public void ReleaseChargeableMove(SaiyanPlayer user, Entity entityHit){
		if (user.GetBattler().CanAttack()){
			ComboItem ci = moves.get(current);
			ci.move.Use(user, entityHit);
			user.setChargingHeavy(false);

		}
	}
	
	public void ExecuteMove(SaiyanPlayer user, Entity entityHit){
		if (user.GetBattler().CanAttack()){
			ComboItem ci = moves.get(current);
			if (ci.move.IsChargeable()){
				user.setChargingHeavy(true);
			} else {
				ci.move.Use(user, entityHit);
			}
			user.comboManager.nextQueued = false;
		} else {
			user.comboManager.nextQueued = true;
		}
	}
	
	public void Start(SaiyanPlayer user, Entity entityHit) {
		current = 0;
		ExecuteMove(user, entityHit);
	}

}
