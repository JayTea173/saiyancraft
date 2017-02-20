package com.jaynopp.saiyancraft.input;

import org.lwjgl.input.Keyboard;

import com.jaynopp.saiyancraft.input.StatedKeyBinding.KeyEvents;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class KeyBindings {
	public static KeyBinding openSaiyanGUI;
	public static KeyBinding block;
	public static StatedKeyBinding light_attack;
	public static StatedKeyBinding heavy_attack;
	
	public void Init(){
		openSaiyanGUI = new KeyBinding("key.opensaiyangui", Keyboard.KEY_O, "key.categories.SaiyanCraft");
		block = new KeyBinding("key.block", Keyboard.KEY_LCONTROL, "key.categories.SaiyanCraft");
		heavy_attack = new StatedKeyBinding("key.heavy_attack", -99 /*rmb*/, "key.categories.SaiyanCraft", new KeyEvents(){
			public void OnDown(){

				SaiyanPlayer.local.comboManager.Act(heavy_attack.binding, SaiyanPlayer.local,  SaiyanPlayer.local.GetRayTraceTargetEntity());
				/*if (SaiyanPlayer.isPlayerEntityUsingFists()){
	        	SaiyanPlayer.local.setChargingHeavy(true);
			}*/
			}
			
			public void OnUp(){
				if (SaiyanPlayer.local.isChargingHeavy() && SaiyanPlayer.local.comboManager.currentCombo != null)
					SaiyanPlayer.local.comboManager.currentCombo.ReleaseChargeableMove(SaiyanPlayer.local, SaiyanPlayer.local.GetRayTraceTargetEntity());
			}
			
			public void OnPressed(){

			}
		});
		light_attack = new StatedKeyBinding("key.light_attack", -100 /*lmb*/, "key.categories.SaiyanCraft", new KeyEvents(){
			public void OnDown(){
				SaiyanPlayer.local.comboManager.Act(light_attack.binding, SaiyanPlayer.local,  SaiyanPlayer.local.GetRayTraceTargetEntity());
			}
			
			public void OnUp(){
				if (SaiyanPlayer.local.isChargingHeavy())
					SaiyanPlayer.local.comboManager.currentCombo.ReleaseChargeableMove(SaiyanPlayer.local, SaiyanPlayer.local.GetRayTraceTargetEntity());
			}
			
			public void OnPressed(){

			}
		});
		ClientRegistry.registerKeyBinding(openSaiyanGUI);
	}
	
}
