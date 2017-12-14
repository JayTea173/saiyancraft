package com.jaynopp.saiyancraft;

public interface IAction<I, O> {
	public O Invoke(I input);
}
