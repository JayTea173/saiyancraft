package com.jaynopp.saiyancraft.player.moves.combos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.DefaultSaiyanBattler;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class ComboManager {
	public List<ISaiyanMove> moves;
	public Map<KeyBinding, Combo> combos;
	public Combo currentCombo;
	public boolean nextQueued;
	public KeyBinding queuedKey;
	
	public ComboManager(EntityLiving entity){
		moves = DefaultSaiyanBattler.Get(entity).GetMoves();
		SetupCombos();
	}
	
	public ComboManager(EntityPlayer entity){
		moves = DefaultSaiyanBattler.Get(entity).GetMoves();
		System.out.println("ComboManager initalized for " + entity.getName() + ", containing " + moves.size() + " moves.");
		SetupCombos();
	}
	
	private void SetupCombos(){
		combos = new HashMap<KeyBinding, Combo>();
		if (moves.size() > 0){
			List<ISaiyanMove> comboMoves = new ArrayList<ISaiyanMove>();
			for (int i = 0; i < moves.size(); i++){
				ISaiyanMove move = moves.get(i);
				if (move.GetType() == ISaiyanMove.Type.LIGHT_MELEE)
					comboMoves.add(move);
			}
			Combo combo = new Combo(comboMoves);
			AddCombo(combo);
			
			comboMoves.clear();
			for (int i = 0; i < moves.size(); i++){
				ISaiyanMove move = moves.get(i);
				if (move.GetType() == ISaiyanMove.Type.HEAVY_MELEE)
					comboMoves.add(move);
			}
			combo = new Combo(comboMoves);
			AddCombo(combo);
		} else {
			System.out.println("UNABLE TO SETUP COMBOS, CAUSE NO MOVES WERE REGISTERED");
		}
	}
	
	public void AddCombo(Combo combo){
		System.out.println("New combo (" + combo.moves.size() + " moves), starting with " + combo.moves.get(0).input.getDisplayName());
		combos.put(combo.moves.get(0).input, combo);
	}
	
	private void StartCombo(KeyBinding input, SaiyanPlayer user, Entity entityHit){
		currentCombo = combos.get(input);
		if (currentCombo == null)
			return;
		
		if (currentCombo.moves.get(currentCombo.current).move.IsChargeable()){
			System.out.println("The move you're using is chargeable");
		}
		currentCombo.Start(input, user, entityHit);
		System.out.println("Start combo (" + currentCombo.moves.size() + ")");
	}
	
	public void ContinueCombo(KeyBinding input, SaiyanPlayer user, Entity entityHit){
			if (currentCombo == null)
				return;
			if (user.GetBattler().CanAttack())
				currentCombo.GetNext();
			currentCombo.ExecuteMove(input, user, entityHit);
			if (!currentCombo.HasNext() && !currentCombo.moves.get(currentCombo.current).move.IsChargeable())
				AbortCurrentCombo();
			
	}
	
	public void AbortCurrentCombo(){
		currentCombo.Reset();
		currentCombo = null;
		System.out.println("ABORT COMBO!!");
	}
	
	public void Act(KeyBinding input, SaiyanPlayer user, Entity entityHit){
		if (user.isChargingHeavy())
			return; //abort input if charging attack
		if (currentCombo == null){
			if (user.GetBattler().CanAttack())
				StartCombo(input, user, entityHit);
		} else if (currentCombo.HasNext()) {
			if (currentCombo.PeekNext().input == input)
				ContinueCombo(input, user, entityHit);
		} else {
			System.out.println("Combo finished!");
			AbortCurrentCombo();
			StartCombo(input, user, entityHit);
		}
	}
	

}
