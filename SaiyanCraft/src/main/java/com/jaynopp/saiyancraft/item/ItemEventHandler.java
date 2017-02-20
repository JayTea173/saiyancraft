package com.jaynopp.saiyancraft.item;

import com.jaynopp.saiyancraft.SaiyanCraft;

import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemEventHandler {
	@SubscribeEvent
	public void OnItemTooltip(ItemTooltipEvent event){
		if (event.getEntityPlayer().isCreative()){
			event.getToolTip().add(String.format("ItemValue: %1$.2f", SaiyanCraft.itemValueManager.GetValue(event.getItemStack().getItem())));
		}
	}
}
