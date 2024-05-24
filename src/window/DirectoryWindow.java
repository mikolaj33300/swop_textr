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

    /**
     * The directory input handler
     */
    private DirectoryInputHandler dih;

    /**
     * The terminal adapter
     */
    private TermiosTerminalAdapter adapter;

    /**
     * The view of this DirectoryWindow
     */
    private final View view;

    /**
     * Creates a DirectoryWindow object
     * @param dih the directory input handler
     * @param adapter the terminal adapter
     */
    public DirectoryWindow(DirectoryInputHandler dih, TermiosTerminalAdapter adapter) {
        this.dih = dih;
        this.adapter = adapter;
        this.view = new ScrollbarDecorator(new DirectoryView(adapter, dih.getDirectory()));
    }

    /**
     * Returns the view of this DirectoryWindow
     * @return view
     */
    @Override
    public View getView() {
        return view;
    }

    /**
     * Returns the directory input handler
     * @return dih
     */
    @Override
    public InputHandlingElement getHandler() {
        return this.dih;
    }

    /**
     * A DirectoryWindow cannot be duplicated, thus it returns null
     */
    @Override
    public Window duplicate() throws IOException {
        return null;
    }

    /**
     * Accepts a visitor
     * @param wv the visitor
     */
    @Override
    public void accept(WindowVisitor wv) throws IOException {
        wv.visitDirectoryWindow(this);
    }

    /**
     * Sets a new TermiosTerminalAdapter
     */
    @Override
    public void setTermiosAdapter(TermiosTerminalAdapter newAdapter) {
        this.adapter = newAdapter;
    }

    /**
     * Subscribes this DirectoryWindow to a listener
     * @param openWindowRequestListener
     */
    @Override
    public void subscribeWindow(OpenWindowRequestListener openWindowRequestListener) {
        //This normally won't request to open any windows (not directly)
    }

    /**
     * Since a DirectoryWindow does not have a path, it returns null
     * @return  null
     */
    @Override
    public String getPath() {
        return null;
    }

}
