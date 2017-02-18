package com.jaynopp.saiyancraft.capabilities.saiyandata;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncSaiyanDataToClientMessage  implements IMessage {
	public ISaiyanData sd;
		
	public SyncSaiyanDataToClientMessage(){
		
	}
	
	public SyncSaiyanDataToClientMessage(ISaiyanData sd){
		this.sd = sd;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		sd = new DefaultSaiyanData();
		sd.SetPowerLevel(buf.readDouble());

		sd.SetVitality(buf.readFloat());
		sd.SetEndurance(buf.readFloat());
		sd.SetSkill(buf.readFloat());
		sd.SetStrength(buf.readFloat());
		sd.SetAgility(buf.readFloat());
		sd.SetSpirit(buf.readFloat());

	}

	@Override
	public void toBytes(ByteBuf buf) {

		buf.writeDouble(sd.GetPowerLevel());
		
		buf.writeFloat(sd.GetVitality());
		buf.writeFloat(sd.GetEndurance());
		buf.writeFloat(sd.GetSkill());
		buf.writeFloat(sd.GetStrength());
		buf.writeFloat(sd.GetAgility());
		buf.writeFloat(sd.GetSpirit());
	}
}
