package com.jaynopp.saiyancraft.player.moves;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class UseMoveMessage implements IMessage {

	public BaseMove move;
	public Entity target;
	public int worldID;
	
	public UseMoveMessage(){
		
	}
	
	public UseMoveMessage(ISaiyanMove move, Entity target, int worldID){
		this.move = (BaseMove) move;
		this.target = target;
		this.worldID = worldID;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		worldID = buf.readInt();
		move = BaseMove.ReadFromBytes(buf);
		target = DimensionManager.getWorld(worldID).getEntityByID(buf.readInt());
		System.out.println("translated entity id to " + target.getName());

	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(worldID);
		BaseMove.WriteFromBytes(buf, move);
		buf.writeInt(target.getEntityId());
	}

}
