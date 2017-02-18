package com.jaynopp.saiyancraft.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;

public class ItemValueManager {
	public Map<String, Float> values = new HashMap<String, Float>();
	
	public static final String itemValuesPath = "SaiyanCraft/ItemValues.txt";
	FileInputStream vin = null;
	FileOutputStream vout = null;
	
	public void Initialize() throws IOException{
		File f = new File(itemValuesPath);
		
		if (!f.exists()){
			f.getParentFile().mkdirs();
			f.createNewFile();
			System.out.println("Writing SaiyanCraft Default ItemValues.");
			SetupDefaultItemValues();
			WriteItemValues();
		}
		ReadItemValues();
			
			
	}
	

	private void ReadItemValues() throws IOException {
		try {
		  vin = new FileInputStream(itemValuesPath);
		  Scanner scanner = new Scanner(vin);
		  
		  while (scanner.hasNextLine()){
			  String line = scanner.nextLine();
			  int equalsIndex = line.indexOf('=');
			  values.put(line.substring(0, equalsIndex-1), Float.parseFloat(line.substring(equalsIndex+1, line.length() - 1)));
		  }
		  
		  scanner.close();
		} finally {
			if (vin != null)
				vin.close();
			System.out.println("SaiyanCraft loaded " + values.size() + " ItemValues.");
		}
	}

	public float GetStackValue(ItemStack stack){
		
		return GetValue(stack.getItem()) * (float)stack.getCount();
	}
	
	
	public float GetValue(Item item) {
		if (item == null)
			return 0f;
		
		String key = item.getUnlocalizedName();
		if (values.containsKey(key))
			return values.get(key);
		else
			return GetNewValueFromCraftable(item);
	}
	
	public float GetValue(Block block){
		return GetValue(block.getItemDropped(block.getDefaultState(), new Random(), 0));
	}
	
	public float GetNewValueFromCraftable(Item item){
		System.out.println("Trying to get ItemValue from recipe for " + item.getUnlocalizedName());
		
		float result = 0f;
		CraftingManager cm = CraftingManager.getInstance();
		List<IRecipe> recipes = cm.getRecipeList();
		InventoryCrafting ic = new InventoryCrafting(new Container() //simulate crafting
	    {
	        public boolean canInteractWith(EntityPlayer playerIn)
	        {
	            return false;
	        }
	    }, 3, 3);
		for (IRecipe recipe : recipes){
			ItemStack output = recipe.getRecipeOutput();
			if (output.getItem() == item){
				NonNullList<ItemStack> reqs = recipe.getRemainingItems(ic);
				
				float inner = 0f;
				for (ItemStack stack : reqs){
					if (stack.getItem() == Items.AIR)
						continue;
					System.out.println("inner: " + stack.getItem().getUnlocalizedName());
					inner = GetValue(stack.getItem()) * (float)stack.getCount();

					result += inner;
					
				}
				
			}
		}
		
		return result;
	}

	private void WriteItemValues()  throws IOException {
		try {
			  vout = new FileOutputStream(itemValuesPath);
			  
			  for(Map.Entry<String, Float> entry : values.entrySet()){
					vout.write((entry.getKey() + "=").getBytes());
					vout.write((entry.getValue().toString() + System.lineSeparator()).getBytes());
				}
			} finally {
				if (vin != null)
					vin.close();	
			}
	}
	
	private void SetupDefaultItemValues() {
		values.put(Items.APPLE.getUnlocalizedName(), 2f);
		values.put(Items.BEEF.getUnlocalizedName(), 2f);
		values.put(Items.BLAZE_ROD.getUnlocalizedName(), 32f);
		values.put(Items.BONE.getUnlocalizedName(), 2f);
		values.put(Items.CARROT.getUnlocalizedName(), 2f);
		values.put(Items.CHICKEN.getUnlocalizedName(), 2f);
		values.put(Items.COAL.getUnlocalizedName(), 4f);
		values.put(Items.DIAMOND.getUnlocalizedName(), 512f);
		values.put(Items.DYE.getUnlocalizedName(), 1f);
		values.put(Items.EGG.getUnlocalizedName(), 2f);
		values.put(Items.ELYTRA.getUnlocalizedName(), 1024f);
		values.put(Items.ENDER_PEARL.getUnlocalizedName(), 64f);
		values.put(Items.FEATHER.getUnlocalizedName(), 2f);
		values.put(Items.FISH.getUnlocalizedName(), 2f);
		values.put(Items.FLINT.getUnlocalizedName(), 1f);
		values.put(Items.GLOWSTONE_DUST.getUnlocalizedName(), 8f);
		values.put(Items.GUNPOWDER.getUnlocalizedName(), 4f);
		values.put(Items.NAME_TAG.getUnlocalizedName(), 256f);
		values.put(Items.GOLD_INGOT.getUnlocalizedName(), 64f);
		values.put(Items.IRON_INGOT.getUnlocalizedName(), 16f);
		values.put(Items.LEATHER.getUnlocalizedName(), 4f);
		values.put(Items.MAGMA_CREAM.getUnlocalizedName(), 32f);
		values.put(Items.MELON.getUnlocalizedName(), 1f);
		values.put(Items.MUTTON.getUnlocalizedName(), 2f);
		values.put(Items.NETHER_WART.getUnlocalizedName(), 16f);
		values.put(Items.NETHER_STAR.getUnlocalizedName(), 16384f);
		values.put(Items.NETHERBRICK.getUnlocalizedName(), 1.5f);
		values.put(Items.PORKCHOP.getUnlocalizedName(), 2.1f);
		values.put(Items.QUARTZ.getUnlocalizedName(), 16f);
		values.put(Items.REDSTONE.getUnlocalizedName(), 6f);
		values.put(Items.REEDS.getUnlocalizedName(), 1f);
		values.put(Items.ROTTEN_FLESH.getUnlocalizedName(), .5f);
		values.put(Items.SNOWBALL.getUnlocalizedName(), .1f);
		values.put(Items.SPIDER_EYE.getUnlocalizedName(), 6f);
		values.put(Items.STRING.getUnlocalizedName(), 2f);
		values.put(Items.WHEAT.getUnlocalizedName(), 1.5f);
		values.put(Blocks.STONE.getUnlocalizedName(), 0.3f);
		values.put(Blocks.COBBLESTONE.getUnlocalizedName(), 0.1f);
		values.put(Blocks.LOG.getUnlocalizedName(), 1.2f);
		values.put(Blocks.LOG2.getUnlocalizedName(), 1.2f);
	}

}
