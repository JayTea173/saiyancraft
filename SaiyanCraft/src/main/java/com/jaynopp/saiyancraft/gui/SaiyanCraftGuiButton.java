package com.jaynopp.saiyancraft.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class SaiyanCraftGuiButton extends GuiButton {
	public boolean isDown;
	
	public SaiyanCraftGuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	
	@Override
	public boolean mousePressed (Minecraft mc, int mouseX, int mouseY){
		if (isDown && isMouseOver())
			isDown = !isDown;
		else
			isDown = isMouseOver();
		
		return super.mousePressed(mc, mouseX, mouseY);
	}
	
	@Override
	public void mouseDragged (Minecraft mc, int mouseX, int mouseY){
		//System.out.println("DRAG");
		super.mouseDragged(mc, mouseX, mouseY);
	}
	
	@Override
	public void mouseReleased (int mouseX, int mouseY){
		System.out.println("RELEASED");
		isDown = false;
		super.mouseReleased(mouseX, mouseY);
	}
	
	

}
