package com.jaynopp.saiyancraft.item;

import com.jaynopp.core.item.JNItemInitializer;
import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataProvider;
import com.jaynopp.saiyancraft.gui.SaiyanPlayerStatusGui;
import com.jaynopp.saiyancraft.lib.Names;
import com.jaynopp.saiyancraft.lib.Names.Items;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class DragonBall extends Item implements JNItemInitializer {
	public static DragonBall instance;
	
	public static void Initialize(){
		instance = new DragonBall();
	}
	
	public DragonBall(){
		JNItemInitializer.super.Initialize(this, SaiyanCraft.resourcePrefix, Names.Items.DRAGON_BALL);
		setCreativeTab(CreativeTabs.MATERIALS);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		if (!world.isRemote){
		
		} else {
			//Minecraft.getMinecraft().displayGuiScreen(new SaiyanPlayerStatusGui());
		}
		return super.onItemRightClick(world, player, hand);
	}
	

	
}
