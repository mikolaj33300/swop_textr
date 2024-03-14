package core;

import io.github.btj.termios.Terminal;
import layouttree.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Controller {

    /**
     * Holds the line separator for this application
     */
    private static byte[] lineSeparatorArg;

    /**
     * Root layout
     */
    Layout rootLayout;

    /**
     * Creates a controller object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link LayoutLeaf} will be assigned according to arguments given by {@link Controller#main(String[])}
     */
    public Controller(String[] args) {
        this.rootLayout = initRootLayout(args);
    }

    /**
     * A beautiful start for a beautiful project
     */
    public static void main(String[] args) throws IOException, RuntimeException {
        // If no arguments given
        if(args.length == 0 ||
                (
                        // Or --lf || --crlf is given
                        (args[0].equals("--lf") || (args[0].equals("--crlf")))
                        // But amount of args is 1
                        && args.length == 1
                )
        ) { // Then no path is specified, and we cannot open
            throw new RuntimeException("TextR needs at least one specified file");
        }
        setLineSeparatorFromArgs(args);

        Controller btj = new Controller(args);
        if(!args[args.length-1].equals("noterminal"))
            btj.loop();
    }

    /**
     * Creates an instance of {@link Layout} representing a {@link LayoutNode} containing {@link LayoutLeaf} depending on
     * main input arguments.
     */
    private Layout initRootLayout(String[] args) {
        ArrayList<Layout> leaves = new ArrayList<>();
        for(int i = args[0].equals("--lf") || args[0].equals("--crlf") ? 1 : 0 ; i < args.length; i++) {
            Path checkPath = Paths.get(args[i]);
            if (!Files.exists(checkPath))
                System.out.println("Kutzooi");
            else
                leaves.add(new LayoutLeaf(args[i], i == 0));
        }

        if(leaves.size() == 1)
            return leaves.get(0);
        else
            return new VerticalLayoutNode(leaves);
    }

    /**
     * Contains the main input loop
     */
    public void loop() throws IOException {
        // Terminal moet in rawInput staan voor dimensies te kunnen lezen!
        Terminal.enterRawInputMode();
        // Reading terminal dimensions for correct rendering
        render();
        // Main loop
        for ( ; ; ) {
            int b = Terminal.readByte();
            /*System.out.print(b);
            String bs = String.valueOf(b);
            Terminal.printText(1,1, bs);*/

            switch(b) {
                case 8, 127, 10, 62:
                    deleteCharacter();
                    break;
                // Control + S
                case 19:
                    saveBuffer();
                    break;
                // Control + P
                case 16:
                    moveFocus(DIRECTION.LEFT);
                    break;
                // Control + N
                case 14:
                    moveFocus(DIRECTION.RIGHT);
                    break;
                // Control + R
                case 18:
                    rotateLayout(ROT_DIRECTION.COUNTERCLOCKWISE);
                    break;
                // Control + T
                case 20:
                    rotateLayout(ROT_DIRECTION.CLOCKWISE);
                    break;
                // Surrogate keys
                case 27:
                    Terminal.readByte();
                    int c = Terminal.readByte();

                    switch ((char) c) {
                        case 'A', 'B', 'C', 'D':
                            moveCursor((char) c);
                            break;
                        case 'S':// F4
                            System.out.println((char) c);
                            break;
                    }
                    break;
                // Line separator
                case 13:
                    enterLineSeparator();
                    Terminal.clearScreen();
                    break;
                // Character input
                default:
                    Terminal.clearScreen();
                    if(b < 32 && b != 10 && b != 13 || 127 <= b)
                        break;
                    enterText((Integer.valueOf(b)).byteValue());
                    break;
            }
            Terminal.clearScreen();

            render();
            //Terminal.printText(10, 10, String.valueOf(b));
            // Flush stdIn & Recalculate dimensions
            System.in.skipNBytes(System.in.available());
        }

    }

    private void deleteCharacter() {
        rootLayout.deleteCharacter();
    }

    /**
     * Renders the layout with the terminal current height & width
     */
    void render() throws IOException {
        this.rootLayout.renderContent();
        this.rootLayout.renderCursor();
    }

    /**
     * Saves the FileBuffer's content to its file.
     */
    void saveBuffer() {
      this.rootLayout.saveActiveBuffer();
    }

    /**
     * Moves insertion point in a file buffer
     */
    void moveCursor(char code) {
        rootLayout.moveCursor(code);
    }

    /**
     * Handles inputted text and redirects them to the active {@link LayoutLeaf}.
     */
    void enterText(byte b) {
        rootLayout.enterText(b);
    }

    /**
     * Line separator is non-ASCII, so cannot enter through {@link Controller#enterText(byte)}
     */
    void enterLineSeparator() {
      rootLayout.enterInsertionPoint();
    }

    /**
     * Rearranges the Layouts clockwise or counterclockwise, depending on the argument given
     */
    void rotateLayout(ROT_DIRECTION orientation){
        rootLayout.rotateRelationshipNeighbor(orientation);
    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     */
    void moveFocus(DIRECTION dir) {
        this.rootLayout.moveFocus(dir);
    }

    // Test functions

    /**
     * Returns the root layout {@link Controller#rootLayout}. Only for testing purposes (default access modifier)
     */
    Layout getRootLayout() {
        return rootLayout.clone();
    }

    static void setLineSeparatorFromArgs(String[] args) {
        if(args[0].equals("--lf"))
            Controller.lineSeparatorArg = new byte[]{0x0a};
        else if(args[0].equals("--crlf"))
            Controller.lineSeparatorArg = new byte[]{0x0d, 0x0a};
        else Controller.lineSeparatorArg = System.lineSeparator().getBytes();
    }

    public static byte[] getLineSeparatorArg(){
        return Controller.lineSeparatorArg;
    }

}
