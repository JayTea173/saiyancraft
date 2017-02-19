package com.jaynopp.saiyancraft.storage;

import java.util.ArrayList;
import java.util.List;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SaiyanBattlerStorage implements IStorage<ISaiyanBattler> {
	@Override
	public NBTBase writeNBT(Capability<ISaiyanBattler> capability, ISaiyanBattler instance, EnumFacing side){
		final NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat("cooldown",instance.GetCooldown());
		nbt.setFloat("stuntime",instance.GetStunTimeLeft());
		List<ISaiyanMove> moves = instance.GetMoves();
		nbt.setInteger("num_moves", moves.size());
		return nbt;
	}

	@Override
	public void readNBT(Capability<ISaiyanBattler> capability, ISaiyanBattler instance, EnumFacing side, NBTBase nbt){
		final NBTTagCompound tag = (NBTTagCompound)nbt;
		instance.SetCooldown(tag.getFloat("cooldown"));
		instance.SetStunTime(tag.getFloat("stuntime"));
		int numMoves = tag.getInteger("num_moves");
		
		
	}
}
