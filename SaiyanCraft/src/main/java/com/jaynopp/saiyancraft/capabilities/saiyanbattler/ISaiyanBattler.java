package com.jaynopp.saiyancraft.capabilities.saiyanbattler;

import java.text.DecimalFormat;

import net.minecraft.entity.Entity;

public interface ISaiyanBattler {
	
	public void UpdateFrom(ISaiyanBattler other);
	

	public static ISaiyanBattler Get(Entity entity){
		/*if (entity.hasCapability(SaiyanDataProvider.POWERLEVEL_CAP, null))
			return entity.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		*/
		return null;
	}
}
