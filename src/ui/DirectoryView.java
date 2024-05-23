package ui;

import ioadapter.TermiosTerminalAdapter;
import directory.Directory;
import directory.directorytree.FileSystemEntry;
import util.Coords;

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
     * Determines the indent from left
     * "  "# name directory == 2
     */
    private final int indent = 2;
    /**
     * The amount of lines between every entry
     */
    private final int spacing = 1;

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
        return indent;
    }

    @Override
    public int getFocusedLine() {
        return (spacing+1) + dir.getFocused()*(spacing+1);
    }

    @Override
    public int getTotalContentHeight() {
        return (spacing + 1) + dir.getEntries().size() * (spacing + 1);
    }

    /**
     * Renders the {@link Directory} object
     * @param activeHash the hash of the active window
     * @throws IOException
     */
    @Override
    public void render(int activeHash) throws IOException {
        super.fill(1 + uiCoordsReal.startX, 1 + uiCoordsReal.startY, uiCoordsReal.width, uiCoordsReal.height, " ");

        Coords coords = super.getRealCoords();
        int height = coords.height;
        int width = coords.width;
        int startY = coords.startY;
        int startX = coords.startX;

        // We start printing one space from the top border
        int printLocationY = startY+2;

        // We loop over every entry and print it
        this.termiosTerminalAdapter.printText(printLocationY, startX + 2, "#  .");
        printLocationY += 2;

        for(int i = getStartIndex(); i < dir.getEntries().size(); i++) {

            FileSystemEntry entry = dir.getEntries().get(i);

            if(entry.isDirectory())
                this.termiosTerminalAdapter.printText(printLocationY, startX + 2, "#  /" + entry.getName());
            else
                this.termiosTerminalAdapter.printText(printLocationY, startX + 2, "#  " + entry.getName());

            printLocationY += 2; // 2 spaces between every entry
        }

    }

    @Override
    public Coords getRealCoords() {
        return super.getRealCoords();
    }

    @Override
    public void renderCursor() throws IOException {
        Coords coords = super.getRealCoords();
        int height = coords.height;
        int width = coords.width;
        int startY = coords.startY;
        int startX = coords.startX;
        termiosTerminalAdapter.moveCursor(startY + getFocusedLine(), startX + getFocusedCol());
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
     * Determines the start index for {@link Directory#getEntries()} to display so the list
     * will not fall offscreen before we hit {@link Directory#getFocused()}
     * @return the index in {@link Directory#getEntries()} which will be the highest
     * @throws IOException
     */
    int getStartIndex() throws IOException {

        Coords coords = super.getRealCoords();
        int height = coords.height;

        // 1 + focused want 0 is parent dir ; spacing + 1 want we voorzien minimum 1 lijn per entry
        int totalHeightNeeded = (1+dir.getFocused()) * (spacing+1);

        // Total height - window heigt = de hoogte dat we moeten negeren.
        // Dit delen we door hoeveel spacing we hebben EN hoeveel de entry bijdraagt aan de hoogte = 1
        /* focus op entry3 en hoogte window = 3; entry 2 en 3 moeten zichtbaar zijn.
         * totalheight = (1+3) * (1+1) = 8 ; 8-3 / (1+1) = 5/2 = 2.5 = 2 -> entry 2 mogen we printen
        -
        root
        -
        entry1
        -
        entry2
        -
        entry3
        -
         */
        return (int) Math.floor((totalHeightNeeded - height) / (spacing + 1)) < 0
                ?
                0 : (int) Math.floor((totalHeightNeeded - height) / (spacing + 1));

    }

}
