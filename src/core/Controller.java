package core;

import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Controller {

    private static byte[] lineSeparatorArg;

    /**
     * Root layout
     */
    private Layout rootLayout;

    /**
     * Size of the terminal
     */
    private int width, height;

    /**
     * Creates a controller object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link LayoutLeaf} will be assigned according to arguments given by {@link Controller#main(String[])}
     */
    public Controller(String[] args) {
        this.rootLayout = initRootLayout(args, lineSeparatorArg);
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
        btj.loop();
    }

    /**
     * Creates an instance of {@link Layout} representing a {@link LayoutNode} containing {@link LayoutLeaf} depending on
     * main input arguments.
     */
    private Layout initRootLayout(String[] args, byte[] lineSeparator) {
        ArrayList<Layout> leaves = new ArrayList<>();
        for(int i = lineSeparator == null ? 0 : 1 ; i < args.length; i++) {
            Path checkPath = Paths.get(args[i]);
            if (!Files.exists(checkPath))
                System.out.println("Kutzooi");
            else
                leaves.add(new LayoutLeaf(new FileBuffer(args[i]), i == 0));
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

        // Start program.
        Terminal.clearScreen();
        // Terminal moet in rawInput staan voor dimensies te kunnen lezen!
        Terminal.enterRawInputMode();
        // Reading terminal dimensions for correct rendering
        retrieveDimensions();
        render();
        // Main loop
        for ( ; ; ) {
            int b = Terminal.readByte();
            /*System.out.print(b);
            String bs = String.valueOf(b);
            Terminal.printText(1,1, bs);*/

            switch(b) {
                case 8, 127, 10:
                    deleteCharacter();
                    break;
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
                // Control + R
                case 18:
                    rotateLayout(Layout.ROT_DIRECTION.COUNTERCLOCKWISE);
                    break;
                // Control + T
                case 20:
                    rotateLayout(Layout.ROT_DIRECTION.CLOCKWISE);
                    break;
                        // Arrow keys
                case 27:
                    Terminal.readByte();
                    moveCursor((char) Terminal.readByte());
                    break;
                // Line separator
                case 13:
                    enterLineSeparator();
                    break;
                // Character input
                default:
                    if (b >= 32 && b <= 126)
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
    void render() {
        this.rootLayout.renderTextContent(0, 0, this.width, this.height);
        this.rootLayout.renderCursor(0, 0, this.width, this.height);
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
    void rotateLayout(Layout.ROT_DIRECTION orientation){
        rootLayout.rotateRelationshipNeighbor(orientation);
    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     */
    void moveFocus(Layout.DIRECTION dir) {
        this.rootLayout.moveFocus(dir);
    }

    // Test functions

    /**
     * Returns the root layout {@link Controller#rootLayout}. Only for testing purposes (default access modifier)
     */
    Layout initRootLayout() {
        return rootLayout;
    }

    /**
     * <p>Calculates the dimensions of the terminal
     * Credits to BTJ. This looks very clean and intuïtive.
     * Sets the fields {@link Controller#width} and {@link Controller#height}.</p>
     * <p>Method set to default for unit test access.</p>
     */
    void retrieveDimensions() throws IOException {

        Terminal.reportTextAreaSize();
        int tempByte = 0;
        for(int i = 0; i < 4; i++)
            Terminal.readByte();

        int c = Terminal.readByte();
        int height = c - '0';
        tempByte = Terminal.readByte();

        for(;;) {
            if(tempByte < '0' || '9' < tempByte)
                break;
            if (height > (Integer.MAX_VALUE - (c - '0')) / 10)
                break;
            height = height * 10 + (tempByte - '0');
            tempByte = Terminal.readByte();

        }

        c = Terminal.readByte();
        int width = c - '0';
        tempByte = Terminal.readByte();

        for(;;) {
            if(tempByte < '0' || '9' < tempByte)
                break;
            if (width > (Integer.MAX_VALUE - (c - '0')) / 10)
                break;
            width = width * 10 + (tempByte - '0');
            tempByte = Terminal.readByte();
        }
        this.width = width;
        this.height = height;

    }

    protected static void setLineSeparatorFromArgs(String[] args) {
        if(args[0].equals("--lf"))
            Controller.lineSeparatorArg = new byte[]{0x0a};
        else if(args[0].equals("--crlf"))
            Controller.lineSeparatorArg = new byte[]{0x0d, 0x0a};
        else Controller.lineSeparatorArg = null;
    }

    public static byte[] getLineSeparatorArg(){
        return Controller.lineSeparatorArg;
    }
}
