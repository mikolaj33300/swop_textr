package inputhandler;

import directory.Directory;
import directory.directorytree.FileSystemEntry;
import files.OpenFileOnPathRequestListener;
import util.RenderIndicator;

import java.io.IOException;

public class DirectoryInputHandler extends InputHandlingElement {

  /**
   * the directory this points towards
   */
    private final Directory directory;

    /**
     * make a new instance of this with a Directory
     */
    public DirectoryInputHandler(Directory dir) {
        this.directory = dir;
    }

    /**
     * make a new instance of this with a FileSystemEntry
     */
    public DirectoryInputHandler(FileSystemEntry fe) {
        this.directory = new Directory(fe);
    }


    public DirectoryInputHandler(OpenFileOnPathRequestListener listener) {
        this.directory = new Directory(listener);
    }

        
    /**
     * it can be closed at any time 
     */
    @Override
    public int forcedClose() {
        directory.forcedClose();
        return 0;
    }

    /**
     * You can't save a Directory
     */
    @Override
    public void save() {
        return;
    }

    /**
     * we have no input to be handled
     * @return no rerender needed since nothing changed
     */
    @Override
    public RenderIndicator input(byte b) throws IOException {
        return RenderIndicator.NONE;
    }

    /**
     * since there is nothing to be handled, we can close at any time
     * @return true
     */
    @Override
    public boolean isSafeToClose() {
        return true;
    }

    /**
     * we don't need to be able to move the cursor right
     * @return no refresh
     */
    @Override
    public RenderIndicator handleArrowRight() {
        return RenderIndicator.NONE;
    }

    /**
     * we don't need to be able to move the cursor left 
     * @return no refresh
     */
    @Override
    public RenderIndicator handleArrowLeft() {
        return RenderIndicator.NONE;
    }

    /**
     * we need to go one item down
     * @return window refresh
     */
    @Override
    public RenderIndicator handleArrowDown() {
        this.directory.increaseFocused();
        return RenderIndicator.WINDOW;
    }

    /**
     * we need to go one item up
     * @return a window refresh
     */
    @Override
    public RenderIndicator handleArrowUp() {
        this.directory.decreaseFocused();
        return RenderIndicator.WINDOW;
    }

    /**
     * when we get an enter we need to open the file under the cursor
     * @return a window refresh
     */
    @Override
    public RenderIndicator handleSeparator() throws IOException {
        if (this.directory.selectEntry() == null) {
            //TODO: Request closing this window through listener
        }
        return RenderIndicator.WINDOW;
    }

    /**
     * @return the folder this points to
     */
    public Directory getDirectory() {
        return this.directory;
    }

    /**
     * execute the visitors code on this
     */
    public void accept(InputHandlerVisitor v) {
        v.visitDirectoryInputHandler(this);
    }

    public void subscribeCloseEvents(Runnable closeEventListener) {
        directory.subscribeCloseEvents(closeEventListener);
    }
}
