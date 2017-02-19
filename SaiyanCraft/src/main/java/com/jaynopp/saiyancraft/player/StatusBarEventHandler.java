package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.player.gui.SaiyanHud;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class StatusBarEventHandler {

	@SubscribeEvent
	public void onRenderOverlay(RenderGameOverlayEvent event){
		ElementType etype = event.getType();
		switch (etype){
		case HEALTH:
			SaiyanHud.inst.DrawHealth(event);
			SaiyanHud.inst.DrawCharge(event);
			event.setCanceled(true);
			break;
		case ARMOR:
			event.setCanceled(true);
			break;
		case FOOD:
			//event.setCanceled(true);
			break;
		case EXPERIENCE:
			//event.setCanceled(true);
			break;
		default:
			break;
			
		}
			
	}
}

