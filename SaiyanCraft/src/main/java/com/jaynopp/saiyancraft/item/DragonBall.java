package com.jaynopp.saiyancraft.item;

import com.jaynopp.core.item.JNItemInitializer;
import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.lib.Names;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class DragonBall extends Item implements JNItemInitializer {
	public static DragonBall instance;
	
	public static void Initialize(){
		instance = new DragonBall();
	}
	
	public DragonBall(){
		JNItemInitializer.super.Initialize(this, SaiyanCraft.resourcePrefix, Names.Items.DRAGON_BALL);
		setCreativeTab(CreativeTabs.MATERIALS);
	}
	/*1.11
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand){
		if (!world.isRemote){
		
		} else {
			//Minecraft.getMinecraft().displayGuiScreen(new SaiyanPlayerStatusGui());
		}
		return super.onItemRightClick(world, player, hand);
	}
	*/
	

	
}
