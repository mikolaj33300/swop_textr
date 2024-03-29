package controller;

import Java.String;
import Java.util.ArrayList;

import layouttree.Layout;
import inputhandler.InputHandler;

class window {
	public final View view;
	public final InputHandler handler;
}

class ControllerFacade {
	ArrayList<window> windows;
	Layout rootLayout;

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link LayoutLeaf} will be assigned according to arguments given by {@link TextR#main(String[])}
     * @throws IOException when the path is invalid
     */
	public void ControllerFacade(String[] paths) throws IOException {
		ArrayList<Layout> leaves = new ArrayList<LayoutLeaf>(paths.length);

		for (int i = 0; i < paths.length; i++) {
		    Path checkPath = Paths.get(paths[i]); 
		    if (!Files.exists(checkPath)) 
			    this.activeUseCaseController = new FileErrorPopupController(this);
		    else
			    leaves.add(new LayoutLeaf(path, false, ));

		    this.windows.add(new window(new view(paths[i]), new inputhandler(paths[i])));
		    leaves.add(new LayoutLeaf(paths[i], i==0, windows[i].view.getHash()));
		}

		if(leaves.size() == 1)
		    this.rootLayout = leaves.get(0);
		else
		    this.rootLayout = new VerticalLayoutNode(leaves);
	}
}
