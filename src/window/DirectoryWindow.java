package window;

import directory.Directory;
import directory.directorytree.FileSystemEntry;
import inputhandler.DirectoryInputHandler;
import inputhandler.InputHandlingElement;
import ioadapter.TermiosTerminalAdapter;
import ui.DirectoryView;
import ui.View;

import java.io.IOException;

public class DirectoryWindow extends Window {

    private final FileSystemEntry entry;
    private final TermiosTerminalAdapter adapter;
    private final Directory dir;

    public DirectoryWindow(FileSystemEntry entry, TermiosTerminalAdapter adapter) {
        this.entry = entry;
        this.adapter = adapter;
        this.dir = new Directory(entry);
    }

    @Override
    public View getView() {
        return new DirectoryView(adapter, dir);
    }

    @Override
    public InputHandlingElement getHandler() {
        return new DirectoryInputHandler(dir);
    }

    @Override
    public Window duplicate() throws IOException {
        return null;
    }

    @Override
    public void accept(WindowVisitor wv) throws IOException {

    }

    @Override
    public void setTermiosAdapter(TermiosTerminalAdapter newAdapter) {

    }

}
