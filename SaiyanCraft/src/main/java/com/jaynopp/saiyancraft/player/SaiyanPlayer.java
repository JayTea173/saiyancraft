package com.jaynopp.saiyancraft.player;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.DefaultSaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;
import com.jaynopp.saiyancraft.input.KeyBindings;
import com.jaynopp.saiyancraft.player.moves.combos.ComboManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

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
	private long updates = 0;
	
	public static Map<String, SaiyanPlayer> players = new HashMap<String, SaiyanPlayer>();
	public ComboManager comboManager;
	
	public void Block(boolean value){
		blocking = value;
	}
	
	public SaiyanPlayer(EntityPlayer player) {
		this.player = player;
		movement = new SaiyanMovement(this);
	}
	
	public void SetupComboManager(){
		System.out.println("Setting up combomanager with " + GetBattler().GetMoves().size() + " moves.");
		comboManager = new ComboManager(player);
	}
	
	public DefaultSaiyanData GetStats(){
		return DefaultSaiyanData.Get(player);
	}
	
	public DefaultSaiyanBattler GetBattler(){
		return DefaultSaiyanBattler.Get(player);
	}

	public static void Initialize(EntityPlayer player){
		System.out.println("Initialized SaiyanPlayer");
		SaiyanPlayer sp = new SaiyanPlayer(player);
		if (player == Minecraft.getMinecraft().player)
			local = sp;
		System.out.println("adding splayer: " + player.getName());
		players.put(player.getName(), sp);
		
			
	}
	
	public static SaiyanPlayer Get(EntityPlayer player){
		System.out.println("Looking for splayer of " + player.getName() + ", we have " + players.size());
		String name = player.getName();
		if (players.containsKey(name))
			return players.get(name);
		else
			System.out.println("UNABLE TO FIND SPLAYER!");
		return null;
	}
	
	public void Update(){
		updates++;
		if (updates % 120 == 0){
			System.out.println("Sending periodic update to server.");
			SaiyanCraft.network.sendToServer(new SyncSaiyanDataMessage(GetStats()));
		}
		DefaultSaiyanData data = GetStats();
		GetBattler().Update(this);
		if (comboManager != null)
			if (comboManager.nextQueued && GetBattler().CanAttack()){
				System.out.println("next from queued!");
				comboManager.ContinueCombo(this, this.GetRayTraceTargetEntity());
	
			}
		if (chargingHeavy){
			attackCharge += 0.85f * DT;
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
		} else {
			data.SetStamina(0f);
			if (curr <= 0)
				return false;
				
		}
		float end = data.GetEndurance();
		float bonus = (float) (amount * 0.0004d / Math.pow(data.GetMaxStamina() / 100d, 0.66d) / Math.pow(end, 0.5d));
		data.SetEndurance(end + bonus);
		return true;
	}

	public boolean isChargingHeavy() {
		return chargingHeavy;
	}

	public void setChargingHeavy(boolean chargingHeavy) {
		this.chargingHeavy = chargingHeavy;
		if (chargingHeavy)
			attackCharge = 0f;
		
	}

	public Entity GetRayTraceTargetEntity() {
		
		return Minecraft.getMinecraft().objectMouseOver.entityHit;
	}
}
