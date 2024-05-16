package controller.adapter;

import ui.swing.SwingTerminal;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import util.Coords;

public class SwingTerminalAdapter implements TermiosTerminalAdapter {
  private SwingTerminal terminal = new SwingTerminal();

  @Override
  public void clearScreen() {
		terminal.clearBuffer();
    terminal.repaint();
  }

  @Override
  public void enterRawInputMode(){
    return;
  }

  @Override
  public void leaveRawInputMode() {
      return;
  }

  @Override
  public void moveCursor(int row, int column){
      terminal.setY(row);
      terminal.setX(column);
  }

  @Override
  public void printText(int row, int column, String text){

  }

  @Override
  public int readByte() throws IOException {

  }

  @Override
  public int readByte(long deadline) throws IOException, TimeoutException{

  }

  @Override
  public Coords getTextAreaSize() throws IOException {

  }
}
