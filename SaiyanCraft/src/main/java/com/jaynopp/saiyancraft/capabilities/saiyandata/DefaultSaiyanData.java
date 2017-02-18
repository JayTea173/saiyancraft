package com.jaynopp.saiyancraft.capabilities.saiyandata;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

public class DefaultSaiyanData implements ISaiyanData {
	private double powerLevel = 1d;
	private float vitality = 1f;
	private float endurance = 1f;
	private float skill = 1f;
	private float strength = 1f;
	private float agility = 1f;
	private float spirit = 1f;
	
	public float speedBonus;
	public float healthBonus;
	private float staminaMax = 100f;
	private float stamina = 100f;
	private float kiMax = 100f;
	private float ki = 100f;
	
	public static void UpdateStats(EntityPlayer player){
		DefaultSaiyanData cap = (DefaultSaiyanData) player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		if (cap == null)
			return;
		double pl = cap.GetPowerLevel();
		float vit = cap.GetVitality();
		
		cap.healthBonus = (float)((vit - 1d) / Math.pow(1d / pl, .1f));
		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20d + cap.healthBonus);	
		UpdateSpeed(cap, player);
		UpdateKi(cap);
		UpdateStamina(cap);
	}
	
	public static void UpdateKi(DefaultSaiyanData cap){
		//DefaultSaiyanData cap = (DefaultSaiyanData) player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		if (cap == null)
			return;
		double pl = cap.GetPowerLevel();
		float spi = cap.GetSpirit();
		cap.kiMax = (float)(100d + Math.pow(spi - 1d, 1.2d) / Math.pow(1d / pl, .25f));
	}
	
	public static void UpdateStamina(DefaultSaiyanData cap){
		//DefaultSaiyanData cap = (DefaultSaiyanData) player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		if (cap == null)
			return;
		double pl = cap.GetPowerLevel();
		float end = cap.GetEndurance();
		cap.staminaMax = (float)(100d + Math.pow(end - 1d, .75d) * 10d);
	}
	
	public static void UpdateSpeed(DefaultSaiyanData cap, EntityPlayer player){
		//DefaultSaiyanData cap = (DefaultSaiyanData) player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		if (cap == null)
			return;
		double pl = cap.GetPowerLevel();
		float agi = cap.GetAgility();
		cap.speedBonus = (float)(Math.pow(agi - 1d, .75d) / Math.pow(1d / pl, .075f) * .1d);

	}
	
	public static DefaultSaiyanData Get(EntityPlayer player){
		return (DefaultSaiyanData)player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
	}
	
	public double GetPowerLevel(){
		double statsum = vitality + endurance + skill + strength + agility + spirit;
		powerLevel = (statsum-6d) / 3d;
		powerLevel = Math.pow(powerLevel, Math.pow(statsum, .125d)) * 3d + 3d;
		return powerLevel;
	}
	
	public void SetPowerLevel(double value){
		powerLevel = value;
	}
	
	public void SetVitality(float value){
		vitality = value;
	}
	public float GetVitality(){
		return vitality;
	}
	public void SetEndurance(float value){
		endurance = value;
	}
	public float GetEndurance(){
		return endurance;
	}
	public void SetSkill(float value){
		skill = value;
	}
	public float GetSkill(){
		return skill;
	}
	public void SetStrength(float value){
		strength = value;
	}
	public float GetStrength(){
		return strength;
	}
	public void SetAgility(float value){
		agility = value;
		/*Minecraft.getMinecraft().player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(20d + 
				(vitality - 1d) / Math.pow(1d / GetPowerLevel(), .1f));*/
	}
	public float GetAgility(){
		return agility;
	}
	public void SetSpirit(float value){
		spirit = value;
	}
	public float GetSpirit(){
		return spirit;
	}

	public void UpdateFrom(ISaiyanData other) {
		this.powerLevel = other.GetPowerLevel();
		this.vitality = other.GetVitality();
		this.endurance = other.GetEndurance();
		this.skill = other.GetSkill();
		this.strength = other.GetStrength();
		this.agility = other.GetAgility();
		this.spirit = other.GetSpirit();
	}

	public float GetKi() {
		return ki;
	}

	public float GetMaxKi() {
		return kiMax;
	}

	public float GetStamina() {
		return stamina;
	}

	public float GetMaxStamina() {
		return staminaMax;
	}

	public void SetKi(float value) {
		ki = value;
		
	}

	public void SetStamina(float value) {
		stamina = value;
		
	}
}
