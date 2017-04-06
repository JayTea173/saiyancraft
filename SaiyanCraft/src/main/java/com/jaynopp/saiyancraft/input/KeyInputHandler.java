package com.jaynopp.saiyancraft.input;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.DefaultSaiyanBattler;
import com.jaynopp.saiyancraft.gui.SaiyanPlayerStatusGui;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyInputHandler {
	
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(KeyBindings.openSaiyanGUI.isPressed()){
        	Minecraft.getMinecraft().displayGuiScreen(new SaiyanPlayerStatusGui());
        	System.out.println("You have " + DefaultSaiyanBattler.Get(Minecraft.getMinecraft().player).GetMoves().size() + " moves");
        }
        

        
        	
        
    }
	
	@SubscribeEvent
	public void onMouseInput (InputEvent.MouseInputEvent event){
		for (StatedKeyBinding i : StatedKeyBinding.registered){
			boolean pressed = i.binding.isKeyDown();
			if (pressed && !i.pressed)
				i.eventHandler.OnDown();
			
			if (!pressed && i.pressed)
				i.eventHandler.OnUp();
			
			i.pressed = pressed;
			if (i.pressed)
				i.eventHandler.OnPressed();
			
		}
	}
	
}
