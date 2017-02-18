package com.jaynopp.saiyancraft.capabilities.saiyanbattler;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SaiyanBattlerProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(ISaiyanBattler.class)
	public static final Capability<ISaiyanBattler> BATTLER_CAP = null;
	
	private ISaiyanBattler instance = BATTLER_CAP.getDefaultInstance();
			
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capability == BATTLER_CAP;
	}
	
	@Override 
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return hasCapability(capability, facing) ? BATTLER_CAP.<T>cast(instance) : null;
	}
	
	@Override
	public NBTBase serializeNBT(){
		return (NBTTagCompound) BATTLER_CAP.getStorage().writeNBT(BATTLER_CAP, instance, null);
	}
	
	@Override
	public void deserializeNBT(NBTBase nbt){
		BATTLER_CAP.getStorage().readNBT(BATTLER_CAP, instance, null, nbt);
	}
}
