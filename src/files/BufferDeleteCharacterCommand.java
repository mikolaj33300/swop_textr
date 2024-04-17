package files;

import java.util.Arrays;

public class BufferDeleteCharacterCommand implements Command {
        private final int iCol;
        private final int iLine;
        private byte[] deleted;

        private final FileBuffer containedFb;

	/**
	 * execute a command to delete in the contained filebuffer
	 */
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

	/**
	 * undo the deletion in the contained filebuffer
	 */
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

	/**
	 * constructor
	 * @param iCol the column of our deleted character
	 * @param iLine the line of our deleted character
	 * @param containedFb the filebuffer this operates on
	 */
        public BufferDeleteCharacterCommand(int iCol, int iLine, FileBuffer containedFb){
            this.containedFb = containedFb;
            this.iCol = iCol;
            this.iLine = iLine;
        }
}
