package com.jaynopp.saiyancraft.input;

import com.jaynopp.saiyancraft.gui.SaiyanPlayerStatusGui;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
	
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(KeyBindings.openSaiyanGUI.isPressed())
        	Minecraft.getMinecraft().displayGuiScreen(new SaiyanPlayerStatusGui());
        if (KeyBindings.block.isPressed()){
        	SaiyanPlayer.local.Block(true);
        }
        if (!KeyBindings.block.isKeyDown())
        	SaiyanPlayer.local.Block(false);
    }
	
}
