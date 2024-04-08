package controller;

import files.PathNotFoundException;
import io.github.btj.termios.Terminal;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import inputhandler.FileBufferInputHandler;
import layouttree.Layout;
import inputhandler.InputHandler;
import layouttree.LayoutLeaf;
import layouttree.ROT_DIRECTION;
import layouttree.DIRECTION;
import layouttree.VerticalLayoutNode;
import ui.*;
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
    UICoords screenUICoords;

    /**
     * Creates a ControllerFacade object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link Layout} will be assigned according to arguments given by {@link TextR#main(String[])}
     * @throws IOException when the path is invalid
     */
	public ControllerFacade(String[] paths) throws PathNotFoundException, IOException {
		ArrayList<Layout> leaves = new ArrayList<Layout>(paths.length);
		for (int i = 0; i < paths.length; i++) {
		    String checkPath = paths[i];
			if (!Files.exists(Path.of(checkPath)))
				//TODO: exception needs to be caught on level above
				throw new PathNotFoundException();
		    else {
                FileBufferInputHandler openedFileHandler = new FileBufferInputHandler(checkPath);
                this.windows.add(new Window(new FileBufferView(openedFileHandler.getFileBufferContextTransparent()), openedFileHandler));
                leaves.add(new LayoutLeaf(windows.get(i).view.hashCode()));
            }
		}

		if(leaves.size() == 1)
		    this.rootLayout = leaves.get(0);
		else
		    this.rootLayout = new VerticalLayoutNode(leaves);
	}

  public void renderContent() throws IOException {
    for(int i = 0; i< windows.size(); i++){
        windows.get(i).view.render();
    };
  }

  public void renderCursor() throws IOException {
    windows.get(active).view.renderCursor();
  }

  public int closeActive(){
        int status = windows.get(active).handler.close();
        if(status == 0){
            //safe to do a force close since 0 == succesful close eg. clean buffer
            return forceCloseActive();
        } else {
            return 1;
        }
  }

  public int forceCloseActive() {
    int oldHashCode = windows.get(active).view.hashCode();
    int newHashCode = rootLayout.getNeighborsContainedHash(DIRECTION.RIGHT, windows.get(active).view.hashCode());
    if(newHashCode == oldHashCode){
        newHashCode = rootLayout.getNeighborsContainedHash(DIRECTION.LEFT, windows.get(active).view.hashCode());
        if(oldHashCode == newHashCode){
            //no left or right neighbor to focus
            rootLayout = null;
            return 2;
        }
    }
    rootLayout = this.rootLayout.delete(windows.get(active).view.hashCode());
    windows.remove(active);
    int newActive = -1;
    for(int i = 0; i<windows.size(); i++){
        if(windows.get(i).view.hashCode() == newHashCode){
            newActive = i;
        }
    }
    active = newActive;
    if(active == -1){
        throw new RuntimeException("Layout and collection of views inconsistent!");
    }

    return 0;
  }

  public void passToActive(byte b) throws IOException {
    this.windows.get(active).handler.Input(b);
  }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     */
    public void moveFocus(DIRECTION dir) {

        int newActive = this.rootLayout.getNeighborsContainedHash(dir, this.windows.get(active).view.hashCode());
        for (int i = 0; i < this.windows.size(); i++){
          if (this.windows.get(i).view.hashCode() == newActive){
            this.active = i;
            break;
          }
        }

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
    void rotateLayout(ROT_DIRECTION orientation) throws IOException {
        rootLayout = rootLayout.rotateRelationshipNeighbor(orientation, this.windows.get(active).view.hashCode());
        updateViewCoordinates();
    }

    private void updateViewCoordinates() {
        HashMap<int, Rectangle> coordsMap = rootLayout.getCoordsList(new Rectangle(0, 0, 1, 1));
        for(Window w : windows){
            w.view.setScaledCoords(coordsMap.get(w.view.hashCode()));
        }
    }
}
