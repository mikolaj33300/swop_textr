package controller;

import inputhandler.InputHandler;
import ui.View;

class Window {
    public final View view;
    public final InputHandler handler;

    public Window(View v, InputHandler h) {
        this.view = v;
        this.handler = h;
    }
}
