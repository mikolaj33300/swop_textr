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

public class Controller {

    /**
     * The beginning layout when the application is started with paths.
     */
    private Layout rootLayout;

    /**
     * Instance which calculates terminal size.
     */
    private int width, height;

    /**
     * Creates a controller object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link LayoutLeaf} will be assigned according to arguments given by {@link Controller#main(String[])}
     */
    public Controller(String[] args) {
        this.rootLayout = getRootLayout(args);
    }

    /**
     * A beautiful start for a beautiful project
     */
    public static void main(String[] args) throws IOException {
        Controller btj = new Controller(args);
        btj.loop();
    }

    /**
     * Creates an instance of {@link Layout} representing a {@link LayoutNode} containing {@link LayoutLeaf} depending on
     * main input arguments.
     */
    private Layout getRootLayout(String[] args) {
        ArrayList<Layout> leaves = new ArrayList<>();
        for(String s : args) {
            Path checkPath = Paths.get(s);
            if (!Files.exists(checkPath)) {
                //TODO throw error for unknown path
            }
            leaves.add(new LayoutLeaf(new FileBuffer(s), false));
        }
        return new LayoutNode(Layout.Orientation.HORIZONTAL, leaves);
    }

    /**
     * Returns the root layout {@link Controller#rootLayout}. Only for testing purposes (default access modifier)
     */
    Layout getRootLayout() {
        return rootLayout;
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
     * Saves the {@link FileBuffer#getContent()} to its file.
     */
    public void saveBuffer() {
        //TODO
        //this.rootLayout.getActive().save();
    }

    /**
     * Handles inputted text and redirects them to the active {@link LayoutLeaf}.
     */
    public void enterText(int c) {
        // TODO getActive layout
        //this.rootLayout.getActive().enterText(c);
    }

    /**
     * Changes the focused {@link LayoutLeaf} to another.
     */
    public void moveFocus(Layout.DIRECTION dir) {
        this.rootLayout.moveFocus(dir);
    }

    /**
     * TODO !!!!!!!!!! wat is dit???
     */
    public void rotateRelationshipNeighbour() {}

    /**
     * Calculates the dimensions of the terminal
     * Credits to BTJ. This looks very clean and intuïtive.
     * Sets the fields {@link Controller#width} and {@link Controller#height}.
     *
     * Method set to default for unit test access.
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

    /**
     * Temporary helper
     */
    private void print(String... s) {
        for(String si : s) {
            System.out.println(si);
        }
    }

}