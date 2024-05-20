package controller;

import controller.adapter.TermiosTerminalAdapter;
import exception.PathNotFoundException;
import files.BufferCursorContext;
import inputhandler.FileBufferInputHandler;
import inputhandler.InputHandlingElement;
import ui.FileBufferView;
import ui.ScrollbarDecorator;
import ui.View;

import java.io.IOException;

public class FileBufferWindow extends Window{
    private FileBufferInputHandler fileBufferInputHandler;
    private View view;
    public FileBufferWindow(String path, byte[] lineSeparatorArg, TermiosTerminalAdapter adapter) throws PathNotFoundException, IOException {
        super();
        FileBufferInputHandler openedFileHandler;
        try {
            openedFileHandler = new FileBufferInputHandler(path, lineSeparatorArg);
        } catch (PathNotFoundException | IOException e) {
            throw e;
        }
        this.view = (new ScrollbarDecorator(new FileBufferView(openedFileHandler.getFileBufferContextTransparent(), adapter)));
        this.fileBufferInputHandler = (openedFileHandler);

    }

    public FileBufferWindow(FileBufferView newView, FileBufferInputHandler fileBufferInputHandler) {
        this.view = newView;
        this.fileBufferInputHandler = fileBufferInputHandler;
    }


    @Override
    public View getView() {
        return this.view;
    }

    @Override
    public InputHandlingElement getHandler() {
        return this.fileBufferInputHandler;
    }

    @Override
    public FileBufferWindow duplicate() {
        BufferCursorContext dupedContext = new BufferCursorContext(this.fileBufferInputHandler.getFileBufferContextTransparent());
        FileBufferView newView = new FileBufferView(dupedContext, this.view.getTermiosTerminalAdapter());

        FileBufferWindow windowToAdd = new FileBufferWindow(newView, new FileBufferInputHandler(dupedContext));
        windowToAdd.view = new ScrollbarDecorator(windowToAdd.view);
        return windowToAdd;
    }

    @Override
    public void accept(WindowVisitor v) throws IOException {
        v.visitFileWindow(this);
    }

    @Override
    public void setTermiosAdapter(TermiosTerminalAdapter newAdapter) {
        this.view.setTermiosTerminalAdapter(newAdapter);
    }
}
