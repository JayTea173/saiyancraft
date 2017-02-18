package com.jaynopp.saiyancraft.damagesources;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class SaiyanDamageSource extends EntityDamageSourceIndirect {

	public SaiyanDamageSource(String damageTypeIn, Entity source, Entity indirectEntityIn) {
		super(damageTypeIn, source, indirectEntityIn);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ITextComponent getDeathMessage(EntityLivingBase entity){
		return new TextComponentString(entity.getDisplayName() + " was knocked the f*ck out by " + getSourceOfDamage().getDisplayName() + ".");
	}

}
