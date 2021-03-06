package com.jaynopp.saiyancraft.player;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClientPlayerPerfectBlockMessage implements IMessage {

	public int attackerEntityID;
	public int sourceEntityID;
	
	public ClientPlayerPerfectBlockMessage(){
		
	}
	
	public ClientPlayerPerfectBlockMessage(int attackerEntityID, int sourceEntityID){
		this.attackerEntityID = attackerEntityID;
		this.sourceEntityID = sourceEntityID;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		attackerEntityID = buf.readInt();
		sourceEntityID = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(attackerEntityID);
		buf.writeInt(sourceEntityID);
	}

}
