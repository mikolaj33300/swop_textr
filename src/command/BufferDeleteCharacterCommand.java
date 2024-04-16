package command;

import files.FileBuffer;

public class BufferDeleteCharacterCommand implements Command{
        private int iCol;
        private int iLine;
        private byte deleted;

        private FileBuffer containedFb;

        public void execute() {
            deleted = containedFb.deleteCharacter(iCol, iLine);
        }

        public void undo() {
            int pos = containedFb.convertLineAndColToIndex(iLine, iCol) - 1;
            if (pos > 0)
                containedFb.write(deleted, iLine, iCol-1);
        }

        public BufferDeleteCharacterCommand(int iCol, int iLine, FileBuffer containedFb){
            this.containedFb = containedFb;
            this.iCol = iCol;
            this.iLine = iLine;
        }
}
