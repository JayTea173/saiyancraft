package com.jaynopp.saiyancraft.storage;

import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SayanDataStorage implements IStorage<ISaiyanData> {
	@Override
	public NBTBase writeNBT(Capability<ISaiyanData> capability, ISaiyanData instance, EnumFacing side){
		final NBTTagCompound nbt = new NBTTagCompound();
		nbt.setDouble("powerlevel", instance.GetPowerLevel());
		nbt.setFloat("stamina", instance.GetStamina());
		nbt.setFloat("ki", instance.GetKi());
		
		nbt.setFloat("vitality", instance.GetVitality());
		nbt.setFloat("endurance", instance.GetEndurance());
		nbt.setFloat("skill", instance.GetSkill());
		nbt.setFloat("strength", instance.GetStrength());
		nbt.setFloat("agility", instance.GetAgility());
		nbt.setFloat("spirit", instance.GetSpirit());
		return nbt;
	}

	@Override
	public void readNBT(Capability<ISaiyanData> capability, ISaiyanData instance, EnumFacing side, NBTBase nbt){
		final NBTTagCompound tag = (NBTTagCompound)nbt;
		instance.SetPowerLevel(tag.getDouble("powerlevel"));
		instance.SetStamina(tag.getFloat("stamina"));
		instance.SetKi(tag.getFloat("ki"));
		instance.SetVitality(tag.getFloat("vitality"));
		instance.SetEndurance(tag.getFloat("endurance"));
		instance.SetSkill(tag.getFloat("skill"));
		instance.SetStrength(tag.getFloat("strength"));
		instance.SetAgility(tag.getFloat("agility"));
		instance.SetSpirit(tag.getFloat("spirit"));
		
	}
}
