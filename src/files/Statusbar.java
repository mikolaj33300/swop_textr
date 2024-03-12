package files;

public class Statusbar {

    private final FileBuffer buffer;
    private int column, lines, scroll;

    public Statusbar(FileBuffer buffer) {
        this.buffer = buffer;
        this.scroll = 0;
    }

    /**
     * Determines the top visible line in of the buffer
     */
    int getScroll() {
        return this.scroll;
    }

    int getColumn() {
        return this.column;
    }

    int getLines() {
        return this.lines;
    }

    String renderStatus() {
        String statusLine = buffer.getFileHolder().getPath();
        statusLine += " ";
        statusLine += String.valueOf(buffer.getInsertionPoint()/buffer.getBytes().length);
        statusLine += "%";
        return statusLine;
    }

    String getScrollbar(int height, int index){
        if (index == height*((float) buffer.getInsertionPoint()/this.buffer.getBytes().length))
            return "+";
        else
            return "|";
    }
}
