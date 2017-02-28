package com.jaynopp.saiyancraft.player;

public interface ILearnable {
	public boolean IsLearnableBy(SaiyanPlayer learner);
	public void Learn(SaiyanPlayer learner);
	public int GetMaxLevel();
	public int GetCurrentLevel();
	public float GetProgress();
	public void SetProgress(float progress);
	public void AdvanceLevel();
}
