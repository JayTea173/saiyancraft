package com.jaynopp.saiyancraft.storage;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.Capability.*;

public class SaiyanBattlerStorage implements IStorage<ISaiyanBattler> {
	@Override
	public NBTBase writeNBT(Capability<ISaiyanBattler> capability, ISaiyanBattler instance, EnumFacing side){
		final NBTTagCompound nbt = new NBTTagCompound();
		return nbt;
	}

	@Override
	public void readNBT(Capability<ISaiyanBattler> capability, ISaiyanBattler instance, EnumFacing side, NBTBase nbt){
		final NBTTagCompound tag = (NBTTagCompound)nbt;

		
	}
}
