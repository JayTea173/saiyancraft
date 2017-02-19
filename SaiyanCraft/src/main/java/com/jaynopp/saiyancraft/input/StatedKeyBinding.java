package com.jaynopp.saiyancraft.input;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.settings.KeyBinding;

public class StatedKeyBinding {
	public static List<StatedKeyBinding> registered = new ArrayList<StatedKeyBinding>();
	public KeyBinding binding;
	public KeyEvents eventHandler;
	public boolean pressed;
	
	public StatedKeyBinding(String desc, int keyCode, String cat, KeyEvents eventHandler){
		binding = new KeyBinding(desc, keyCode, cat);
		this.eventHandler = eventHandler;
		registered.add(this);
		
	}

	public interface KeyEvents {
		public void OnDown();
		public void OnUp();
		public void OnPressed();
	}
}
