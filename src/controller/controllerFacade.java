package controller;

import io.github.btj.termios.Terminal;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import inputhandler.FileBufferInputHandler;
import layouttree.Layout;
import inputhandler.InputHandler;
import layouttree.LayoutLeaf;
import layouttree.ROT_DIRECTION;
import layouttree.DIRECTION;
import layouttree.VerticalLayoutNode;
import ui.FileBufferView;
import ui.View;
import ui.FileBufferView;

class Window {
	public final View view;
	public final InputHandler handler;

	public Window(View v, InputHandler h){
		this.view = v;
		this.handler = h;
	}
}

class ControllerFacade {
	private ArrayList<Window> windows;
	private Layout rootLayout;
	private int active;

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

  public void renderContent() throws IOException {
    return;
  }

  public void renderCursor() throws IOException {
    return;
  }

  public int forceCloseActive() {
    this.windows.remove(active);
    if (this.windows.size() == 0)// return 2 if no more windows left
      return 2;
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
        rootLayout.rotateRelationshipNeighbor(orientation, this.windows.get(active).handler.getHash());
    }
}
