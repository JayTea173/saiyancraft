package com.jaynopp.saiyancraft.gui;

import com.jaynopp.saiyancraft.IAction;

public class ScrollViewItem {
	public Object object;
	public String displayText;
	public int hoverTime;
	public IAction<ScrollViewItem, ScrollViewItem> onClickDown, onClickUp, onEnter, onExit;
	
	public ScrollViewItem(Object object, String displayText){
		this.object = object;
		this.displayText = displayText;
		this.hoverTime = 0;
	}
}
