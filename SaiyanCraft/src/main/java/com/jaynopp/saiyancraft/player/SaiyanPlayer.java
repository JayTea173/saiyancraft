package com.jaynopp.saiyancraft.player;

import java.util.ArrayList;
import java.util.List;

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
	public EntityPlayer player;
	public SaiyanMovement movement;
	private boolean blocking;
	
	
	public static List<SaiyanPlayer> players = new ArrayList<SaiyanPlayer>();
	
	public void Block(boolean value){
		blocking = value;
	}
	
	public SaiyanPlayer(EntityPlayer player) {
		this.player = player;
		movement = new SaiyanMovement(this);
	}
	
	public DefaultSaiyanData GetData(){
		return DefaultSaiyanData.Get(player);
	}

	public static void Initialize(EntityPlayer player){
		SaiyanPlayer sp = new SaiyanPlayer(player);
		if (player == Minecraft.getMinecraft().player)
			local = sp;
			
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
		;
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
