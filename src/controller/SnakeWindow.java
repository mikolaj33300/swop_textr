package controller;

import controller.adapter.TermiosTerminalAdapter;
import exception.PathNotFoundException;
import inputhandler.FileBufferInputHandler;
import inputhandler.SnakeInputHandler;
import ui.FileBufferView;
import ui.SnakeView;

import java.io.IOException;

public class SnakeWindow extends Window{
    public SnakeWindow(int width, int height, TermiosTerminalAdapter termiosTerminalAdapter) throws PathNotFoundException, IOException {
        SnakeInputHandler handler = new SnakeInputHandler(width, height);
        SnakeView view = new SnakeView(handler.getSnakeGame(), termiosTerminalAdapter);
        setView(view);
        setHandler(handler);
    }
}
