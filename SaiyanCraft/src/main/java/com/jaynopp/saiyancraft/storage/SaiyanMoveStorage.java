package com.jaynopp.saiyancraft.storage;

import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SaiyanMoveStorage implements IStorage<ISaiyanMove> {

	public NBTBase writeNBT(Capability<ISaiyanMove> capability, ISaiyanMove instance, EnumFacing side) {
		final NBTTagCompound tag = new NBTTagCompound();
		return null;
	}

	public void readNBT(Capability<ISaiyanMove> capability, ISaiyanMove instance, EnumFacing side, NBTBase nbt) {
		final NBTTagCompound tag = (NBTTagCompound)nbt;
		
	}

}
