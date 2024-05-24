package ui;

import ioadapter.TermiosTerminalAdapter;
import io.github.btj.termios.Terminal;
import util.Coords;
import util.Rectangle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class View {

    /**
     * @return our cursor Column
     */
    public abstract int getFocusedCol();

    /**
     * @return our cursor Line
     */
    public abstract int getFocusedLine();

    /**
     * @return our the height of all lines
     */
    public abstract int getTotalContentHeight();
    /**
     * The adapter used for interacting with termios
     */
    protected TermiosTerminalAdapter termiosTerminalAdapter;


    /**
     * the coordintes to print on
     */
    Coords uiCoordsReal;

    /**
     * The total width of the terminal
     */
    static int terminalWidth;

    /**
     * The total height of the terminal
     */
    static int terminalHeight;

    /**
     * @return our rendering object
     */
    public TermiosTerminalAdapter getTermiosTerminalAdapter() {
        return termiosTerminalAdapter;
    }

    /**
     * create this with a termiosTerminalAdapter
     * @param termiosTerminalAdapter a object we can render trough
     */
    public View(TermiosTerminalAdapter termiosTerminalAdapter) {
        this.termiosTerminalAdapter = termiosTerminalAdapter;
    }

    /**
     * Clears the content on the terminal window
     * Used to prevent text ghosting on the screen
     */
    public void clearContent() throws IOException {
/*        retrieveDimensions();
        for(int i = startY; i<startY+height; i++){
            for(int j = startX; j<startX+width; j++){
                Terminal.printText(i+1, j+1, " ");
            }
        }*/
        Terminal.clearScreen();
    }

    /**
     * Render all the elements on the this view
     * @param activeHash the hash of the active window
     */
    public abstract void render(int activeHash) throws IOException;

    /**
     * Renders the cursor on the current view
     */
    public abstract void renderCursor() throws IOException;

    /**
     * Checks whether this View and the given object are the same type and have the same contents
     * @param o the object to compare this to
     */
    public abstract boolean equals(Object o);


    /**
     * @return the length length of 
     * @param focusedLine
     */
    protected abstract int getLineLength(int focusedLine);

    /**
     * update our uiCoordsReal with rectangle
     * @param rectangle the coordinates out of our tree
     */
    public void setRealCoords(Rectangle rectangle) {
        this.uiCoordsReal = new Coords((int) Math.floor(rectangle.startX), (int) Math.floor(rectangle.startY), (int) Math.floor(rectangle.width), (int) Math.floor(rectangle.height));
    }

    /**
     * @return our coords
     */
    public Coords getRealCoords() {
        return uiCoordsReal;
    }

    /**
     * update our termiosTerminalAdapter to 
     * @param adapter
     */
    public void setTermiosTerminalAdapter(TermiosTerminalAdapter adapter){
        this.termiosTerminalAdapter = adapter;
    };

/*    public static void write(String path, String text) {
        try {
            // Overwrite file test.txt
            FileWriter writer = new FileWriter(new File("test2.txt"), true);
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    /**
     * Fills an amount of space with a string
     */
    void fill(int x, int y, int w, int h, String s){
      for (int i = 0; i < h; i++){
        termiosTerminalAdapter.printText(y+i, x, s.repeat(w));
      }
    }
}
