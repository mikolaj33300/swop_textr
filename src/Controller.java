import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.LayoutNode;
import util.TerminalReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    /**
     * The beginning layout when the application is started with paths.
     */
    private Layout rootLayout;
    private TerminalReader terminalReader;
    private int width, height;

    /**
     * Creates a controller object.
     */
    public Controller(String[] args) {
        this.rootLayout = getRootLayout(args);
        this.terminalReader = new TerminalReader();
    }

    /**
     * Computes the root layout
     */
    private Layout getRootLayout(String[] args) {
        ArrayList<Layout> leaves = new ArrayList<>();
        for(String s : args) {
            leaves.add(new LayoutLeaf(new FileBuffer(s), false));
        }
        return new LayoutNode(Layout.Orientation.HORIZONTAL, leaves);
    }

    /**
     * A beautiful start for a beautiful project
     */
    public static void main(String[] args) throws IOException {
        Controller btj = new Controller(args);
        btj.loop();
    }


    /**
     * Contains the main input loop
     */
    public void loop() throws IOException {

        // Start program.
        Terminal.clearScreen();
        Terminal.enterRawInputMode();

        this.terminalReader.recalculateDimensions();

        // Main loop
        for ( ; ; ) {

            int c = Terminal.readByte();

            switch(c) {
                    // Control + S
                case 19:
                    saveBuffer();

                    break;
                    // Control + P
                case 16:
                    moveFocus(Layout.DIRECTION.LEFT);

                    break;

                    // Control + N
                case 14:
                    moveFocus(Layout.DIRECTION.RIGHT);
                    break;

                default:
                    //this.rootLayout.getActive();
                    enterText(Terminal.readByte());
                    render();

            }

            // Checking if user changed size of window


        }

    }

    /**
     * Renders the layout
     */
    public void render() {
        this.rootLayout.render(/*this.terminalReader.getWidth(), this.terminalReader.getHeight()*/);
    }

    /**
     * After entering this combination
     */
    public void saveBuffer() {

    }

    public void enterText(int c) {

    }

    public void moveFocus(Layout.DIRECTION dir) {
        this.rootLayout.moveFocus(dir);
    }

    public void rotateRelationshipNeighbour() {

    }

    private void print(String... s) {
        for(String si : s) {
            System.out.println(si);
        }
    }

}