package com.jaynopp.saiyancraft.crafting;

import net.minecraft.inventory.IInventory;

public class RecipeDifficultyRater {
	public static float RateCraftMatrix(IInventory craftMatrix){
		for (int i = 0; i < craftMatrix.getSizeInventory(); i++){
			int itemID = craftMatrix.getField(i);
			System.out.println("Rating item: " + itemID);
			
		}
		
		return 0f;
	}
	
}
