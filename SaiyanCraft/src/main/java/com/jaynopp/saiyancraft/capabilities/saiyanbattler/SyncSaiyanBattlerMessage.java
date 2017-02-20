package com.jaynopp.saiyancraft.capabilities.saiyanbattler;

import java.util.List;

import com.jaynopp.saiyancraft.player.moves.BaseMove;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SyncSaiyanBattlerMessage implements IMessage {

	public ISaiyanBattler sd;
	
	public SyncSaiyanBattlerMessage(){
		
	}
	
	public SyncSaiyanBattlerMessage(ISaiyanBattler sd){
		this.sd = sd;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		sd = new DefaultSaiyanBattler();
		sd.SetCooldown(buf.readFloat());
		sd.SetStunTime(buf.readFloat());
		List<ISaiyanMove> moves = sd.GetMoves();
		int moves_count = buf.readInt();

		for (int i = 0; i < moves_count; i++){
			moves.add(BaseMove.ReadFromBytes(buf));
		}
	}
	

	

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(sd.GetCooldown());
		buf.writeFloat(sd.GetStunTimeLeft());
		List<ISaiyanMove> moves = sd.GetMoves();
		int moves_count = moves.size();
		buf.writeInt(moves_count);

		for (int i = 0; i < moves_count; i++){
			BaseMove.WriteFromBytes(buf, moves.get(i));
		}
	}

}
