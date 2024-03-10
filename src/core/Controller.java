package core;

import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.LayoutNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Controller {

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
        this.rootLayout = getRootLayout(args, getLineSeparator(args));
    }

    /**
     * A beautiful start for a beautiful project
     */
    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.out.println("TextR needs parameters to run.");
            return;
        }

        Controller btj = new Controller(args);
        btj.loop();
    }

    /**
     * Creates an instance of {@link Layout} representing a {@link LayoutNode} containing {@link LayoutLeaf} depending on
     * main input arguments.
     */
    private Layout getRootLayout(String[] args, String lineSeparator) {
        ArrayList<Layout> leaves = new ArrayList<>();
        for(int i = lineSeparator == null ? 0 : 1 ; i < args.length; i++) {
            Path checkPath = Paths.get(args[i]);
            if (!Files.exists(checkPath)) {
                //TODO throw error for unknown path
            }
            leaves.add(new LayoutLeaf(new FileBuffer(args[i], lineSeparator), i == 0));
        }
        return new LayoutNode(Layout.Orientation.HORIZONTAL, leaves);
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

            // Flush stdIn & Recalculate dimensions
            System.in.skipNBytes(System.in.available());
            retrieveDimensions();

        }

    }

    /**
     * Renders the layout with the terminal current height & width
     */
    public void render() {
        // TODO root layout has to render its children on itself.
        //this.rootLayout.render(this.width, this.height);
    }

    /**
     * Saves the FileBuffer's content to its file.
     */
    public void saveBuffer() {

    }

    /**
     * Handles inputted text and redirects them to the active {@link LayoutLeaf}.
     */
    public void enterText(int c) {

    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     */
    public void moveFocus(Layout.DIRECTION dir) {
        this.rootLayout.moveFocus(dir);
    }

    // Test functions

    /**
     * Returns the root layout {@link Controller#rootLayout}. Only for testing purposes (default access modifier)
     */
    Layout getRootLayout() {
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

    }

    String getLineSeparator(String[] args) {
        if(args[0].equals("--lf"))
            return "0a";
        else if(args[0].equals("--crlf"))
            return "0d0a";
        else return null;
    }

    /**
     * Temporary helper
     */
    private void print(String... s) {
        for(String si : s) {
            System.out.println(si);
        }
    }

    private static String getPrint(String[] s) {
        String out = "";
        for(String l : s) out += l + ", ";
        return out;
    }

}