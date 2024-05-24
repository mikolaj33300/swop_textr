package inputhandler;

import directory.Directory;
import directory.directorytree.FileSystemEntry;
import files.OpenFileOnPathRequestListener;
import util.RenderIndicator;

import java.io.IOException;

public class DirectoryInputHandler extends InputHandlingElement {

    private final Directory directory;

    public DirectoryInputHandler(Directory dir) {
        this.directory = dir;
    }

    public DirectoryInputHandler(FileSystemEntry fe) {
        this.directory = new Directory(fe);
    }

    public DirectoryInputHandler(OpenFileOnPathRequestListener listener) {
        this.directory = new Directory(listener);
    }

    @Override
    public int forcedClose() {
        directory.forcedClose();
        return 0;
    }

    @Override
    public void save() {
        return;
    }

    @Override
    public RenderIndicator input(byte b) throws IOException {
        return RenderIndicator.NONE;
    }

    @Override
    public boolean isSafeToClose() {
        return true;
    }

    @Override
    public RenderIndicator handleArrowRight() {
        return RenderIndicator.NONE;
    }

    @Override
    public RenderIndicator handleArrowLeft() {
        return RenderIndicator.NONE;
    }

    @Override
    public RenderIndicator handleArrowDown() {
        this.directory.increaseFocused();
        return RenderIndicator.WINDOW;
    }

    @Override
    public RenderIndicator handleArrowUp() {
        this.directory.decreaseFocused();
        return RenderIndicator.WINDOW;
    }

    @Override
    public RenderIndicator handleSeparator() throws IOException {
        if (this.directory.selectEntry() == null) {
            //TODO: Request closing this window through listener
        }
        return RenderIndicator.WINDOW;
    }

    public Directory getDirectory() {
        return this.directory;
    }

    public void accept(InputHandlerVisitor v) {
        v.visitDirectoryInputHandler(this);
    }

    public void subscribeCloseEvents(Runnable closeEventListener) {
        directory.subscribeCloseEvents(closeEventListener);
    }
}
