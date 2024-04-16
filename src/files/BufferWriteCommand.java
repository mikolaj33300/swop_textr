package files;

public class BufferWriteCommand implements Command {
    private final byte writtenByte;
    private final int bArrLine;
    private final int bArrCol;

    private FileBuffer affectedBuffer;

    public void execute() {
        affectedBuffer.write(writtenByte, bArrLine, bArrCol);
    }

    public void undo() {
        affectedBuffer.deleteCharacter(bArrCol+1, bArrLine);
    }

    public BufferWriteCommand(byte uC, int bArrCol, int bArrLine, FileBuffer affectedBuffer){
        this.writtenByte = uC;
        this.bArrCol = bArrCol;
        this.bArrLine = bArrLine;
        this.affectedBuffer = affectedBuffer;
    }
}
