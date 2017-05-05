package com.jaynopp.saiyancraft.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.jaynopp.saiyancraft.IAction;
import com.jaynopp.saiyancraft.SaiyanCraft;
import com.jaynopp.saiyancraft.capabilities.saiyandata.DefaultSaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.ISaiyanData;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SaiyanDataProvider;
import com.jaynopp.saiyancraft.capabilities.saiyandata.SyncSaiyanDataMessage;
import com.jaynopp.saiyancraft.init.ModLearnables;
import com.jaynopp.saiyancraft.player.ILearnable;
import com.jaynopp.saiyancraft.player.SaiyanPlayer;
import com.jaynopp.saiyancraft.player.moves.ISaiyanMove;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class SaiyanPlayerStatusGui extends GuiScreen {

	final ResourceLocation bgTexture = new ResourceLocation(SaiyanCraft.modId, "textures/gui/saiyangui.png");
	final ResourceLocation plTexture = new ResourceLocation(SaiyanCraft.modId, "textures/gui/redscouter.png");
	final ResourceLocation whiteTexture = new ResourceLocation(SaiyanCraft.modId, "textures/common/white.png");
	
	int guiWidth = 256;
	int guiHeight = 192;
	
	GuiButton increaseVitalityButton;
	GuiButton increaseEnduranceButton;
	GuiButton increaseSkillButton;
	GuiButton increaseStrengthButton;
	GuiButton increaseAgilityButton;
	GuiButton increaseSpiritButton;
	protected List<GuiButton> tabButtons;
	final int bIncreaseVitality = 1;
	final int bIncreaseEndurance = 2;
	final int bIncreaseSkill = 3;
	final int bIncreaseStrength = 4;
	final int bIncreaseAgility = 5;
	final int bIncreaseSpirit = 6;
	final int bTabsStart = 7;
	int mouseX, mouseY;
	ScrollView moveList;
	
	protected int centerX = (width / 2)- guiWidth / 2;
	protected int centerY = (height / 2) - guiHeight / 2;
	
	public enum Tab {
		STATS(0),
		MOVES(1);
		
		private final int value;
	    private Tab(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	}
	public static String[] tabNames = {"Stats", "Moves"};
	
	public Tab tab = Tab.STATS;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		drawDefaultBackground();
		switch (tab){
		case STATS:
			DrawStatsGUI();
			break;
		case MOVES:
			DrawMovesGUI();
		default:
			break;
		
		}
		DrawCurrentTab();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	protected void DrawMovesGUI(){
		Minecraft.getMinecraft().renderEngine.bindTexture(bgTexture);
		centerX = (width / 2)- guiWidth / 2;
		centerY = (height / 2) - guiHeight / 2;
		int statOffsetX = Minecraft.getMinecraft().player.isCreative() ? 24 : 4;
		drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
		
		moveList.Draw(Minecraft.getMinecraft().renderEngine, this, mouseX, mouseY);
	}
	
	protected void DrawBGTexture(){

	}
	
	protected void DrawTabs(){
		
	}
	
	protected void DrawCurrentTab(){
		//AddTabButton(new GuiButton(bTabsStart + tabButtons.size(), centerX + 36 * i, centerY, 36, 10, tabNames[i]));
		
		
		centerX = (width / 2)- guiWidth / 2;
		centerY = (height / 2) - guiHeight / 2;
		Minecraft.getMinecraft().renderEngine.bindTexture(whiteTexture);
		GL11.glPushMatrix();
		GL11.glColor3f(0f, 1f, .5f);
		drawTexturedModalRect(centerX + tab.getValue() * 36, centerY + 10, 0, 0, 36, 2);
		GL11.glPopMatrix();
		GL11.glColor3f(1f, 1f, 1f);
		
		

	}
	
	protected void DrawStatsGUI(){
		Minecraft.getMinecraft().renderEngine.bindTexture(bgTexture);
		centerX = (width / 2)- guiWidth / 2;
		centerY = (height / 2) - guiHeight / 2;
		int statOffsetX = Minecraft.getMinecraft().player.isCreative() ? 24 : 4;
		drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
		Minecraft.getMinecraft().renderEngine.bindTexture(plTexture);
		drawModalRectWithCustomSizedTexture(centerX-8, centerY + 14, 0, 0, 24, 24, 24f, 24f);
		//Minecraft.getMinecraft().renderEngine.bindTexture(plTexture);
		//drawTexturedModalRect(centerX + 4, centerY + 16, 0, 0, 32, 32);
		fontRendererObj.FONT_HEIGHT = 18;
		String playerName = Minecraft.getMinecraft().player.getName();
		drawString(fontRendererObj, playerName, (width / 2) - fontRendererObj.getStringWidth(playerName) / 2, centerY + 12, 0xffffff);
		
		ISaiyanData data = Minecraft.getMinecraft().player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		
		String statStr = ISaiyanData.PLToString(data.GetPowerLevel(), isShiftKeyDown());
		fontRendererObj.drawString(statStr, centerX + 74, centerY + 22, 0x000000);
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
		
		/*ISaiyanBattler battler = Minecraft.getMinecraft().player.getCapability(SaiyanBattlerProvider.BATTLER_CAP, null);
		List<ISaiyanMove> moves = battler.GetMoves();
		for (int i = 0; i <moves.size(); i++){
			ISaiyanMove move = moves.get(i);
			String moveStr = move.GetType().toString() + " (" + move.GetCooldown() + ", " + move.GetStunTime() + ", " + move.GetKnockback() + ")";
			fontRendererObj.drawString(moveStr, centerX + statOffsetX + 120, centerY + 32 + i * 10, 0xee0000);
		}*/
	}
	
	protected void AddTabButton(GuiButton button){
		buttonList.add(button);
		tabButtons.add(button);
		 
	}
	
	protected void UpdateTabbedButtons(){
		switch (tab){
			case STATS:
				if (buttonList.contains(moveList.scrollButton))
					buttonList.remove(moveList.scrollButton);
				break;
			case MOVES:
				if (!buttonList.contains(moveList.scrollButton))
					buttonList.add(moveList.scrollButton);
				break;
			default:
				break;
		
		}
	
	}
	
	protected void SetTab(Tab tab){
		this.tab = tab;
		UpdateTabbedButtons();
	
	}
	
	protected void GetAllLearnables() {
		for (ILearnable learnable : ModLearnables.learnables){
			
		
		}
	}
	
	protected void GetAllMoves() {
		moveList.Clear();
		for (ISaiyanMove move : SaiyanPlayer.local.comboManager.moves){
			ScrollViewItem item = moveList.AddItem(move, move.toString());
			item.onClickDown = new IAction<ScrollViewItem, ScrollViewItem>(){

				public ScrollViewItem Invoke(ScrollViewItem input) {
					System.out.println("CLICK DOWN!");
					return input;
				}
				
			};
		}
	}
	
	@Override
	public void initGui(){
		centerX = (width / 2)- guiWidth / 2;
		centerY = (height / 2) - guiHeight / 2;
		
		buttonList.clear();
		tabButtons = new ArrayList<GuiButton>();
		//Tabs
		for (int i = 0; i < tabNames.length; i++){
			AddTabButton(new GuiButton(bTabsStart + tabButtons.size(), centerX + 36 * i, centerY, 36, 10, tabNames[i]));
		}
		moveList = new ScrollView(centerX, centerY + 12, guiWidth, guiHeight - 12, bTabsStart + tabButtons.size());
		GetAllMoves();
		//buttonList.add(moveList.RegisterScrollButton());
		moveList.RegisterScrollButton();
		GetAllLearnables();
		if (Minecraft.getMinecraft().player.isCreative()){
			CreateStatDebugButtons();
		}
		super.initGui();
	}
	
	protected void CreateStatDebugButtons(){
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
	
	@Override
	protected void actionPerformed(GuiButton button){
		ISaiyanData cap = mc.player.getCapability(SaiyanDataProvider.POWERLEVEL_CAP, null);
		if (cap == null){
			System.err.println("SaiyanGUI trying to get saiyandata for local player, but is null!");
			return;
		}
		float inc = isShiftKeyDown() ? 10f : isCtrlKeyDown() ? 100f : 1f;
		
		if (button.id >= bTabsStart && button.id < bTabsStart + tabNames.length){
			System.out.println("Clicked tab: " + (button.id - bTabsStart));
			SetTab(Tab.values()[button.id - bTabsStart]);
		} else {

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
			default:
				HandleElementClicked(button);
				break;
			}
		}
		DefaultSaiyanData.UpdateStats(mc.player);
		SaiyanCraft.network.sendToServer(new SyncSaiyanDataMessage(cap));
	}
	
	protected void HandleElementClicked(GuiButton button){

	}
	
	@Override
	public void onGuiClosed(){
		super.onGuiClosed();
	}
}
