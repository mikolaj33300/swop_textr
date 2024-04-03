package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.VerticalLayoutNode;
import inputhandler.InputHandler;
import inputhandler.FileBufferInput;
import ui.View;
import ui.Render;
import ui.InsertionPoint;

class window {
	public final InsertionPoint point;
	public final InputHandler handler;
}

class ControllerFacade {
	private ArrayList<window> windows;
	private Layout rootLayout;
	private Render render;
	private int active;

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link LayoutLeaf} will be assigned according to arguments given by {@link TextR#main(String[])}
     * @throws IOException when the path is invalid
     */
	public void ControllerFacade(String[] paths) throws IOException {
		ArrayList<Layout> leaves = new ArrayList<LayoutLeaf>(paths.length);

		for (int i = 0; i < paths.length; i++) {
		    this.active = 0;
		    Path checkPath = Paths.get(paths[i]); 

		    this.windows.add(new window(new view(paths[i]), new FileBufferinput(paths[i])));
		    if (!Files.exists(checkPath)) 
			    this.activeUseCaseController = new FileErrorPopupController(this);
		    else {
          LayoutLeaf tmp = new LayoutLeaf(windows.get(i).view.getHash());
          leaves.add(tmp);
        }

		}

		if(leaves.size() == 1)
		    this.rootLayout = leaves.get(0);
		else
		    this.rootLayout = new VerticalLayoutNode(leaves);
	}
}
