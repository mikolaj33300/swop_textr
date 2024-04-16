package command;

import files.FileBuffer;

import java.util.Arrays;

public class BufferDeleteCharacterCommand implements Command{
        private int iCol;
        private int iLine;
        private byte[] deleted;

        private FileBuffer containedFb;

        public void execute() {
            if(iCol == 0 && iLine != 0){
                deleted = containedFb.getLineSeparator();
            } else if (iCol!=0){
                deleted = new byte[]{containedFb.getLines().get(iLine).get(iCol-1).byteValue()};
            } else {
                deleted = null;
            }
            containedFb.deleteCharacter(iCol, iLine);
        }

        public void undo() {
            if(Arrays.equals(containedFb.getLineSeparator(), deleted)){
                containedFb.enterInsertionPoint(containedFb.convertLineAndColToIndex(iLine, iCol));
            } else {
                if(deleted != null){
                    //We deleted a normal character
                    containedFb.write(deleted[0], iLine, iCol-1);
                }
            }
        }

        public BufferDeleteCharacterCommand(int iCol, int iLine, FileBuffer containedFb){
            this.containedFb = containedFb;
            this.iCol = iCol;
            this.iLine = iLine;
        }
}
