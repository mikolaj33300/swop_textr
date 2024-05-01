package ui;

import listeners.directoryListener;
import util.Coords;
import controller.adapter.TermiosTerminalAdapter;
import util.Rectangle;

public class directoryView extends View implements directoryListener {
  String content;
  Rectangle coords;

  @Override 
  public void render(int activeHash){
    TermiosTerminalAdapter.printText(coords.startX, coords.startY, 
        coords.height, coords.width, content);
    return;
  }

  @Override
  public void renderCursor() {
    return;
  }

  public void setContent(String cntnt){
    this.content = cntnt;
  }

  @Override
  public boolean equals (Object o) {
    if (o instanceof directoryView dir) {
      return (this.uiCoordsScaled == dir.uiCoordsScaled && this.terminalWidth == dir.terminalWidth && this.terminalHeight == dir.terminalHeight);
    } else {
      return false;
    }
  }
}
