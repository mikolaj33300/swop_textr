package window;

import directory.Directory;
import directory.directorytree.FileSystemEntry;
import inputhandler.DirectoryInputHandler;
import inputhandler.InputHandlingElement;
import ioadapter.TermiosTerminalAdapter;
import listeners.OpenWindowRequestListener;
import ui.DirectoryView;
import ui.ScrollbarDecorator;
import ui.View;

import java.io.IOException;

public class DirectoryWindow extends Window {
    private DirectoryInputHandler dih;

    private TermiosTerminalAdapter adapter;
    private final View view;

    public DirectoryWindow(DirectoryInputHandler dih, TermiosTerminalAdapter adapter) {
        this.dih = dih;
        this.adapter = adapter;
        this.view = new ScrollbarDecorator(new DirectoryView(adapter, dih.getDirectory()));
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public InputHandlingElement getHandler() {
        return this.dih;
    }

    @Override
    public Window duplicate() throws IOException {
        return null;
    }

    @Override
    public void accept(WindowVisitor wv) throws IOException {
        wv.visitDirectoryWindow(this);
    }

    @Override
    public void setTermiosAdapter(TermiosTerminalAdapter newAdapter) {
        this.adapter = newAdapter;
    }

    @Override
    public void subscribeWindow(OpenWindowRequestListener openWindowRequestListener) {
        //This normally won't request to open any windows (not directly)
    }

    @Override
    public String getPath() {
        return null;
    }

}
