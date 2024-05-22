package ioadapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ui.SwingEditableTerminalApp;
import util.Coords;

public class SwingTerminalAdapter implements TermiosTerminalAdapter {
    private ArrayList<ASCIIKeyEventListener> asciiListenerArrayList = new ArrayList<>(0);


    private SwingEditableTerminalApp editableSwingTerminal;


    public SwingTerminalAdapter() {
        this.editableSwingTerminal = new SwingEditableTerminalApp();
		editableSwingTerminal.setVisible(true);
    }

  @Override
  public void clearScreen() {
      editableSwingTerminal.clearBuffer();
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
      editableSwingTerminal.moveCursor(row-1, column-1);
  }

  @Override
  public void printText(int row, int column, String text){
	  editableSwingTerminal.updateBuffer(text, column-1, row-1);
  }

  @Override
  public int readByte() throws IOException {
	  return 0;
  }

  /*
   * less than perfect, but how to get the input directly here?
   */
  @Override
  public int readByte(long deadline) throws IOException, TimeoutException{
/*      while (keyQueue.isEmpty()) {
	  try {
	      TimeUnit.MILLISECONDS.sleep(1);
	  } catch (Exception e) {
	      System.out.println("sleep failed");
	      System.exit(1);
	  }
	  if (System.currentTimeMillis() > deadline){
	      throw new TimeoutException();
	  }
      }
      return keyQueue.remove();*/
	  return 0;
  }

  @Override
  public Coords getTextAreaSize() throws IOException {
      return editableSwingTerminal.getSwingTerminalCharSize();
  }

    @Override
    public void subscribeToResizeTextArea(ResizeListener l) {
        editableSwingTerminal.subscribeToResize(l);
    }

    @Override
    public void setInputListenerOnAWTEventQueue(Runnable runnable) {

    }

    @Override
    public void clearInputListener() {

    }

    public char[][] getContentBuffer(){
        return editableSwingTerminal.getContentBuffer();
    }

    public void subscribeToKeyPresses(ASCIIKeyEventListener newAsciiListener) {
        editableSwingTerminal.subscribeToASCIIKeyEnters(newAsciiListener);
    }

}
