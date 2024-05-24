package files;

public class BufferWriteCommand implements Command {

    /**
     * The written character
     */
    private final byte writtenByte;

    /**
     * The line where the character is written
     */
    private final int bArrLine;

    /**
     * The column where the character is written
     */
    private final int bArrCol;

    /**
     * The buffer this operates on
     */
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
     * Constructor for a BufferWriteCommand
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
