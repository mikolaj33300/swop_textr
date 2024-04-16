package command;

import files.FileBuffer;

public class BufferWriteCommand implements Command{
    private byte uC;
    private int bArrLine;
    private int bArrCol;

    private FileBuffer affectedBuffer;

    public void execute() {
        affectedBuffer.write(uC, bArrLine, bArrCol);
    }

    public void undo() {
        affectedBuffer.deleteCharacter(bArrCol+1, bArrLine);
    }

    public BufferWriteCommand(byte uC, int bArrCol, int bArrLine, FileBuffer affectedBuffer){
        this.uC = uC;
        this.bArrCol = bArrCol;
        this.bArrLine = bArrLine;
        this.affectedBuffer = affectedBuffer;
    }
}
