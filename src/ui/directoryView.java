package ui;

import java.io.IOException;
import java.util.ArrayList;

import directory.directory;
import util.Coords;
import controller.adapter.TermiosTerminalAdapter;
import util.Rectangle;

public class directoryView extends View {

  private final directory dir;
  private TermiosTerminalAdapter termiosTerminalAdapter;

  public directoryView(directory dir, TermiosTerminalAdapter termios) {
    this.dir = dir;
    this.termiosTerminalAdapter = termios;
  }

  @Override 
  public void render (int activeHash) throws IOException {
    ArrayList<String> content = this.dir.getFilesList(); 
    Coords c = super.getRealUICoordsFromScaled(termiosTerminalAdapter);
    int x = this.dir.getX();
    int y = this.dir.getY();

    for (int i = 0; i < content.size(); i++){
      this.termiosTerminalAdapter.printText(c.startY+i+1, c.startX+1,  content.get(i));
    }

    renderScrollbar(c.height, c.startX+c.width-1, c.startY,
	y, content.size());
    renderHorzScrollbar(c.width, c.startX, c.startY+c.height-1,
	x, content.get(y).length(), 1);
    this.termiosTerminalAdapter.moveCursor(c.startY + y+1, c.startX + x+1);
    return;
  }

  @Override
  public void renderCursor () throws IOException {
    ArrayList<String> content = this.dir.getFilesList(); 
    int x = this.dir.getX();
    int y = this.dir.getY();
    Coords c = super.getRealUICoordsFromScaled(termiosTerminalAdapter);

    renderScrollbar(c.height, c.startX+c.width-1, c.startY,
	y, content.size());
    renderHorzScrollbar(c.width, c.startX, c.startY+c.height-1,
	x, content.get(y).length(), 1);
    this.termiosTerminalAdapter.moveCursor(c.startY + y+1, c.startX + x+1);
  }

  @Override
  public boolean equals (Object o) {
    if (o instanceof directoryView dir) {
      return (this.uiCoordsScaled == dir.uiCoordsScaled && this.terminalWidth == dir.terminalWidth && this.terminalHeight == dir.terminalHeight);
    } else {
      return false;
    }
  }
    /**
     * @param height the height of our view
     * @param scrollStartX the x coordinate for the scrollbar
     * @param focusedLine the number of the focussed line
     * @param fileBufferTotalHeight the amount of lines of the filebuffer
     */
    private void renderScrollbar(int height, int scrollStartX, int startY, int focusedLine, int fileBufferTotalHeight){
        fileBufferTotalHeight = fileBufferTotalHeight == 0 ? 1 : fileBufferTotalHeight;
        int visibleStartPercent = (focusedLine*height)/fileBufferTotalHeight;
        int visibleEndPercent = ((2+focusedLine)*height)/fileBufferTotalHeight;

        for (int i = 0; i < visibleStartPercent; i++)
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "|");
        for (int i = visibleStartPercent; i < visibleEndPercent; i++)
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "+");
        for (int i = visibleEndPercent; i < height-startY-2; i++)
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "|");
    }

    /**
     * render a horizontal renderHorzScrollbar
     * @param width the window width
     * @param startX the x coordinate to draw the scrollbar on
     * @param startY the Y coordinate to draw the scrollbar on
     * @param focusedCol the focussed column
     * @param fileBufferTotalWidth the 
     * @param focusedLine the focussed line
     */
    private void renderHorzScrollbar(int width, int startX, int startY, int focusedCol, int fileBufferTotalWidth, int focusedLine){
        fileBufferTotalWidth = fileBufferTotalWidth == 0 ? 1 : fileBufferTotalWidth;
        int visibleStartPercent = (focusedCol*width)/fileBufferTotalWidth;
        int visibleEndPercent = ((2+focusedCol)*width)/fileBufferTotalWidth;

        for (int i = 0; i < visibleStartPercent; i++)
          termiosTerminalAdapter.printText(1+startY, 1+startX+i, "-");
        for (int i = visibleStartPercent; i < visibleEndPercent; i++)
          termiosTerminalAdapter.printText(1+startY, 1+startX+i, "+");
        for (int i  = visibleEndPercent; i < width-1; i++)
          termiosTerminalAdapter.printText(1+startY, 1+startX+i, "-");
    }
}
