package inputhandler;

import directory.Directory;
import directory.directorytree.FileSystemEntry;
import util.RenderIndicator;

import java.io.IOException;

public class DirectoryInputHandler extends InputHandlingElement {

    private final Directory directory;

    public DirectoryInputHandler(FileSystemEntry entry) {
        this.directory = new Directory(entry);
    }

    @Override
    public int forcedClose() {
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
    public void handleArrowRight() {
        return;
    }

    @Override
    public void handleArrowLeft() {
        return;
    }

    @Override
    public void handleArrowDown() {
        this.directory.increaseFocused();
    }

    @Override
    public void handleArrowUp() {
        this.directory.decreaseFocused();
    }

    @Override
    public void handleSeparator() throws IOException {
        this.directory.selectEntry();
    }

}
