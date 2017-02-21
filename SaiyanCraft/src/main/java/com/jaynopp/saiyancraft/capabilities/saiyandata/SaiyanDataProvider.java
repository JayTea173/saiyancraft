package com.jaynopp.saiyancraft.capabilities.saiyandata;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SaiyanDataProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(ISaiyanData.class)
	public static final Capability<ISaiyanData> POWERLEVEL_CAP = null;
	
	private ISaiyanData instance = POWERLEVEL_CAP.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capability == POWERLEVEL_CAP;
	}
	
	@Override 
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return hasCapability(capability, facing) ? POWERLEVEL_CAP.<T>cast(instance) : null;
	}
	
	@Override
	public NBTBase serializeNBT(){
		return (NBTTagCompound) POWERLEVEL_CAP.getStorage().writeNBT(POWERLEVEL_CAP, instance, null);
	}
	
	@Override
	public void deserializeNBT(NBTBase nbt){
		POWERLEVEL_CAP.getStorage().readNBT(POWERLEVEL_CAP, instance, null, nbt);
	}
}
