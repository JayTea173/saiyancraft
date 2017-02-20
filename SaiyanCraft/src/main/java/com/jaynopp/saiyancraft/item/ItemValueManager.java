package com.jaynopp.saiyancraft.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import scala.actors.threadpool.Arrays;

public class ItemValueManager {
	public Map<String, Float> values = new HashMap<String, Float>();
	
	public static final String itemValuesPath = "SaiyanCraft/ItemValues.txt";
	FileInputStream vin = null;
	FileOutputStream vout = null;
	
	private List<Item> currentItemSearchStack;
	
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
	
	public void BakeValueList(){
		System.out.println("Baking ItemValues.");
		RegistryNamespaced<ResourceLocation, Item> items = GetAllItems();
		//Item i = items.getObject((ResourceLocation) items.getKeys().toArray()[10]);

		for (ResourceLocation location : items.getKeys()){
			currentItemSearchStack = new ArrayList<Item>();
			Item i = items.getObject(location);
			if (!values.containsKey(i.getUnlocalizedName())){
				System.out.println(i.getUnlocalizedName() + "=" + GetValue(i));
				/*try {
					System.in.read();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
			}
		}
	}
	
	public RegistryNamespaced<ResourceLocation, Item> GetAllItems(){
		return Item.REGISTRY;
	}
	

	private void ReadItemValues() throws IOException {
		try {
		  vin = new FileInputStream(itemValuesPath);
		  Scanner scanner = new Scanner(vin);
		  
		  while (scanner.hasNextLine()){
			  String line = scanner.nextLine();
			  int equalsIndex = line.indexOf('=');
			  values.put(line.substring(0, equalsIndex), Float.parseFloat(line.substring(equalsIndex+1, line.length())));
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
	
	
	@SuppressWarnings("unused")
	public float GetValue(Item item) {
		String key = item.getUnlocalizedName();
		boolean containsKey = values.containsKey(key);
		if(currentItemSearchStack == null)
			currentItemSearchStack = new ArrayList<Item>();
		
		if (currentItemSearchStack.contains(item) && !containsKey){
			//System.out.println("CIRCULAR SEARCH: " + key);
			return 0f; //circular search.
		}
		
		currentItemSearchStack.add(item);
		if (item == null)
			return 0f;
		
		
		if (containsKey) {
			return values.get(key);
		} else
			return GetNewValueFromCraftable(item);
	}
	
	public float GetValue(Block block){
		return GetValue(block.getItemDropped(block.getDefaultState(), new Random(), 0));
	}
	
	@SuppressWarnings("unchecked")
	public float GetNewValueFromCraftable(Item item){
		
		
		List<Float> results = new ArrayList<Float>();
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
		//System.out.println("Trying to get ItemValue from recipe for " + item.getUnlocalizedName());
		for (IRecipe recipe : recipes){
			int amountOut = recipe.getRecipeOutput().getCount();
			
			if (recipe.getRecipeOutput().getItem() == item){
				if (recipe instanceof ShapedOreRecipe){
					ShapedOreRecipe _recipe = (ShapedOreRecipe)recipe;
					result += CalcRecipeInputListsValue(_recipe.getInput());
					
				} else if (recipe instanceof ShapelessOreRecipe){
					ShapelessOreRecipe _recipe = (ShapelessOreRecipe)recipe;
					result += CalcRecipeInputListsValue(_recipe.getInput().toArray());
				} else if (recipe instanceof ShapedRecipes) {
					ShapedRecipes _recipe = (ShapedRecipes)recipe;
					List<Float> recipeItems = Arrays.asList(GetItemStacksValues(_recipe.recipeItems));
					Float lowest = (Float)GetLowestValueInList(recipeItems);
					//System.out.println("Lowest of all the possible recipeItems: " + lowest + " in recipe: " + _recipe.recipeItems);
					result = lowest;	
				} else
					System.out.println("The ItemValueManager doesn't know how to handle recipe type " + recipe.getClass());
				
				results.add(result / (float)amountOut);
				//System.out.println("Added result for " + item.getUnlocalizedName() + ", count now " + results.size());
				result = 0f;
				//List<IRecipe> recipes = cm.getRecipeList();
			}
		}
		Float lowestResult = GetLowestValueInList(results);
		return lowestResult == null ? 0f : lowestResult;
	}
	
	public Float[] GetItemStacksValues(ItemStack[] stacks){
		Float[] result = new Float[stacks.length];
		for (int i = 0; i < stacks.length; i++){
			//System.out.println("getting stacksvalue for stack item(" + i + "): " + stacks[i].getItem().getUnlocalizedName() + ", current: " + currentItemSearchStack.get(currentItemSearchStack.size() - 1).getUnlocalizedName());
			result[i] = GetValue(stacks[i].getItem());
		}
		return result;
	}
	
	public <T extends Comparable<T>> T GetLowestValueInList(List<T> input){
	    
		if (input.isEmpty()) {	
	        return null;
	    } else {
	    	@SuppressWarnings("unused")
			int minIndex = 0;
			final ListIterator<T> itr = input.listIterator();
			T min = itr.next(); // first element as the current minimum
	        minIndex = itr.previousIndex();
	        while (itr.hasNext()) {
	            final T curr = itr.next();
	            if (curr.compareTo(min) < 0) {
	                min = curr;
	                minIndex = itr.previousIndex();
	            }
	        }
	        return min;
	    } 
	}

	public float CalcRecipeInputListsValue(Object[] input){
		float result = 0f;
		float inner = 0f;
		for (Object in : input){
			if (in instanceof NonNullList){
				@SuppressWarnings("unchecked")
				NonNullList<ItemStack> inList = (NonNullList<ItemStack>)in;
				for (ItemStack stack : inList){
					if (stack.getItem() == Items.AIR)
						continue;
					inner = GetValue(stack.getItem());
					//System.out.println("recipe +" + inner + ", " + stack.getItem().getUnlocalizedName());
					result += inner;
				}
					
			} else if (in instanceof ItemStack){
				result = GetValue(((ItemStack)in).getItem());
			} else {
				if (in != null)
					System.out.println("The ItemValueManager doesn't know how to handle recipe input of type " + in.getClass());
				else
					System.out.println("Null was passed as recipe input!");
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
