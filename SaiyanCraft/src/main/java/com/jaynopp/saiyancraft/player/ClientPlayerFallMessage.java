package com.jaynopp.saiyancraft.player;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClientPlayerFallMessage implements IMessage {

	public float speed;
	
	public ClientPlayerFallMessage(){
		
	}
	
	public ClientPlayerFallMessage(float speed){
		this.speed = speed;;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		speed = buf.readFloat();

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(speed);
	}

}
