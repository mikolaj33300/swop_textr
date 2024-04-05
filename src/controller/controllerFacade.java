package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.ROT_DIRECTION;
import layouttree.DIRECTION;
import layouttree.VerticalLayoutNode;
import inputhandler.InputHandler;
import inputhandler.FileBufferInput;
import ui.View;
import ui.FileBufferView;
import ui.Render;
import ui.InsertionPoint;

class Window {
	public final InsertionPoint point;
	public final InputHandler handler;

  public Window(InsertionPoint pt, InputHandler handler) {
    this.point = pt;
    this.handler = handler;
  }
}

class ControllerFacade {
	private ArrayList<Window> windows;
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
		ArrayList<Layout> leaves = new ArrayList<Layout>(paths.length);

		for (int i = 0; i < paths.length; i++) {
		    this.active = 0;
		    Path checkPath = Paths.get(paths[i]); 

		    Window win = new Window(new InsertionPoint(0, 0), new FileBufferInput());
        this.windows.add(win);
		    if (!Files.exists(checkPath)) 
			    //this.activeUseCaseController = new FileErrorPopupController(this); => throw error
          throw new IOException("File does not exist");
		    else {
          LayoutLeaf tmp = new LayoutLeaf(this.render.getHash(i));
          leaves.add(tmp);
        }

		}

		if(leaves.size() == 1)
		    this.rootLayout = leaves.get(0);
		else
		    this.rootLayout = new VerticalLayoutNode(leaves);
	}

  public void renderContent() throws IOException {
    return;
  }

  public void renderCursor() throws IOException {
    return;
  }

  public int forceCloseActive() {
    return 0;
  }

  public void passToActive(byte b) throws IOException {
    this.windows.get(active).handler.Input(b);
  }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     */
    void moveFocus(DIRECTION dir) {
      return;
      /*
        int newActive = this.rootLayout.getHashActiveNeighbour(dir, this.render.getHash(active));
        for (int i = 0; i < this.windows.size(); i++){
          if (this.render.getHash(i) == newActive){
            this.active = i;
            break;
          }
        }
        */
    }

    /**
     * Calls clearContent on the contained {@link ui.FileBufferView}(s).
     */
    public void clearContent() throws IOException{
      return;
    }

    /**
     * Rearranges the Layouts clockwise or counterclockwise, depending on the argument given
     */
    void rotateLayout(ROT_DIRECTION orientation){
        rootLayout.rotateRelationshipNeighbor(orientation, render.getHash(active));
    }
}
