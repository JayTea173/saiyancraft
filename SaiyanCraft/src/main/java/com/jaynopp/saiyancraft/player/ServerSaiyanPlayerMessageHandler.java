package com.jaynopp.saiyancraft.player;

import java.util.Iterator;

import com.google.common.collect.Iterators;
import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;
import com.jaynopp.saiyancraft.crafting.RecipeDifficultyRater;
import com.jaynopp.saiyancraft.damagesources.SaiyanDamageSource;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ServerSaiyanPlayerMessageHandler  implements IMessageHandler<ClientPlayerFallMessage, IMessage> {
	@Override
	public IMessage onMessage(ClientPlayerFallMessage message, MessageContext ctx) {
		EntityPlayerMP sender = ctx.getServerHandler().playerEntity;
		DefaultSaiyanData data = DefaultSaiyanData.Get(sender);
		IThreadListener mainThread = (WorldServer) sender.world;
		mainThread.addScheduledTask(new Runnable(){
			@Override
			public void run(){
				
				double fallNegation =  Math.pow((data.GetPowerLevel() - 3d) * 0.001d, .25d);
				System.out.println("Pla+yer fell, speed on impact: " + message.speed + " negation: " + fallNegation + " pl: " + data.GetPowerLevel());
				if (message.speed > .8d + fallNegation){
					//event.setDistance(8f);
					float dmg = (float) (message.speed - .8d + fallNegation) * 10f;
					sender.attackEntityFrom(DamageSource.FALL, dmg);
					System.out.println("Damage: " + dmg);	
					SaiyanCraft.network.sendTo(new SyncSaiyanDataMessage(data), sender);
				}
				

			}
		});
		return null;
		
		
		
	}
}
