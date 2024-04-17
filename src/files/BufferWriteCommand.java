package files;

public class BufferWriteCommand implements Command {
    private final byte writtenByte;
    private final int bArrLine;
    private final int bArrCol;

    private FileBuffer affectedBuffer;

    /**
     * execute the write operation
     */
    public void execute() {
        affectedBuffer.write(writtenByte, bArrLine, bArrCol);
    }

    /**
     * undo the write operation
     */
    public void undo() {
        affectedBuffer.deleteCharacter(bArrCol+1, bArrLine);
    }

    /**
     * @param uC the written character
     * @param bArrLine the line where the character is written
     * @param bArrCol the column where the character is written
     * @param affectedBuffer the buffer this operates on
     */
    public BufferWriteCommand(byte uC, int bArrCol, int bArrLine, FileBuffer affectedBuffer){
        this.writtenByte = uC;
        this.bArrCol = bArrCol;
        this.bArrLine = bArrLine;
        this.affectedBuffer = affectedBuffer;
    }
}
