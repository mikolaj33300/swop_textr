package controller;

import controller.adapter.TermiosTerminalAdapter;
import inputhandler.InputHandlingElement;
import ui.View;

import java.io.IOException;

abstract class Window {
    //TODO turn this into a decorator of view and handler: so this also gets handle/updateCoords methods and delegates it to the view/handler
    TermiosTerminalAdapter termiosTerminalAdapter;
    Window() {

    }

    public abstract View getView();

    public abstract InputHandlingElement getHandler();

    public abstract Window duplicate() throws IOException;
}
