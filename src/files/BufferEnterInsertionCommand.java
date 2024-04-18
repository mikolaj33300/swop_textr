package files;

public class BufferEnterInsertionCommand implements Command{
    private final int iCol;
    private final int iLine;

    private final FileBuffer containedFb;

    public BufferEnterInsertionCommand(int iCol, int iLine, FileBuffer containedFb){
        this.iCol = iCol;
        this.iLine = iLine;
        this.containedFb = containedFb;
    }

    @Override
    public void undo() {
        containedFb.deleteCharacter(0, iLine+1);
    }

    @Override
    public void execute() {
        containedFb.enterInsertionPoint(iLine, iCol);
    }
}
