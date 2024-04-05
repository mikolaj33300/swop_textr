package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import inputhandler.FileBufferInputHandler;
import layouttree.Layout;
import inputhandler.InputHandler;
import layouttree.LayoutLeaf;
import layouttree.VerticalLayoutNode;
import ui.FileBufferView;
import ui.View;

class Window {
	public final View view;
	public final InputHandler handler;

	public Window(View v, InputHandler h){
		this.view = v;
		this.handler = h;
	}
}

class ControllerFacade {
	ArrayList<Window> windows;
	Layout rootLayout;

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link Layout} will be assigned according to arguments given by {@link TextR#main(String[])}
     * @throws IOException when the path is invalid
     */
	public void ControllerFacade(String[] paths) throws IOException {
		ArrayList<Layout> leaves = new ArrayList<Layout>(paths.length);

		for (int i = 0; i < paths.length; i++) {
		    String checkPath = paths[i];
			FileBufferInputHandler openedFileHandler = new FileBufferInputHandler(checkPath);
		    this.windows.add(new Window(new FileBufferView(openedFileHandler.getFileBufferContextTransparent()), openedFileHandler));
		    if (!Files.exists(Path.of(checkPath)))
				//TODO: exception needs to be caught on level above
				throw new IOException("Path not found");
		    else
				leaves.add(new LayoutLeaf(windows.get(i).view.hashCode()));
		}

		if(leaves.size() == 1)
		    this.rootLayout = leaves.get(0);
		else
		    this.rootLayout = new VerticalLayoutNode(leaves);
	}
}
