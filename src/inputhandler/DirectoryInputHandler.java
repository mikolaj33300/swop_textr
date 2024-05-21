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
        this.directory.selectEntry();
	return RenderIndicator.WINDOW;
    }

}
