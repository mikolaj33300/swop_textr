package command;

import files.FileBuffer;

public class BufferWriteCommand implements Command{
    private byte uC;
    private int bArrIndex;

    private FileBuffer affectedBuffer;

    public void execute() {
        affectedBuffer.write(uC, bArrIndex);
    }

    public void undo() {
        affectedBuffer.deleteCharacterWithIndex(bArrIndex);
    }

    public BufferWriteCommand(byte uC, int bArrIndex, FileBuffer affectedBuffer){
        this.uC = uC;
        this.bArrIndex = bArrIndex;
        this.affectedBuffer = affectedBuffer;
    }
}
