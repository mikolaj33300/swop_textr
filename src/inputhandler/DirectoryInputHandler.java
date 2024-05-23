package inputhandler;

import directory.Directory;
import util.RenderIndicator;

import java.io.IOException;

public class DirectoryInputHandler extends InputHandlingElement {

    private final Directory directory;

    public DirectoryInputHandler(Directory dir) {
        this.directory = dir;
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
        if(this.directory.selectEntry() == null){
            //Request closing this window (
        };
	return RenderIndicator.WINDOW;
    }

}
