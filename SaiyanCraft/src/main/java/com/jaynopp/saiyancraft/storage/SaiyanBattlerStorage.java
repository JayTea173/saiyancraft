package com.jaynopp.saiyancraft.storage;

import java.util.List;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.player.moves.BaseMove;
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
		for (int i = 0; i < moves.size(); i++){
			WriteMove(moves.get(i), nbt, i);
		}
		
		return nbt;
	}
	
	public void WriteMove(ISaiyanMove move, NBTTagCompound nbt, int index){
		String prefix = "move" + index + "_";
		nbt.setFloat(prefix + "cooldown", move.GetCooldown());
		nbt.setFloat(prefix + "stuntime", move.GetStunTime());
		nbt.setInteger(prefix + "type", move.GetType().ordinal());
		nbt.setFloat(prefix + "power", move.GetPower());
		nbt.setFloat(prefix + "knockback", move.GetKnockback());
		nbt.setFloat(prefix + "chargetime", move.GetChargeTime());
		nbt.setFloat(prefix + "chargepowermodifier", move.GetChargePowerModifier());
	}

	@Override
	public void readNBT(Capability<ISaiyanBattler> capability, ISaiyanBattler instance, EnumFacing side, NBTBase nbt){
		final NBTTagCompound tag = (NBTTagCompound)nbt;
		instance.SetCooldown(tag.getFloat("cooldown"));
		instance.SetStunTime(tag.getFloat("stuntime"));
		int numMoves = tag.getInteger("num_moves");
		List<ISaiyanMove> moves = instance.GetMoves();
		for (int i = 0; i < numMoves; i++){
			moves.add(ReadMove(tag, i));
		}
		
	}
	
	public BaseMove ReadMove(NBTTagCompound nbt, int index){
		String prefix = "move" + index + "_";
		return new BaseMove(nbt.getFloat(prefix + "cooldown"), nbt.getFloat(prefix + "stuntime"), ISaiyanMove.Type.values()[nbt.getInteger(prefix + "type")],
				nbt.getFloat(prefix + "power"), nbt.getFloat(prefix + "knockback"), nbt.getFloat(prefix + "chargetime"), nbt.getFloat(prefix + "chargepowermodifier"));

	}
}
