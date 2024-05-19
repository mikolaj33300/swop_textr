package controller;

import controller.adapter.TermiosTerminalAdapter;
import exception.PathNotFoundException;
import files.BufferCursorContext;
import inputhandler.FileBufferInputHandler;
import inputhandler.InputHandlingElement;
import ui.FileBufferView;
import ui.View;

import java.io.IOException;

public class FileBufferWindow extends Window{
    private FileBufferInputHandler fileBufferInputHandler;
    private FileBufferView fileBufferView;
    public FileBufferWindow(String path, byte[] lineSeparatorArg, TermiosTerminalAdapter adapter) throws PathNotFoundException, IOException {
        super();
        FileBufferInputHandler openedFileHandler;
        try {
            openedFileHandler = new FileBufferInputHandler(path, lineSeparatorArg);
        } catch (PathNotFoundException | IOException e) {
            throw e;
        }
        this.fileBufferView = (new FileBufferView(openedFileHandler.getFileBufferContextTransparent(), adapter));
        this.fileBufferInputHandler = (openedFileHandler);

    }

    public FileBufferWindow(FileBufferView newView, FileBufferInputHandler fileBufferInputHandler) {
        this.fileBufferView = newView;
        this.fileBufferInputHandler = fileBufferInputHandler;
        super.termiosTerminalAdapter = newView.getTermiosTerminalAdapter();
    }


    @Override
    public View getView() {
        return this.fileBufferView;
    }

    @Override
    public InputHandlingElement getHandler() {
        return this.fileBufferInputHandler;
    }

    @Override
    public Window duplicate() throws IOException {
        BufferCursorContext dupedContext = new BufferCursorContext(this.fileBufferInputHandler.getFileBufferContextTransparent());
        FileBufferView newView = new FileBufferView(dupedContext, this.termiosTerminalAdapter);
        Window windowToAdd = new FileBufferWindow(newView, new FileBufferInputHandler(dupedContext));
        return windowToAdd;
    }
}
