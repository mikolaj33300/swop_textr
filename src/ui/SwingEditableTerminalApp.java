package ui;

import ioadapter.ASCIIKeyEventListener;
import ioadapter.ResizeListener;
import util.Coords;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingEditableTerminalApp extends JFrame {
  /**
   * the ASCIIKeyEventListener is a listener we send a request to every time a key is pressed
   */
    private ArrayList<ASCIIKeyEventListener> asciiListenerArrayList = new ArrayList<>(0);
  /**
   * the contentBuffer holds the characters currently displayed
   */
    private char[][] contentBuffer;

    /**
     * the row of the cursor
     */
    private int cursorRow = 0;
    /**
     * the column of the cursor
     */
    private int cursorCol = 0;

    /**
     * the listeners we send a request to every time we get resized
     */
    ArrayList<ResizeListener> resizeListeners = new ArrayList<>(0);

    /**
     * the underlying swing object
     */
    TerminalPanel terminalPanel;
    ResizeListener listenerOnPanel = null;

    /**
     * move the cursor to the 
     * @param cursorRow 
     * @param cursorCol
     */
   public void moveCursor(int cursorRow, int cursorCol){
       this.cursorRow = cursorRow;
       this.cursorCol = cursorCol;
       updateAndRenderVisual();
   }


   /**
    * rerender the display using the terminalPanel
    */
   private void updateAndRenderVisual(){
       terminalPanel.setNewBuffer(contentBuffer);
       terminalPanel.cursorRow = cursorRow;
       terminalPanel.cursorCol = cursorCol;
       terminalPanel.repaint();
   }

   /**
    * @param toWriteMessage the message to be insereted
    * @param dstBeginCol the column to insert the message
    * @param dstBeginRow the row to insert the message
    */
    public void updateBuffer(String toWriteMessage, int dstBeginCol, int dstBeginRow) {
        toWriteMessage.getChars(0, Math.min(toWriteMessage.length(), contentBuffer[0].length), contentBuffer[dstBeginRow], dstBeginCol);
        updateAndRenderVisual();
   }

   /**
    * clear our char buffer
    */
    public void clearBuffer(){
        for(int i = 0; i<contentBuffer.length; i++){
            for(int j = 0; j< contentBuffer[0].length; j++){
                contentBuffer[i][j] = ' ';
            }
        }
        cursorCol = 0;
        cursorRow = 0;
        updateAndRenderVisual();
    }

    /*
    This needs to subscribe by itself to events of the panel instead of just passing provided listeners to the panel
    directly, since this is the frontend of the swing ui part and the panel is a low level renderer
    and parameters for listeners would also vary (we add some invisible characters to the buffer due
    to a rendering glitch where the rightmost column disappears)
     */
    /**
     * Create new SwingEditableTerminalApp
     * adds ResizeListener
     * and KeyListener
     */
    public SwingEditableTerminalApp() {
        //Fixed listeners to use copies, i actually got the concurrent modification exception here
        super("Swing Terminal App");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.terminalPanel = new TerminalPanel();
        contentBuffer =  new char[25][100];
        terminalPanel.setNewBuffer(contentBuffer);
        getContentPane().add(terminalPanel);

        terminalPanel.subscribeToResize(new ResizeListener() {
            @Override
            public void notifyNewCoords(Coords n) throws IOException {
                cursorCol = 0;
                cursorRow = 0;
                contentBuffer = new char[n.height][n.width];
                List<ResizeListener> resizeListenersCopy = List.copyOf(resizeListeners);
                for(ResizeListener l : resizeListenersCopy){
                    l.notifyNewCoords(n);
                }

            }

        });

        terminalPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                    final char keyChar = e.getKeyChar();
                    if (keyChar == KeyEvent.CHAR_UNDEFINED){
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_F4:
                                notifyASCIISurrogate(27, 83);
                                break;
                            case KeyEvent.VK_KP_UP:
                            case KeyEvent.VK_UP:
                                notifyASCIISurrogate(27, 65);
                                break;
                            case KeyEvent.VK_KP_DOWN:
                            case KeyEvent.VK_DOWN:
                                notifyASCIISurrogate(27, 66);
                                break;
                            case KeyEvent.VK_KP_RIGHT:
                            case KeyEvent.VK_RIGHT:
                                notifyASCIISurrogate(27, 67);
                                break;
                            case KeyEvent.VK_KP_LEFT:
                            case KeyEvent.VK_LEFT:
                                notifyASCIISurrogate(27, 68);
                                break;
                        }
                    } else {
                        if(e.getKeyCode() == KeyEvent.VK_ENTER){
                            notifyASCIIBasic(13);
                        } else {
                            notifyASCIIBasic((int) keyChar);
                        }

                    }
            }
        });

        pack();

        setLocationRelativeTo(null);
    }

    /**
     * @return the size of our buffer
     */
    public Coords getSwingTerminalCharSize(){
       return new Coords(0, 0, contentBuffer[0].length, contentBuffer.length);
    }

    /**
     * @return the content of our buffer
     */
    public char[][] getContentBuffer(){
       char[][] toReturn = new char[contentBuffer.length][contentBuffer[0].length];
        for(int i = 0; i<contentBuffer.length; i++){
            for(int j = 0; j< contentBuffer[0].length; j++){
                toReturn[i][j] = contentBuffer[i][j];
            }
        }
        return toReturn;
    }

    /**
     * @return the row our cursor is in 
     */
    int getCursorRow(){
       return cursorRow;
    }

    /**
     * add the resizeListeners to our listeners
     * @param r the ResizeListener
     */
    public void subscribeToResize(ResizeListener r){
        this.resizeListeners.add(r);
    }

    /**
     * add the ASCIIKeyEventListener to our listeners
     * @param asciiKeyEventListener the ASCIIKeyEventListener
     */
    public void subscribeToASCIIKeyEnters(ASCIIKeyEventListener asciiKeyEventListener) {
        this.asciiListenerArrayList.add(asciiKeyEventListener);
    }

    /**
     * send our keyinput to the listener
     * @param b the key pressed
     */
    private void notifyASCIIBasic(int b){
        List<ASCIIKeyEventListener> asciiKeyEventListenersCopy = List.copyOf(asciiListenerArrayList);
        for(ASCIIKeyEventListener l : asciiKeyEventListenersCopy){
            l.notifyNormalKey(b);
        }
    }

    /**
     * send our keyinput with special characters (surrogates) to the listener
     * @param first the surrogate type (always 27 [ESC])
     * @param second the key
     */
    private void notifyASCIISurrogate(int first, int second){
        List<ASCIIKeyEventListener> asciiKeyEventListenersCopy = List.copyOf(asciiListenerArrayList);
        for(ASCIIKeyEventListener l : asciiKeyEventListenersCopy){
            l.notifySurrogateKeys(first, second);
        }
    }

    /**
     * @return the terminalPanel
     */
    public TerminalPanel getPanel(){
        return this.terminalPanel;
    }

    /**
     * remove an asciiKeyEventListener from our listeners, for example when we close a window
     */
    public void unsubscribeFromASCIIKeyEnters(ASCIIKeyEventListener listenerToRemove) {
        this.asciiListenerArrayList.remove(listenerToRemove);
    }

    
    /**
     * remove an resizeListener from our listeners, for example when we close a window
     */
    public void unsubscribeFromResize(ResizeListener listenerToRemove) {
        this.resizeListeners.remove(listenerToRemove);
    }
}
