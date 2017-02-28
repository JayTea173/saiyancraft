package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

public class BaseLearnable implements ILearnable {
	protected ISaiyanMove learnedMove;
	protected ISaiyanData requirements;
	protected float progress;
	
	public BaseLearnable (ISaiyanMove learnedMove, ISaiyanData requirements){
		this.learnedMove = learnedMove;
		this.requirements = requirements;
		progress = 0f;
	}
	
	
	public boolean IsLearnableBy(SaiyanPlayer learner) {
		return learner.GetStats().IsAllGreaterOrEqual(requirements);
	}

	public void Learn(SaiyanPlayer learner) {
		if (!learner.comboManager.moves.contains(learnedMove))
			learner.comboManager.moves.add(learnedMove);
	}

	public int GetMaxLevel() {
		return 0;
	}

	public int GetCurrentLevel() {
		return 0;
	}

	public float GetProgress() {
		return progress;
	}

	public void SetProgress(float progress) {
		if (GetCurrentLevel() < GetMaxLevel())
			this.progress += progress;
	}

	public void AdvanceLevel() {
	}


}
