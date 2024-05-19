package controller;

import controller.adapter.TermiosTerminalAdapter;
import inputhandler.InputHandlingElement;
import inputhandler.FileBufferInputHandler;
import ui.View;
import ui.FileBufferView;
import files.BufferCursorContext;

class Window {
    public final View view;
    public final InputHandlingElement handler;

    public Window(View v, InputHandlingElement h) {
        this.view = v;
        this.handler = h;
    }

    public String getPath() {
	if (this.handler instanceof FileBufferInputHandler fh) {
	    return fh.getPath();
	}
	return "";
    }

    public Window duplicate(TermiosTerminalAdapter termiosTerminalAdapter) {
        if (this.handler instanceof FileBufferInputHandler fbh) {
            BufferCursorContext dupedContext = new BufferCursorContext(fbh.getFileBufferContextTransparent());
            FileBufferView newView = new FileBufferView(dupedContext, termiosTerminalAdapter);
            return new Window(newView, new FileBufferInputHandler(dupedContext));
	}
	return null;
    }
}
