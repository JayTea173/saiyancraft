package com.jaynopp.saiyancraft.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.jaynopp.saiyancraft.SaiyanCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class ScrollView {
	int x, y, width, height, scrollButtonID;
	SaiyanCraftGuiButton scrollButton;
	final ResourceLocation bgTexture = new ResourceLocation(SaiyanCraft.modId, "textures/common/white.png");
	float position;
	protected List<ScrollViewItem> items; 
	protected ScrollViewItem hoveredItem;
	
	public ScrollView(int x, int y, int width, int height, int scrollButtonID){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.scrollButtonID = scrollButtonID;
		this.items = new ArrayList<ScrollViewItem>();
	}
	
	public ScrollViewItem AddItem(Object object, String displayText){
		System.out.println("Register ScrollViewItem: " + displayText);
		ScrollViewItem item = new ScrollViewItem(object, displayText);
		items.add(item);
		return item;
		
	}
	
	public void Clear(){
		items.clear();
		
	}
	
	public void Draw(TextureManager renderer, Gui gui, int mouseX, int mouseY){
		if (scrollButton.isDown){
			position = (float)(mouseY - y - 3) / (float)(height-9);
			if (position > 1f)
				position = 1f;
			if (position < 0f)
				position = 0f;
		}
		
		DrawBar(gui);
		DrawItems(gui, mouseX, mouseY);
	}
	
	protected void DrawItems(Gui gui, int mouseX, int mouseY){
		int currY = 0;
		FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
		fr.FONT_HEIGHT = 8;
		boolean hovering = false;
		for (ScrollViewItem item : items){
			gui.drawString(fr, item.displayText, x + 2, y + currY + 2, RGBToInt(255, 255 - (int)(.2f * item.hoverTime * 85), 255 - (int)(1f * item.hoverTime * 85)));
			if (mouseX > x + 2 && mouseX < x + width && mouseY > y + 2 + currY && mouseY < y + 2 + currY + 10){
				
				hovering = true;
				if (hoveredItem != item)
					hoveredItem = item;
			}
			if (item == hoveredItem){
				if (item.hoverTime < 3)
					item.hoverTime++;	
			
			} else if (item.hoverTime > 0)
				item.hoverTime--;
			currY += 12;
		}
		if (!hovering){
			if (hoveredItem  != null){
				hoveredItem = null;
			}
		}
	}
	
	protected void DrawBar(Gui gui){
		scrollButton.yPosition = y + (int)(position * (height - 9));
		Minecraft.getMinecraft().renderEngine.bindTexture(bgTexture);
		GL11.glPushMatrix();
		GL11.glColor3f(0f, 0f, 0f);
		gui.drawTexturedModalRect(x + width - 10, y, 0, 0, 10, height);
		GL11.glPopMatrix();
		GL11.glColor3f(1f, 1f, 1f);
	}
	

	
	public static int RGBToInt(int r, int g, int b){
		r = Clamp(r, 0, 255);
		g = Clamp(g, 0, 255);
		b = Clamp(b, 0, 255);
		return r + g * 256 + b * 256 * 256;
	}
	
	public static int Clamp (int val, int low, int high){
		return Math.max(low, Math.min(high, val));
		
	}
	
	public GuiButton RegisterScrollButton(){
		return scrollButton = new SaiyanCraftGuiButton(scrollButtonID, x + width - 9, y, 8, 8, "");
	}
}
