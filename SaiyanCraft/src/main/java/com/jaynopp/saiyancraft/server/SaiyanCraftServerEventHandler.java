package com.jaynopp.saiyancraft.server;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class SaiyanCraftServerEventHandler {
	int ticks = 0;
	long startTime = Long.MIN_VALUE;
	long updateTime, lastTime, lastUpdateTime = 0;
	public float dt;
	int tps;
	int lastTicks = 0;
	
	
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event){
		ServerTick();

		
		
		
		lastUpdateTime = updateTime;
	}
	
	private void ServerTick(){
		if (startTime == Long.MIN_VALUE){
			startTime = System.currentTimeMillis();
			lastTime = startTime;
		}
		updateTime = System.currentTimeMillis() - lastTime;
		
		dt = ((float)(updateTime - lastUpdateTime)) / 1000f;
		
		ticks++; 
		if (updateTime > 1000){
			tps = ticks - lastTicks;
			lastTime = System.currentTimeMillis();
			lastTicks = ticks;
			//System.out.println("Server tps: " + tps + ((tps < 40) ? " -" + (40-tps) : "") + ((tps > 40) ? " +" + (tps-40) : ""));
			//System.out.println(ISaiyanData.carriers.size() + " SaiyanData, " + ISaiyanBattler.carriers.size() + " SaiyanBattlers");
		}
		ISaiyanData.Update(dt);
		ISaiyanBattler.Update(dt);
		
	}
}
