package com.jaynopp.saiyancraft.gui;

import java.util.List;

import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.ISaiyanBattler;
import com.jaynopp.saiyancraft.capabilities.saiyanbattler.SaiyanBattlerProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class SaiyanPlayerStatusGui extends GuiScreen {

	final ResourceLocation texture = new ResourceLocation(SaiyanCraft.modId, "textures/gui/saiyangui.png");
	final ResourceLocation plTexture = new ResourceLocation(SaiyanCraft.modId, "textures/gui/redscouter.png");
	
	int guiWidth = 256;
	int guiHeight = 192;
	
	GuiButton increaseVitalityButton;
	GuiButton increaseEnduranceButton;
	GuiButton increaseSkillButton;
	GuiButton increaseStrengthButton;
	GuiButton increaseAgilityButton;
	GuiButton increaseSpiritButton;
	final int bIncreaseVitality = 1;
	final int bIncreaseEndurance = 2;
	final int bIncreaseSkill = 3;
	final int bIncreaseStrength = 4;
	final int bIncreaseAgility = 5;
	final int bIncreaseSpirit = 6;
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		int centerX = (width / 2)- guiWidth / 2;
		int centerY = (height / 2) - guiHeight / 2;
		int statOffsetX = Minecraft.getMinecraft().player.isCreative() ? 24 : 4;
		drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(plTexture);
		drawModalRectWithCustomSizedTexture(centerX-8, centerY + 8, 0, 0, 24, 24, 24f, 24f);
		//Minecraft.getMinecraft().renderEngine.bindTexture(plTexture);
		//drawTexturedModalRect(centerX + 4, centerY + 16, 0, 0, 32, 32);
		fontRendererObj.FONT_HEIGHT = 18;
		String playerName = Minecraft.getMinecraft().player.getName();
		drawString(fontRendererObj, playerName, (width / 2) - fontRendererObj.getStringWidth(playerName) / 2, centerY + 2, 0xffffff);
		
		ISaiyanData data = Minecraft.getMinecraft().player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		
		String statStr = ISaiyanData.PLToString(data.GetPowerLevel(), isShiftKeyDown());
		fontRendererObj.drawString(statStr, centerX + 74, centerY + 16, 0x000000);
		fontRendererObj.FONT_HEIGHT = 8;
		statStr = "Vitality";
		fontRendererObj.drawString(statStr, centerX + statOffsetX, centerY + 32, 0xee0000);
		fontRendererObj.drawString(ISaiyanData.PLToString(data.GetVitality(), isShiftKeyDown()), centerX + statOffsetX + 70, centerY + 32, 0xee0000);
		statStr = "Endurance";
		fontRendererObj.drawString(statStr, centerX + statOffsetX, centerY + 42, 0x00ee00);
		fontRendererObj.drawString(ISaiyanData.PLToString(data.GetEndurance(), isShiftKeyDown()), centerX + statOffsetX + 70, centerY + 42, 0xee0000);
		statStr = "Skill";
		fontRendererObj.drawString(statStr, centerX + statOffsetX, centerY + 52, 0x0000ee);
		fontRendererObj.drawString(ISaiyanData.PLToString(data.GetSkill(), isShiftKeyDown()), centerX + statOffsetX + 70, centerY + 52, 0xee0000);
		statStr = "Strength";
		fontRendererObj.drawString(statStr, centerX + statOffsetX, centerY + 62, 0xee9900);
		fontRendererObj.drawString(ISaiyanData.PLToString(data.GetStrength(), isShiftKeyDown()), centerX + statOffsetX + 70, centerY + 62, 0xee0000);
		statStr = "Agility";
		fontRendererObj.drawString(statStr, centerX + statOffsetX, centerY + 72, 0x99ee00);
		fontRendererObj.drawString(ISaiyanData.PLToString(data.GetAgility(), isShiftKeyDown()), centerX + statOffsetX + 70, centerY + 72, 0xee0000);
		statStr = "Spirit";
		fontRendererObj.drawString(statStr, centerX + statOffsetX, centerY + 82, 0x0099ee);
		fontRendererObj.drawString(ISaiyanData.PLToString(data.GetSpirit(), isShiftKeyDown()), centerX + statOffsetX + 70, centerY + 82, 0xee0000);
		
		ISaiyanBattler battler = Minecraft.getMinecraft().player.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
		List<ISaiyanMove> moves = battler.GetMoves();
		for (int i = 0; i <moves.size(); i++){
			ISaiyanMove move = moves.get(i);
			String moveStr = move.GetType().toString() + " (" + move.GetCooldown() + ", " + move.GetStunTime() + ")";
			fontRendererObj.drawString(moveStr, centerX + statOffsetX + 120, centerY + 32 + i * 10, 0xee0000);
		}
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui(){
		int centerX = (width / 2)- guiWidth / 2;
		int centerY = (height / 2) - guiHeight / 2;
		if (Minecraft.getMinecraft().player.isCreative()){
			buttonList.clear();
			
			
			buttonList.add(increaseVitalityButton = new GuiButton(-bIncreaseVitality, centerX + 4, centerY + 32, 6, 6, "-"));
			buttonList.add(increaseEnduranceButton = new GuiButton(-bIncreaseEndurance, centerX + 4, centerY + 42, 6, 6, "-"));
			buttonList.add(increaseSkillButton = new GuiButton(-bIncreaseSkill, centerX + 4, centerY + 52, 6, 6, "-"));
			buttonList.add(increaseStrengthButton = new GuiButton(-bIncreaseStrength, centerX + 4, centerY + 62, 6, 6, "-"));
			buttonList.add(increaseAgilityButton = new GuiButton(-bIncreaseAgility, centerX + 4, centerY + 72, 6, 6, "-"));
			buttonList.add(increaseSpiritButton = new GuiButton(-bIncreaseSpirit, centerX + 4, centerY + 82, 6, 6, "-"));
			buttonList.add(increaseVitalityButton = new GuiButton(bIncreaseVitality, centerX + 12, centerY + 32, 6, 6, "+"));
			buttonList.add(increaseEnduranceButton = new GuiButton(bIncreaseEndurance, centerX + 12, centerY + 42, 6, 6, "+"));
			buttonList.add(increaseSkillButton = new GuiButton(bIncreaseSkill, centerX + 12, centerY + 52, 6, 6, "+"));
			buttonList.add(increaseStrengthButton = new GuiButton(bIncreaseStrength, centerX + 12, centerY + 62, 6, 6, "+"));
			buttonList.add(increaseAgilityButton = new GuiButton(bIncreaseAgility, centerX + 12, centerY + 72, 6, 6, "+"));
			buttonList.add(increaseSpiritButton = new GuiButton(bIncreaseSpirit, centerX + 12, centerY + 82, 6, 6, "+"));
		}
		super.initGui();
	}
	
	@Override
	protected void actionPerformed(GuiButton button){
		ISaiyanData cap = mc.player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		if (cap == null){
			System.err.println("SaiyanGUI trying to get saiyandata for local player, but is null!");
			return;
		}
		float inc = isShiftKeyDown() ? 10f : isCtrlKeyDown() ? 100f : 1f;
		switch(button.id){
		case bIncreaseVitality:
			cap.SetVitality(cap.GetVitality() + inc);
			break;
		case bIncreaseEndurance:
			cap.SetEndurance(cap.GetEndurance() + inc);
			break;
		case bIncreaseSkill:
			cap.SetSkill(cap.GetSkill() + inc);
			break;
		case bIncreaseStrength:
			cap.SetStrength(cap.GetStrength() + inc);
			break;
		case bIncreaseAgility:
			cap.SetAgility(cap.GetAgility() + inc);
			break;
		case bIncreaseSpirit:
			cap.SetSpirit(cap.GetSpirit() + inc);
			break;
		case -bIncreaseVitality:
			cap.SetVitality(cap.GetVitality() - inc);
			break;
		case -bIncreaseEndurance:
			cap.SetEndurance(cap.GetEndurance() - inc);
			break;
		case -bIncreaseSkill:
			cap.SetSkill(cap.GetSkill() - inc);
			break;
		case -bIncreaseStrength:
			cap.SetStrength(cap.GetStrength() - inc);
			break;
		case -bIncreaseAgility:
			cap.SetAgility(cap.GetAgility() - inc);
			break;
		case -bIncreaseSpirit:
			cap.SetSpirit(cap.GetSpirit() - inc);
			break;
			
		}
		DefaultSaiyanData.UpdateStats(mc.player);
		SaiyanCraft.network.sendToServer(new SyncSaiyanDataMessage(cap));
	}
	
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
	}
}
