package com.jaynopp.saiyancraft.player;

import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.input.KeyBindings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.settings.KeyBindingMap;

public class SaiyanMovement {

	private Vec3d lastGroundVel, relativeVelocity, velocity;
	private boolean previouslyGrounded;
	private SaiyanPlayer splayer;
	private EntityPlayer player;
	
	private boolean triedToMoveLastFrame;
	private boolean triedToJumpLastFrame;
	private long msLastJump, msLastJumpTry;
	
	private double speedLimit = 1d;
	private double acceleration = Math.pow(.2d, .5d/(1d));
	private double friction = 1.3d;
	private double airfriction = 1.05d;
	private double SPEED_MOD = 1d/6d;
	private int tickGroundChange;
	
	public boolean canJump = false;
	public boolean jumping = true;
	public boolean falling = false;
	private double currSpeedLimit;
	private double jumpedHeight = 0d;
	private double highestSpeedInJump = 0d;
	
	private DefaultSaiyanData data;
	
	
	public SaiyanMovement(SaiyanPlayer splayer) {
		this.splayer = splayer;
		this.player = splayer.player;
		player.capabilities.setPlayerWalkSpeed(0f);
		lastGroundVel = new Vec3d(0d, 0d, 0d);
		relativeVelocity = new Vec3d(player.motionX, player.motionY, player.motionZ);
		velocity = relativeVelocity.rotateYaw((float)Math.toRadians(-player.rotationYaw));
		player.capabilities.setPlayerWalkSpeed(0f);

	}
	
	public void GroundedChange(boolean groundedNow){
		previouslyGrounded = groundedNow;
		
		if (!groundedNow){
			
		}
		tickGroundChange = player.ticksExisted;
		OnGroundedChange();
	}
	
	public void OnGroundedChange(){
	}
	
	private int GetTicksSinceGroundChange(){
		return player.ticksExisted - tickGroundChange;
	}
	

	private Vec3d GetMoveInputVector(){
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		double strafe = p.movementInput.rightKeyDown ? 1d : p.movementInput.leftKeyDown ? -1d : 0d;
		double forward = p.movementInput.forwardKeyDown ? 1d : p.movementInput.backKeyDown ? -1d : 0d;
		
		triedToMoveLastFrame = strafe != 0d || forward != 0d;
		return new Vec3d(-strafe, 0, forward).normalize();
	}

	public void Update(DefaultSaiyanData data) {
		this.data = data;
		if (!player.isSprinting() && !jumping){
			data.SetStamina(data.GetStamina() + 0.05f);
			if (data.GetStamina() > data.GetMaxStamina())
				data.SetStamina(data.GetMaxStamina());
		}
		
		//acceleration = Math.pow(.2d, .5d/(data.GetAgility()));
		speedLimit = 1d + data.speedBonus;
		currSpeedLimit = player.isSneaking() ? Math.min( Math.pow(speedLimit, .2d) * .5d, 1d) : player.isSprinting() ? (speedLimit + .5d) : Math.pow(speedLimit, .2d);
		boolean inLava = player.isInLava();
		boolean inWater = player.isInWater();
		if (player.onGround || inLava || inWater){
			if (player.isSprinting())
				if (!splayer.UseStamina(0.025f))
					player.setSprinting(false);
			
			
			jumping = false;
			jumpedHeight = 0d;
			highestSpeedInJump = 0d;
			if (player == Minecraft.getMinecraft().player){
				Vec3d move = GetMoveInputVector();
			
				Vec3d acc = move.scale(acceleration * currSpeedLimit);
			
				if (inLava)
					acc = acc.scale(.2d);
				if (inWater)
					acc = acc.scale(.5d);
				
				velocity = velocity.add(acc.rotateYaw((float)Math.toRadians(-player.rotationYaw)));
			}
			if (!previouslyGrounded)
				GroundedChange(true);
			if (player.velocityChanged)
				lastGroundVel = new Vec3d(velocity.xCoord, velocity.yCoord, velocity.zCoord);
			
		} else {
			if (player.motionY < 0d){
				jumping = false;
				falling = true;
			} else {
				jumpedHeight += player.motionY;
				if (player.motionY >= highestSpeedInJump){
					highestSpeedInJump = player.motionY;
				}
			}
			velocity.scale(airfriction);
			if (previouslyGrounded)
				GroundedChange(false);
		}
		

		double l = velocity.lengthVector();
		if ((!triedToMoveLastFrame && player.onGround))
			velocity = velocity.scale(1d / friction);
		else if (l > currSpeedLimit){
			if(previouslyGrounded && this.GetTicksSinceGroundChange() > 0)
				if (inWater || inLava || player.onGround)
					velocity = velocity.scale(1d / friction);
		}
		

		
		relativeVelocity = velocity.rotateYaw((float)Math.toRadians(player.rotationYaw));
		player.setVelocity(velocity.xCoord * SPEED_MOD, player.motionY, velocity.zCoord * SPEED_MOD);
		
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		if (player == p){
			
			if (!p.movementInput.jump && player.onGround && !canJump)
				canJump = true;
			
			
			triedToJumpLastFrame = p.movementInput.jump;
		}
	}

	public void OnJump() {
		if (!canJump){
			if (player.onGround)
				player.motionY = 0d;

		} else {
			canJump = false;
			jumping = true;
			Vec3d speedBonus = velocity.normalize().scale(currSpeedLimit * .1d / Math.max(velocity.lengthVector(), 1f));
			velocity = velocity.add(speedBonus);
			player.motionY += (Math.pow(splayer.GetData().GetAgility(), .5d) -1d) * .2d;
			GroundedChange(false);
		}
	}

}
