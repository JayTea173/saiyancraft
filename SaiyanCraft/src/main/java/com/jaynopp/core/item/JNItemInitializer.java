package com.jaynopp.core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public interface JNItemInitializer {
	public static List<Item> initializedItems = new ArrayList<Item>();
	
	public default void Initialize(Item item, String modResourcePrefix, String itemName) {
		String resourcePath = modResourcePrefix + itemName;
		item.setRegistryName(new ResourceLocation(resourcePath));
		item.setUnlocalizedName(resourcePath); 
		GameRegistry.register(item);
		initializedItems.add(item);
	}
	
	public static void Register(){
		System.err.println("Trying to register a JNItem without having an overriden Register method!");
	}
}
