package controller;

import inputhandler.InputHandlingElement;
import ui.View;

class Window {
    public final View view;
    public final InputHandlingElement handler;

    public Window(View v, InputHandlingElement h) {
        this.view = v;
        this.handler = h;
    }
}
