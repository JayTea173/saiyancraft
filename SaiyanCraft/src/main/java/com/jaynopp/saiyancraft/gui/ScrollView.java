package com.jaynopp.saiyancraft.gui;

import org.lwjgl.opengl.GL11;

import com.jaynopp.saiyancraft.SaiyanCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class ScrollView {
	int x, y, width, height, scrollButtonID;
	SaiyanCraftGuiButton scrollButton;
	final ResourceLocation bgTexture = new ResourceLocation(SaiyanCraft.modId, "textures/common/white.png");
	float position;
	
	public ScrollView(int x, int y, int width, int height, int scrollButtonID){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.scrollButtonID = scrollButtonID;
	}
	
	public void Draw(TextureManager renderer, Gui gui, int mouseX, int mouseY){
		if (scrollButton.isDown){
			position = (float)(mouseY - y - 3) / (float)(height-9);
			if (position > 1f)
				position = 1f;
			if (position < 0f)
				position = 0f;
		}
		scrollButton.yPosition = y + (int)(position * (height - 9));
		Minecraft.getMinecraft().renderEngine.bindTexture(bgTexture);
		GL11.glPushMatrix();
		GL11.glColor3f(0f, 0f, 0f);
		gui.drawTexturedModalRect(x + width - 10, y, 0, 0, 10, height);
		GL11.glPopMatrix();
		GL11.glColor3f(1f, 1f, 1f);
	}
	
	public GuiButton RegisterScrollButton(){
		return scrollButton = new SaiyanCraftGuiButton(scrollButtonID, x + width - 9, y, 8, 8, "");
	}
}
