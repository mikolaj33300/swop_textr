package files;

public class BufferEnterInsertionCommand implements Command{

    /**
     * The column of the insertion point
     */
    private final int iCol;

    /**
     * The line of the insertion point
     */
    private final int iLine;

    /**
     * The filebuffer this command operates on
     */
    private final FileBuffer containedFb;

    /**
     * Constructor for a BufferEnterInsertionCommand
     * @param iCol the column of the insertion point
     * @param iLine the line of the insertion point
     * @param containedFb the filebuffer this operates on
     */
    public BufferEnterInsertionCommand(int iCol, int iLine, FileBuffer containedFb){
        this.iCol = iCol;
        this.iLine = iLine;
        this.containedFb = containedFb;
    }

    /**
     * Undo the insertion in the contained filebuffer
     */
    @Override
    public void undo() {
        containedFb.deleteCharacter(0, iLine+1);
    }


    /**
     * A command to enter an insertion point in the contained filebuffer
     */
    @Override
    public void execute() {
        containedFb.enterInsertionPoint(iLine, iCol);
    }
}
