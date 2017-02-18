package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataProvider;
import com.jaynopp.saiyancraft.eventhandlers.EventHandler;
import com.jaynopp.saiyancraft.lib.Names.Items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;

public class SaiyanPlayer {

	public static SaiyanPlayer local;
	public EntityPlayerSP player;
	public SaiyanMovement movement;
	private boolean blocking;
	
	
	public void Block(boolean value){
		blocking = value;
	}
	
	public SaiyanPlayer(EntityPlayerSP player) {
		this.player = player;
		movement = new SaiyanMovement(this);
	}
	
	public DefaultSaiyanData GetData(){
		return DefaultSaiyanData.Get(player);
	}

	public static void Initialize(){
		local = new SaiyanPlayer(Minecraft.getMinecraft().player);
	}
	
	public void Update(){
		DefaultSaiyanData data = GetData();
		movement.Update(data);
	}

	public void OnJump() {
		movement.OnJump();
		
	}
	
	public static boolean isPlayerEntityUsingFists(EntityPlayer player){
		if (player.getHeldItemMainhand().getItem() == net.minecraft.init.Items.AIR && player.getHeldItemOffhand().getItem() == net.minecraft.init.Items.AIR)
			return true;
		
		System.out.println(player.getHeldItemMainhand().getDisplayName() + " and " + player.getHeldItemOffhand().getDisplayName());
		return false;
	}
	
	public boolean UseStamina(float amount){
		DefaultSaiyanData data = GetData();
		float curr = data.GetStamina();
		if (curr >= amount){
			data.SetStamina(curr - amount);
			return true;
		} else {
			data.SetStamina(0f);
			return curr > 0f;
		}
	}
}
