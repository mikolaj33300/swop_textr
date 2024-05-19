package controller;

import controller.adapter.TermiosTerminalAdapter;
import exception.PathNotFoundException;
import inputhandler.FileBufferInputHandler;
import inputhandler.InputHandlingElement;
import ui.FileBufferView;
import ui.View;

import java.io.IOException;

public class FileBufferWindow extends Window{
    public FileBufferWindow(String path, byte[] lineSeparatorArg, TermiosTerminalAdapter adapter) throws PathNotFoundException, IOException {
        super();
        FileBufferInputHandler openedFileHandler;
        try {
            openedFileHandler = new FileBufferInputHandler(path, lineSeparatorArg);
        } catch (PathNotFoundException | IOException e) {
            throw e;
        }
        setView(new FileBufferView(openedFileHandler.getFileBufferContextTransparent(), adapter));
        setHandler(openedFileHandler);

    }
}
