package controller;

import Java.String;
import Java.util.ArrayList;

import layouttree.Layout;
import inputhandler.InputHandler;

class window {
	public final View view;
	public final InputHandler handler;
}

class ControllerFacade {
	ArrayList<window> windows;
	Layout rootLayout;

	public void ControllerFacade(String[] paths){
		this.rootLayout = new Layout();
		for (String path:paths) {
			this.windows.add(new window(new view(path), new inputhandler(path)));
		}
	}
}
