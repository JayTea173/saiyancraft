package com.jaynopp.saiyancraft.input;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
	public static KeyBinding openSaiyanGUI;
	public static KeyBinding block;
	
	public void Init(){
		openSaiyanGUI = new KeyBinding("key.opensaiyangui", Keyboard.KEY_O, "key.categories.SaiyanCraft");
		block = new KeyBinding("key.block", Keyboard.KEY_LCONTROL, "key.categories.SaiyanCraft");
		ClientRegistry.registerKeyBinding(openSaiyanGUI);
	}
	
}
