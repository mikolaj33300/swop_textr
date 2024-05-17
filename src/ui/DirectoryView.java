package ui;

import controller.adapter.TermiosTerminalAdapter;
import directory.Directory;
import directory.directorytree.FileSystemEntry;
import util.Coords;
import util.Pos;

import java.io.IOException;

/*
We try to achieve this visual:

   #  .

   #  element.txt

   #  /subdir

   #  /subdir2

 */
public class DirectoryView extends View {

    /**
     * The {@link Directory} object we will render
     */
    private final Directory dir;

    /**
     * Creates a {@link DirectoryView} object given a {@link Directory}
     * @param adapter the adapter we use to print
     * @param dir
     */
    public DirectoryView(TermiosTerminalAdapter adapter, Directory dir) {
        super(adapter);
        this.dir = dir;
    }

    @Override
    public int getFocusedCol() {
        return 0;
    }

    @Override
    public int getFocusedLine() {
        return 0;
    }

    @Override
    public int getTotalContentHeight() {
        return 0;
    }

    /**
     * Renders the {@link Directory} object
     * @param activeHash the hash of the active window
     * @throws IOException
     */
    @Override
    public void render(int activeHash) throws IOException {

        Coords coords = super.getRealUICoordsFromScaled(termiosTerminalAdapter);
        int height = coords.height;
        int width = coords.width;
        int startY = coords.startY;
        int startX = coords.startX;

        // We start printing one space from the top border
        int printLocationY = startY+1;

        this.termiosTerminalAdapter.printText(printLocationY, startX + 2, "#  .");

        // We loop over every entry and print it
        for(FileSystemEntry entry : dir.getEntries()) {

            if(entry.isDirectory())
                this.termiosTerminalAdapter.printText(printLocationY, startX + 2, "#  /" + entry.getName());
            else
                this.termiosTerminalAdapter.printText(printLocationY, startX + 2, "#  " + entry.getName());

            printLocationY += 2; // 2 spaces between every entry
        }

    }

    @Override
    public void renderCursor() throws IOException {



    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    protected int getLineLength(int focusedLine) {
        return 0;
    }

    /**
     * This function will calculate the Y position for the render cursor, when we select an entry which is off screen, we will
     * scroll by making the start Y position higher
     * @return
     */
    int calculateBeginning() throws IOException {

        Coords coords = super.getRealUICoordsFromScaled(termiosTerminalAdapter);
        int height = coords.height;

        // We print 1 from the top border, and 2 spaces per entry so our current depth is:
        int depth = 1;
        for(int i = 0; i < dir.getFocused(); i++)
            depth += (2*i);

        // If we have exceeded our height limit, we must return a valid start in the array
        if(depth > height)
            return depth - height;

        return coords.startY;

    }

}
