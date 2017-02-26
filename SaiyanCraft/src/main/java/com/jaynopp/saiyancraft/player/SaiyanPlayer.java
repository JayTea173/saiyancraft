package com.jaynopp.saiyancraft.player;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.DefaultSaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SaiyanBattlerProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;
import com.jaynopp.saiyancraft.input.KeyBindings;
import com.jaynopp.saiyancraft.player.moves.BaseMove;
import com.jaynopp.saiyancraft.player.moves.combos.ComboManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class SaiyanPlayer {

	public static final int TPS = 40;
	public static final float DT = 1f / (float)TPS;
	public static SaiyanPlayer local;
	public EntityPlayer player;
	public SaiyanMovement movement;
	private boolean blocking;
	private boolean chargingHeavy;
	public float attackCharge;
	public float timeBlocked = 0f;
	public long timeUsedStamina = 0;
	
	private int oldAttackKeyCode; //used for disabling vanilla attack when left clicking
	private long updates = 0;
	
	public static Map<String, SaiyanPlayer> players = new HashMap<String, SaiyanPlayer>();
	public ComboManager comboManager;
	
	public void Block(boolean value){
		blocking = value;
		if (!value)
			timeBlocked = 0f;
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
	
	public void HandleBlockingEvent(LivingAttackEvent event){
		Entity attacker = event.getSource().getEntity();
		ISaiyanBattler attackerBattler = (attacker == null) ? null : attacker.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
		boolean blocked = false;
		if (timeBlocked < 0.2f)
			blocked = HandlePerfectBlockAttack(event, attacker, attackerBattler);
		else
			blocked = HandleBlockAttack(event, attacker, attackerBattler);

	}
	
	private boolean HandlePerfectBlockAttack(LivingAttackEvent event, Entity attacker, ISaiyanBattler attackerBattler){
		float staminaUsage = 10f;
		boolean enoughStamina = UseStamina(staminaUsage);
		
		if (attacker != null){
			if (attacker instanceof EntityLivingBase){
				if (attackerBattler != null){
					EntityLivingBase livingEntity = ((EntityLivingBase)attacker);
					attackerBattler.SetStunTime(1.2f);
					BaseMove.KnockBackEntity(player, attacker, 1.3f, false);
					DefaultSaiyanBattler.AddStun(livingEntity);
					if (player.world.isRemote)
						Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(attacker, EnumParticleTypes.CRIT);
					
					
				}
			}
		}

		
		
		if (enoughStamina)
			event.setCanceled(true);
		else
			event.setCanceled(false);
		
		timeUsedStamina = 500; //instantly reg stamina
		return enoughStamina;
	}
	
	private boolean HandleBlockAttack(LivingAttackEvent event, Entity attacker, ISaiyanBattler attackerBattler){
		float staminaUsage = (float) Math.pow(event.getAmount() * 2.5d, (0.01d + (Math.pow(3d / GetStats().GetPowerLevel(), 0.045d) * 0.99d)));
		boolean enoughStamina = UseStaminaAndIncreaseStats(staminaUsage, .25f);
		if (attacker != null){
			if (attacker instanceof EntityLivingBase){
				if (attackerBattler != null){
						
				}
			}
		}
		
		if (enoughStamina){
			if (player.world.isRemote){
				
			}
			//Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.DAMAGE_INDICATOR);
			float damage = event.getAmount() * 4f;
			if (attacker != null && player != null)
				KnockBack(attacker, .8f);
			event.setCanceled(true);
			timeUsedStamina = -500; //another .5 seconds until stamina regens
		} else {
			if (player.world.isRemote){
				Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.CRIT_MAGIC);
				Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.REDSTONE);
			}
			ISaiyanBattler battler = GetBattler();
			battler.SetStunTime(2f);
			if (attacker != null && player != null){
				movement.KnockAirborne(0.25f * event.getAmount());
				KnockBack(attacker, 0.4f + event.getAmount() * 0.1f);	
			}
			timeUsedStamina = -1000; //extra 1 second stamina regen timeout
		}
		
		return enoughStamina;
	}
	
	public void KnockBack(Entity attacker, float amount){
		Vec3d dir = new Vec3d(player.posX, player.posY, player.posZ).subtract(new Vec3d(attacker.posX, attacker.posY, attacker.posZ)).normalize();
		Vec3d knock = dir.scale(amount);
		player.motionX += knock.xCoord;
		player.motionY += knock.yCoord;
		player.motionZ += knock.zCoord;
		movement.AddVelocity(knock);
		player.velocityChanged = true;
	}

	public static void Initialize(EntityPlayer player){
		
		SaiyanPlayer sp = new SaiyanPlayer(player);
		if (player == Minecraft.getMinecraft().player)
			local = sp;
		
		System.out.println("adding splayer: " + player.getName());
		players.put(player.getName(), sp);
				
	}
	
	public static void Initialize(EntityPlayer player, SaiyanPlayer old){
		
		SaiyanPlayer sp = new SaiyanPlayer(player);
		if (player == Minecraft.getMinecraft().player)
			local = sp;

		System.out.println("updating splayer: " + player.getName());
		players.remove(player.getName(), old);
		players.put(player.getName(), sp);
		if (old != null)
			if (old.comboManager != null)
				sp.comboManager = old.comboManager;
				
	}
	
	public static SaiyanPlayer Get(EntityPlayer player){
		//System.out.println("Looking for splayer of " + player.getName() + ", we have " + players.size());
		String name = player.getName();
		if (players.containsKey(name))
			return players.get(name);

		System.out.println("UNABLE TO FIND SPLAYER!");
		return null;
	}
	
	public void Update(){
		if (player.isSwingInProgress){
			//if (player.swingProgress > 1f)
			//	player.swingProgress = 0f;
			//System.out.println("Swing: " + player.swingProgress + " pre: " + player.prevSwingProgress);
		}
		
		updates++;
		if (updates % 120 == 0){
			System.out.println("Sending periodic update to server.");
			SaiyanCraft.network.sendToServer(new SyncSaiyanDataMessage(GetStats()));
		}
		DefaultSaiyanData data = GetStats();
		GetBattler().Update(this.player, DT);
		if (comboManager != null)
			if (comboManager.nextQueued && GetBattler().CanAttack()){
				System.out.println("next from queued!");
				comboManager.ContinueCombo(comboManager.queuedKey, this, this.GetRayTraceTargetEntity());
	
			}
		if (chargingHeavy){
			attackCharge += 1f / comboManager.currentCombo.moves.get(comboManager.currentCombo.GetCurrent()).move.GetChargeTime() * DT;
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
		if (this.blocking)
			this.timeBlocked += DT;
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
	
	private boolean UseStaminaAndIncreaseStats(float amount, float statGainMod){
		DefaultSaiyanData data = GetStats();
		float curr = data.GetStamina();
		if (curr >= amount){
			data.SetStamina(curr - amount);
			timeUsedStamina = System.currentTimeMillis();
		} else {
			data.SetStamina(0f);
			return false;
				
		}
		float end = data.GetEndurance();
		float bonus = (float) (amount * 0.0004d / Math.pow(data.GetMaxStamina() / 100d, 0.66d) / Math.pow(end, 0.5d));
		data.SetEndurance(end + statGainMod * bonus);
		return true;
	}
	
	public boolean UseStamina(float amount){
		return UseStaminaAndIncreaseStats(amount, 1f);
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
