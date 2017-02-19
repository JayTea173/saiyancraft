package com.jaynopp.saiyancraft.init;

import com.jaynopp.saiyancraft.SaiyanCraft;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds {
	public static SoundEvent PUNCH_LIGHT;
	
	
	public static void RegisterSounds(){
		PUNCH_LIGHT = Register("player.attack.punch.light");
	}
	
	private static SoundEvent Register(String name){
		ResourceLocation location = new ResourceLocation(SaiyanCraft.modId, name);
		return GameRegistry.register(new SoundEvent(location).setRegistryName(location));
	}

}
