import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.LayoutNode;

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
    private int width, height;

    /**
     * Creates a controller object.
     */
    public Controller(String[] args) {
        this.rootLayout = getRootLayout(args);
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

    private void check() {


    }

    /**
     * Contains the main input loop
     */
    public void loop() throws IOException {

        // Start program.
        Terminal.clearScreen();
        Terminal.enterRawInputMode();

        // Get terminal size.
        Terminal.reportTextAreaSize();

        // \033
        Terminal.readByte();
        // [
        Terminal.readByte();
        // 8
        Terminal.readByte();
        // ;
        Terminal.readByte();
        this.height = Terminal.readByte() / 2 - 1;
        Terminal.readByte();
        this.width = Terminal.readByte() / 2 - 1;

        System.out.println("lines: " + height + " - width:" + width);

        // Main loop
        for ( ; ; ) {

            int c = Terminal.readByte();

            switch(c) {
                    // Control + S
                case 19:

                    break;
                    // Control + P
                case 16:

                    break;

                    // Control + N
                case 14:
                    break;

                default:
                    //this.rootLayout.getActive();
                    render();

            }

        }

    }

    /**
     * Renders the layout
     */
    public void render() {
        this.rootLayout.render();
    }

    /**
     * After entering this combination
     */
    public void saveBuffer() {

    }

    public void enterText(char c) {

    }

    public void moveFocus() {

    }

    public void rotateRelationshipNeighbour() {

    }

    private void print(String... s) {
        for(String si : s) {
            System.out.println(si);
        }
    }

}