package com.jaynopp.saiyancraft.capabilities.saiyandata;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.DefaultSaiyanBattler;

import net.minecraft.entity.Entity;

public interface ISaiyanData {
	
	public List<Entity> carriers = new ArrayList<Entity>();
	
	public static String PLToString(double pl, boolean decimal){
		if (!decimal)
			return new DecimalFormat("#").format(pl);
		if (pl < 10d)
			return new DecimalFormat("#.##").format(pl);
		else if (pl < 100d)
			return new DecimalFormat("#.#").format(pl);
		return new DecimalFormat("#").format(pl);
	}
	
	public void UpdateFrom(ISaiyanData other);
	
	public void SetPowerLevel(double value);
	public double GetPowerLevel();
	public void SetVitality(float value);
	public float GetVitality();
	public void SetEndurance(float value);
	public float GetEndurance();
	public void SetSkill(float value);
	public float GetSkill();
	public void SetStrength(float value);
	public float GetStrength();
	public void SetAgility(float value);
	public float GetAgility();
	public void SetSpirit(float value);
	public float GetSpirit();
	
	public float GetKi();
	public void SetKi(float value);
	public float GetMaxKi();
	public float GetStamina();
	public void SetStamina(float value);
	public float GetMaxStamina();
	
	public static ISaiyanData Get(Entity entity){
		if (entity.hasCapability(SaiyanDataProvider.POWERLEVEL_CAP, null))
			return entity.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		
		return null;
	}
	
	public static void Update(float dt){
		for (int i = 0; i < carriers.size(); i++ ){
			Entity carrier = carriers.get(i);
			
			if (carrier.isDead)
				DefaultSaiyanBattler.carriers.remove(carrier);
		}
		/*for (Entity carrier : carriers){
			if (carrier.hasCapability(SaiyanDataProvider, facing))
		}*/
	}
	
	
	public default boolean IsAllGreaterOrEqual(ISaiyanData data){
		return (GetPowerLevel() >= data.GetPowerLevel() && GetVitality() >= data.GetVitality() && GetEndurance() >= data.GetEndurance() && 
				GetSkill() >= data.GetSkill() && GetStrength() >= data.GetStrength() && GetAgility() >= data.GetAgility() && GetSpirit() >= data.GetSpirit());
	}
}
