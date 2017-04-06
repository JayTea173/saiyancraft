package com.jaynopp.saiyancraft.item.tool;

import java.util.List;

import com.jaynopp.core.item.JNItemInitializer;
import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.lib.Names;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SaiyanPickaxe extends ItemPickaxe implements JNItemInitializer {
	
	public static SaiyanPickaxe instance;
	private boolean dirty = true;
	
	public static void Initialize(){
		instance = new SaiyanPickaxe(ToolMaterial.WOOD);
		
		GameRegistry.addSmelting(Items.IRON_PICKAXE, new ItemStack(instance), 4);
	}
	
	protected SaiyanPickaxe(ToolMaterial material) {
		super(material);
		JNItemInitializer.super.Initialize(this, SaiyanCraft.resourcePrefix, Names.Tools.SAYAN_PICKAXE);
		setCreativeTab(CreativeTabs.TOOLS);
		this.canRepair = true;
		
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean p4){
		if (!dirty){
			String formattedPowerlevelStone = ISaiyanData.PLToString(GetNBTFieldDouble(stack, "powerLevel", 0d) * 4d, GuiScreen.isShiftKeyDown());
			list.add("+Powerlevel (Rocks): " + formattedPowerlevelStone);
			list.add("Durability: " + (stack.getMaxDamage() - stack.getItemDamage()) + " / " + stack.getMaxDamage());
		} else {
			
		}
		
	}
	/*1.11
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		ItemStack stack = (hand == EnumHand.MAIN_HAND) ? player.getHeldItemMainhand() : player.getHeldItemOffhand();
		
		if (!world.isRemote){
			double pl = GetNBTFieldDouble(stack, "powerLevel", 0d);
			double incr = Math.pow(pl * .2d, 0.965d) * .001d;
			IncreasePowerLevel(stack, incr);
			
		}
		return super.onItemRightClick(world, player, hand);
	}
	*/
	
	public void IncreasePowerLevel(ItemStack stack, double inc){
		double pl = GetNBTFieldDouble(stack, "powerLevel", 0d);
		SetNBTFieldDouble(stack, "powerLevel", pl + inc);
	}
	
	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player){
		UpdateStats(stack);
		return super.onBlockStartBreak(stack, pos, player);
	}
	
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){
		if (!world.isRemote){
			float hardness = state.getBlockHardness(world, pos);
			double pl = GetNBTFieldDouble(stack, "powerLevel", 0d);
			double incr = Math.pow(Math.pow(hardness, 2d) / pl, .5d) / 60d;
			IncreasePowerLevel(stack, incr);
		}
		stack.damageItem(1, entity);
		return true;
	}
	
	private void UpdateStats(ItemStack stack){
		double pl = GetNBTFieldDouble(stack, "powerLevel", 0d);
		if (pl == 0d){
			pl = 3d;
			SetNBTFieldDouble(stack, "powerLevel", pl);
		}
		this.efficiencyOnProperMaterial = (float)(Math.pow(pl / 10f, .66d) * 10d);
		this.damageVsEntity = (float)(Math.pow(pl, .75d) / 3d);
		if (pl < 2d)
			this.toolMaterial = ToolMaterial.WOOD;
		else if (pl < 4d)
			this.toolMaterial = ToolMaterial.STONE;
		else if (pl < 8d)
			this.toolMaterial = ToolMaterial.IRON;
		else if (pl < 16d)
			this.toolMaterial = ToolMaterial.GOLD;	
		else
			this.toolMaterial = ToolMaterial.DIAMOND;	
		
		this.setMaxDamage((int)(200f + Math.pow(999800d, (1d - 1d / Math.pow(pl,  .2d)))));
	}
	
	private void UpdateLastRepairPowerLevel(ItemStack stack){
		
	}
	
	private NBTTagCompound GetNBT(ItemStack stack){
		if (stack.getTagCompound() == null)
			return new NBTTagCompound();
		return stack.getTagCompound();
	}
	
	private void SetNBTFieldDouble(ItemStack stack, String key, double val){
		NBTTagCompound nbt = GetNBT(stack);
		
		nbt.setDouble(key, val);
		stack.setTagCompound(nbt);
		UpdateStats(stack);
	}
	
	
	private double GetNBTFieldDouble(ItemStack stack, String key, double _default){
		NBTTagCompound nbt = GetNBT(stack);
		
		if (!nbt.hasKey(key)){
			double pl = _default;
			nbt.setDouble(key, pl);
			stack.setTagCompound(nbt);
			UpdateStats(stack);
			return _default;
		}
		return nbt.getDouble(key);
	}
	
	
	@Override
	public void setDamage(ItemStack stack, int damage){
		if (damage < stack.getItemDamage()){
			System.out.println("Item was repaired!");
		}
		super.setDamage(stack, damage);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int metadata, boolean b){
		if (dirty){
			System.out.println("item update!");
			double lastRepPL = GetNBTFieldDouble(stack, "lastRepairPowerLevel", -1d);
			System.out.println("Last repail PL: " + lastRepPL);
			if (lastRepPL == -1d){
				UpdateLastRepairPowerLevel(stack);
			}
			UpdateStats(stack);
			dirty = false;
		}
		

		//nbt.setInteger("lastDurability", value);
	
	}
	
	/*private int GetRepairAmount(ItemStack material){

		System.out.println("Fuel of " + material.getItem().getUnlocalizedName() + ": " + GameRegistry.getFuelValue(material));
		return TileEntityFurnace.getItemBurnTime(material);

	}*/
	
	@Override
	public boolean getIsRepairable(ItemStack repaired, ItemStack material){
		/*float health = (repaired.getMaxDamage() - repaired.getItemDamage()) / (float)repaired.getMaxDamage();
		int repairAmount = GetRepairAmount(material);
		if (health < 1f && repairAmount > 0){
			repaired.setRepairCost(0);
			System.out.println("Repair: " + repairAmount);
			return true;
		}*/
		
		return false;
	}
}
