package controller.adapter;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SwingTerminalAdapter implemeents TermiosTerminalAdapter {
  private SwingTerminal terminal = new SwingTerminal();

  public void clearScreen() {
		terminal.clearBuffer();
    terminal.repaint();
  }

  public void enterRawInputMode(){
    
  }

  public void moveCursor(int row, int column){

  }

  public void printText(int row, int column, String text){

  }

  public int readByte() throws IOException {

  }

  public int readByte(long deadline) throws IOException, TimeoutException{

  }

  public Coords getTextAreaSize() throws IOException {

  }
}
