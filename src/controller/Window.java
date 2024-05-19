package controller;

import inputhandler.InputHandlingElement;
import ui.View;

class Window {
    //TODO turn this into a decorator of view and handler: so this also gets handle/updateCoords methods and delegates it to the view/handler
    public View view;
    public InputHandlingElement handler;

    public Window(View v, InputHandlingElement h) {
        this.view = v;
        this.handler = h;
    }

    Window() {

    }

    void setView(View v) {
        this.view = v;
    }

    void setHandler(InputHandlingElement h) {
        this.handler = h;
    }
}
