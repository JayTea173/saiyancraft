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
import com.jaynopp.saiyancraft.player.moves.BaseMeleeMove;
import com.jaynopp.saiyancraft.player.moves.combos.ComboManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
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
			blocked = HandlePerfectBlockAttack(event.getAmount(), attacker, attackerBattler);
		else
			blocked = HandleBlockAttack(event.getAmount(), attacker, attackerBattler);

	}

	public void HandleBlockingDamage(Entity attacker, float amount){
		ISaiyanBattler attackerBattler = (attacker == null) ? null : attacker.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
		System.out.println("blocked " + attacker.getName());
		boolean blocked = false;
		if (timeBlocked < 0.2f)
			blocked = HandlePerfectBlockAttack(amount, attacker, attackerBattler);
		else
			blocked = HandleBlockAttack(amount, attacker, attackerBattler);

	}
	
	private boolean HandlePerfectBlockAttack(float amount, Entity attacker, ISaiyanBattler attackerBattler){
		float staminaUsage = 10f;
		boolean enoughStamina = UseStamina(staminaUsage);
		
		if (attacker != null){
			if (attacker instanceof EntityLivingBase){
				if (attackerBattler != null){
					EntityLivingBase livingEntity = ((EntityLivingBase)attacker);
					attackerBattler.SetStunTime(1.2f);
					BaseMeleeMove.KnockBackEntity(player, attacker, 1.3f, false);
					DefaultSaiyanBattler.AddStun(livingEntity);
					if (player.world.isRemote)
						Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(attacker, EnumParticleTypes.CRIT);
					
					
				}
			}
		}

		
		
		if (enoughStamina)
			amount = 0f;
		else
			amount = amount + .1f;
		
		timeUsedStamina = 500; //instantly reg stamina
		return enoughStamina;
	}
	
	private boolean HandleBlockAttack(float amount, Entity attacker, ISaiyanBattler attackerBattler){
		float staminaUsage = (float) Math.pow(amount * 2.5d, (0.01d + (Math.pow(3d / GetStats().GetPowerLevel(), 0.045d) * 0.99d)));
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
			float damage = amount * 4f;
			if (attacker != null && player != null)
				KnockBack(attacker, .8f);
			//event.setCanceled(true);
			timeUsedStamina = -500; //another .5 seconds until stamina regens
		} else {
			if (player.world.isRemote){
				Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.CRIT_MAGIC);
				Minecraft.getMinecraft().effectRenderer.emitParticleAtEntity(player, EnumParticleTypes.REDSTONE);
			}
			ISaiyanBattler battler = GetBattler();
			battler.SetStunTime(2f);
			if (attacker != null && player != null){
				movement.KnockAirborne(0.25f * amount);
				KnockBack(attacker, 0.4f + amount * 0.1f);	
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
		if (player.world.isRemote && Minecraft.getMinecraft().player == player){
			local = sp;
			System.out.println("Local player set to: " + player.getDisplayName());
		}
		
		System.out.println("adding splayer: " + player.getName());
		players.put(player.getName(), sp);
				
	}
	
	public static void Initialize(EntityPlayer player, SaiyanPlayer old){
		
		SaiyanPlayer sp = new SaiyanPlayer(player);
		if (player.world.isRemote && Minecraft.getMinecraft().player == player){
				local = sp;
				System.out.println("Local player set to: " + player.getDisplayName());
		}

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
		if (players.containsKey(name)){
			SaiyanPlayer p = players.get(name);
			if (p == null)
				System.out.println("Your player lists contains null player!");
			return p;
		}

		System.out.println("UNABLE TO FIND SPLAYER!");
		return null;
	}
	
	public void RenderHands(){
		
	}
	
	public void Update(){
		if (player.isSwingInProgress){
			//if (player.swingProgress > 1f)
			//	player.swingProgress = 0f;
			//System.out.println("Swing: " + player.swingProgress + " pre: " + player.prevSwingProgress);
		}
		
		updates++;
		if (updates % 120 == 0){
			//System.out.println("Sending periodic update to server.");
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
		return player == local.player;
	}

	public void OnJump() {
		movement.OnJump();
		
	}
	
	public boolean isBlocking(){
		return blocking;
	}
	
	public static boolean isPlayerEntityUsingFists(EntityPlayer player){
		//replace null with getItem() == net.minecraft.init.Items.AIR (1.11)
		if (player.getHeldItemMainhand() == null && player.getHeldItemOffhand() == null)
			return true;
		
		return false;
	}
	
	public static boolean isPlayerEntityUsingFists(){
		//replace null with net.minecraft.init.Items.AIR (1.11)
		EntityPlayer player = local.player;
		if (player.getHeldItemMainhand() == null && player.getHeldItemOffhand() == null)
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
	
	private boolean UseKiAndIncreaseStats(float amount, float statGainMod){
		DefaultSaiyanData data = GetStats();
		float curr = data.GetKi();
		float max = data.GetMaxKi();
		if (curr >= amount){
			data.SetKi(curr - amount);
		} else {
			data.SetKi(0f);
			return false;	
		}
		float spi = data.GetSpirit();
		float bonus = (float) (amount * 0.0004d / Math.pow(max / 100d, 0.66d) / Math.pow(spi, 0.5d));
		data.SetSpirit(spi + statGainMod * bonus);
		return true;	
	}
	
	public boolean UseKi(float amount){
		return UseKiAndIncreaseStats(amount, 1f);
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

	public void Damage(EntityPlayerMP victim, Entity attacker, float amount) {
		amount *= (1f - (float)victim.getTotalArmorValue() * 0.04f);
		victim.setHealth(victim.getHealth() - amount);
		if (this == local)
			player.performHurtAnimation();
		//System.out.println("Dealing " + amount + " damage to " + victim.getDisplayNameString() + " with " + victim.getTotalArmorValue() + " armor " + " health is now " + victim.getHealth() + "/" + victim.getMaxHealth());
		DefaultSaiyanData data = GetStats();
		float curr = data.GetVitality();
		float bonus = 1f;
		if (!(Float.isInfinite(bonus) && curr == 0f))
			bonus = (float) (Math.pow(amount / curr, 1.4d) * .0025d);

		//System.out.println("Player was attacked! Vitality Stat increased by " + bonus);
		data.SetVitality(curr + bonus);
		DefaultSaiyanData.UpdateStats(player);
		if (local == this){
			
		}
		if (player.getHealth() < 0f){
			player.onKillCommand();
		}
	}
}
