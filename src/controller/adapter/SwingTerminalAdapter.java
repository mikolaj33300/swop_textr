package controller.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import controller.ASCIIKeyEventListener;
import ui.SwingEditableTerminalApp;
import util.Coords;

public class SwingTerminalAdapter implements TermiosTerminalAdapter {
    private ArrayList<ResizeListener> resizeListenerArrayList = new ArrayList<>(0);
    private ArrayList<ASCIIKeyEventListener> asciiListenerArrayList = new ArrayList<>(0);


    private SwingEditableTerminalApp editableSwingTerminal;


    public SwingTerminalAdapter() {
        this.editableSwingTerminal = new SwingEditableTerminalApp();
		editableSwingTerminal.setVisible(true);
        editableSwingTerminal.subscribeToResize(new ResizeListener() {
            @Override
            public void notifyNewCoords(Coords newCoords) throws IOException {
                for(ResizeListener l : resizeListenerArrayList){
                    l.notifyNewCoords(newCoords);
                }
            }
        });

        editableSwingTerminal.subscribeToASCIIKeyEnters(new ASCIIKeyEventListener() {

            @Override
            public void notifyNormalKey(int byteInt) {
                for(ASCIIKeyEventListener l : asciiListenerArrayList){
                    l.notifyNormalKey(byteInt);
                }
            }

            @Override
            public void notifySurrogateKeys(int first, int second) {
                for(ASCIIKeyEventListener l : asciiListenerArrayList){
                    l.notifySurrogateKeys(first, second);
                }
            }
        });
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
/*      while (keyQueue.isEmpty()) {
	  try {
	      TimeUnit.MILLISECONDS.sleep(1);
	  } catch (Exception e) {
	      System.out.println("sleep failed");
	      System.exit(1);
	  }
      }
      return keyQueue.remove();*/
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
        //TODO Implement this, add a listener and use the contained jframe's built in functionality
        resizeListenerArrayList.add(l);
    }

    @Override
    public void setInputListener(Runnable runnable) {

    }

    @Override
    public void clearInputListener() {

    }

    public char[][] getContentBuffer(){
        return editableSwingTerminal.getContentBuffer();
    }

    public void subscribeToEnteredASCII(ASCIIKeyEventListener l){
        asciiListenerArrayList.add(l);
    }

    public void subscribeToKeyPresses(ASCIIKeyEventListener newAsciiListener) {
        editableSwingTerminal.subscribeToASCIIKeyEnters(newAsciiListener);
    }

}
