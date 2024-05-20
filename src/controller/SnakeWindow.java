package controller;

import controller.adapter.TermiosTerminalAdapter;
import exception.PathNotFoundException;
import inputhandler.InputHandlingElement;
import inputhandler.SnakeInputHandler;
import ui.SnakeView;
import ui.View;

import java.io.IOException;

public class SnakeWindow extends Window{
    private SnakeView snakeView;
    private SnakeInputHandler snakeInputHandler;
    public SnakeWindow(int width, int height, TermiosTerminalAdapter termiosTerminalAdapter) throws PathNotFoundException, IOException {
        SnakeInputHandler handler = new SnakeInputHandler(width, height);
        SnakeView view = new SnakeView(handler.getSnakeGame(), termiosTerminalAdapter);
        this.snakeView = (view);
        this.snakeInputHandler = (handler);
    }

    @Override
    public View getView() {
        return this.snakeView;
    }

    @Override
    public InputHandlingElement getHandler() {
        return this.snakeInputHandler;
    }

    @Override
    public SnakeWindow duplicate() {
        return null;
    }

    @Override
    public void accept(WindowVisitor v){
        v.visitSnakeWindow(this);
    }
}
