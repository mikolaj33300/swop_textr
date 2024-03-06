import files.FileBuffer;
import io.github.btj.termios.Terminal;
import layouttree.Layout;
import layouttree.LayoutLeaf;
import layouttree.LayoutNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Controller {

    private Layout rootLayout;

    public Controller(String[] args) {

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
        Controller controller = new Controller(args);
        run();
    }

    public static void run() throws IOException {

        Terminal.enterRawInputMode();

        for (; ; ) {

            int c = Terminal.readByte();

            if (c == '\033')
                System.out.println("Pressed");

        }

    }

    public void render(int heigh, int width) {

    }

    public void saveBuffer() {

    }

    public void enterText(char c) {

    }

    public void moveFocus() {

    }

    public void rotateRelationshipNeighbour() {

    }



}