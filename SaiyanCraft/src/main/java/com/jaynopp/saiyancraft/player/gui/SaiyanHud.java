package com.jaynopp.saiyancraft.player.gui;

import org.lwjgl.opengl.GL11;

import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SaiyanHud extends Gui {
	public static SaiyanHud inst;
	
	public SaiyanHud(){
		inst = this;
	}

	public void DrawHealth(RenderGameOverlayEvent event) {
		EntityPlayer player =  Minecraft.getMinecraft().player;
		float healthMax = player.getMaxHealth();
		float health = player.getHealth();
		DefaultSaiyanData data = DefaultSaiyanData.Get(player);
		
		DrawBar(health, healthMax, 5f,  8, 4, 1f, .0f, 0f, 0);
		DrawBar(data.GetStamina(), data.GetMaxStamina(), 25f, 8, 8, 0f, .85f, .0f, 0);
		DrawBar(data.GetKi(), data.GetMaxKi(), 25f, 8, 12, 0f, .5f, 1f, 0);
		
		 GL11.glColor3f(1f, 1f, 1f);
		
	}
	
	void DrawBar(float value, float maxValue, float valPerSegment, int left, int top, float r, float g, float b, int barID){

		int barTexY = 64 + barID * 10;
		float vpsTex = 10f / valPerSegment;
		int maxMidWidth = (int)(vpsTex * (maxValue - valPerSegment*2f));
		int width = (int)(vpsTex * (value));
		if (maxMidWidth > 161)
			maxMidWidth = 161;
		if (width > 181)
			width = 181;	
        drawTexturedModalRect(left, top, 0, barTexY, 11, 5);
        drawTexturedModalRect(left+11, top, 11, barTexY, maxMidWidth, 5);
        drawTexturedModalRect(left+11+maxMidWidth, top, 172, barTexY, 11, 5);
        barTexY = 69 + barID * 10;
		GL11.glPushMatrix();
		GL11.glColor3f(r, g, b);
        drawTexturedModalRect(left, top, 0, barTexY, (width > 11) ? 11 : width, 5);
        if (width > 11)
        	drawTexturedModalRect(left+11, top, 11, barTexY, width > 159 ? 170 : (width-11), 5);
        if (width > 170)
        drawTexturedModalRect(left+11+maxMidWidth, top, 172, barTexY, 11, 5);
        GL11.glPopMatrix();
	}

	public void DrawCharge(RenderGameOverlayEvent event) {
		if (!SaiyanPlayer.local.isChargingHeavy())
			return;
		int width = event.getResolution().getScaledHeight();
		int height = event.getResolution().getScaledHeight();
		DrawBar(SaiyanPlayer.local.attackCharge, 1f, 0.2f, width - 40, height/2 +20, 1f, 1f, 1f, 2);
		
		
	}
}
