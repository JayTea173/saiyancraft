package com.jaynopp.saiyancraft.player;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class DamageSenderMessage implements IMessage {

	public float amount;
	public int attackerEntityID;
	
	public DamageSenderMessage(){

	}
	
	public DamageSenderMessage(float damage, int attackerEntityID){
		this.amount = damage;
		this.attackerEntityID = attackerEntityID;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		amount = buf.readFloat();
		attackerEntityID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(amount);
		buf.writeInt(attackerEntityID);
	}

}
