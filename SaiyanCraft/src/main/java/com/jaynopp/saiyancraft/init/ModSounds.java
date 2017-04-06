package com.jaynopp.saiyancraft.init;

import com.jaynopp.saiyancraft.SaiyanCraft;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModSounds {
	public static SoundEvent PUNCH_LIGHT;
	public static SoundEvent PUNCH_LIGHT_MISS;
	public static SoundEvent PUNCH_HEAVY;
	
	public static void RegisterSounds(){
		PUNCH_LIGHT = Register("player.attack.punch.light");
		PUNCH_LIGHT_MISS = Register("player.attack.punch.light.miss");
		PUNCH_HEAVY = Register("player.attack.punch.heavy");
	}
	
	private static SoundEvent Register(String name){
		ResourceLocation location = new ResourceLocation(SaiyanCraft.modId, name);
		return GameRegistry.register(new SoundEvent(location).setRegistryName(location));
	}

}
