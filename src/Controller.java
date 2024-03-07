import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.LayoutNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    /**
     * The beginning layout when the application is started with paths.
     */
    private Layout rootLayout;

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
        return new LayoutNode(leaves);
    }

    /**
     * A beautiful start for a beautiful project
     */
    public static void main(String[] args) throws IOException {
        Controller btj = new Controller(args);
        btj.loop();
    }

    public void loop() throws IOException {

        System.out.println("Project runs succesfully");
        Terminal.enterRawInputMode();

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

}