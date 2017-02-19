package com.jaynopp.saiyancraft.player;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.jaynopp.saiyancraft.capabilities.saiyanbattler.DefaultSaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.input.KeyBindings;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public class SaiyanPlayer {

	public static final int TPS = 40;
	public static final float DT = 1f / (float)TPS;
	public static SaiyanPlayer local;
	public EntityPlayer player;
	public SaiyanMovement movement;
	private boolean blocking;
	private boolean chargingHeavy;
	public float attackCharge;
	
	private int oldAttackKeyCode; //used for disabling vanilla attack when left clicking
	
	
	public static List<SaiyanPlayer> players = new ArrayList<SaiyanPlayer>();
	
	public void Block(boolean value){
		blocking = value;
	}
	
	public SaiyanPlayer(EntityPlayer player) {
		this.player = player;
		movement = new SaiyanMovement(this);
	}
	
	public DefaultSaiyanData GetStats(){
		return DefaultSaiyanData.Get(player);
	}
	
	public DefaultSaiyanBattler GetBattler(){
		return DefaultSaiyanBattler.Get(player);
	}

	public static void Initialize(EntityPlayer player){
		SaiyanPlayer sp = new SaiyanPlayer(player);
		if (player == Minecraft.getMinecraft().player)
			local = sp;
			
	}
	
	public void Update(){
		
		DefaultSaiyanData data = GetStats();
		if (chargingHeavy){
			attackCharge += .04f;
			if (attackCharge > 1f)
				attackCharge = 1f;
		}
		
		if (IsLocalPlayer()){
			if (isPlayerEntityUsingFists(player)){
				if (Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode() != Keyboard.KEY_NONE){
					oldAttackKeyCode = Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode();
					Minecraft.getMinecraft().gameSettings.keyBindAttack.setKeyCode(Keyboard.KEY_NONE);
					KeyBindings.light_attack.binding.setKeyCode(-100);
				}
			} else {
				if (Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode() == Keyboard.KEY_NONE){
					Minecraft.getMinecraft().gameSettings.keyBindAttack.setKeyCode(oldAttackKeyCode);	
					KeyBindings.light_attack.binding.setKeyCode(Keyboard.KEY_NONE);
				}
			}
				
		}
		
		movement.Update(data);
		
	}
	
	public boolean IsLocalPlayer(){
		return player == Minecraft.getMinecraft().player;
	}

	public void OnJump() {
		movement.OnJump();
		
	}
	
	public boolean isBlocking(){
		return blocking;
	}
	
	public static boolean isPlayerEntityUsingFists(EntityPlayer player){
		if (player.getHeldItemMainhand().getItem() == net.minecraft.init.Items.AIR && player.getHeldItemOffhand().getItem() == net.minecraft.init.Items.AIR)
			return true;
		
		return false;
	}
	
	public static boolean isPlayerEntityUsingFists(){
		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player.getHeldItemMainhand().getItem() == net.minecraft.init.Items.AIR && player.getHeldItemOffhand().getItem() == net.minecraft.init.Items.AIR)
			return true;
		;
		return false;
	}
	
	public boolean UseStamina(float amount){
		DefaultSaiyanData data = GetStats();
		float curr = data.GetStamina();
		if (curr >= amount){
			data.SetStamina(curr - amount);
			return true;
		} else {
			data.SetStamina(0f);
			return curr > 0f;
		}
	}

	public boolean isChargingHeavy() {
		return chargingHeavy;
	}

	public void setChargingHeavy(boolean chargingHeavy) {
		this.chargingHeavy = chargingHeavy;
		if (chargingHeavy)
			attackCharge = 0f;
		
	}

	public void LightAttack() {
		player.swingArm(EnumHand.MAIN_HAND);
		
	}
}
