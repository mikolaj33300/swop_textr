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
    public int getScroll() {
        return this.scroll;
    }

    public int getColumn() {
        return this.column;
    }

    public int getLines() {
        return this.lines;
    }

    public FileBuffer getBuffer(){
        return this.buffer.clone();
    }

    public String renderStatusbar() {
        String statusLine = buffer.getFileHolder().getPath();
        statusLine += " #Lines:";
        statusLine += String.valueOf(getBuffer().getLines().size());
        statusLine += " #Chars:";
        statusLine += String.valueOf(getBuffer().getBytes().length);
        statusLine += "Insert:[";
        //TODO: getInsertionPoint implementeren om lines door te geven aan de statusbar
        //statusLine += String.valueOf(buffer.getLayout().getInsertionPoint().getLine());
        statusLine += ";";
        //statusLine += String.valueOf(buffer.getLayout().getInsertionPoint().getColumn());
        statusLine += "] ";
        if(buffer.getDirty())
            statusLine += "Dirty";
        else
            statusLine += "Clean";
        statusLine += " ";
        //if(buffer.getLayout().getContainsActive())
        //    statusLine += "Active";
        //else
            statusLine += "Not Active";
        return statusLine;
    }

    String getScrollbar(int height, int index){
        if (index == height*((float) buffer.getInsertionPoint()/this.buffer.getBytes().length))
            return "+";
        else
            return "|";
    }

}
