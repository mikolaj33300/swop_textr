package controller.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import ui.SwingEditableTerminalApp;
import util.Coords;

public class SwingTerminalAdapter implements TermiosTerminalAdapter {
    private ArrayList<ResizeListener> resizeListenerArrayList = new ArrayList<>(0);


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
    }

/*  private void handleKey(KeyEvent e) {
      final char keyChar = e.getKeyChar();

      if (keyChar == KeyEvent.CHAR_UNDEFINED){
		  switch (e.getKeyCode()) {
			  case KeyEvent.VK_F4:
				  keyQueue.add(27);
				  keyQueue.add(83);// S in ascii
				  break;
	    case KeyEvent.VK_KP_UP:
	    case KeyEvent.VK_UP:
		  keyQueue.add(27);
		  keyQueue.add(65);// A in ascii
		  break;
	    case KeyEvent.VK_KP_DOWN:
	    case KeyEvent.VK_DOWN:
		  keyQueue.add(27);
		  keyQueue.add(66);// B in ascii
		  break;
	    case KeyEvent.VK_KP_RIGHT:
	    case KeyEvent.VK_RIGHT:
		  keyQueue.add(27);
		  keyQueue.add(67);// C in ascii
		  break;
	    case KeyEvent.VK_KP_LEFT:
	    case KeyEvent.VK_LEFT:
		  keyQueue.add(27);
		  keyQueue.add(68);// D in ascii
		  break;
	  	}
      } else {
	  	keyQueue.add((int) keyChar);
      }
  }*/

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

    public char[][] getContentBuffer(){
        return editableSwingTerminal.getContentBuffer();
    }

}
