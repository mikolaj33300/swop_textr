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
    private ArrayList<ASCIIKeyEventListener> asciiListenerArrayList = new ArrayList<>(0);
    private char[][] contentBuffer;

    private int cursorRow = 0;
    private int cursorCol = 0;

    ArrayList<ResizeListener> resizeListeners = new ArrayList<>(0);

    TerminalPanel terminalPanel;
    ResizeListener listenerOnPanel = null;

   public void moveCursor(int cursorRow, int cursorCol){
       this.cursorRow = cursorRow;
       this.cursorCol = cursorCol;
       updateAndRenderVisual();
   }


   private void updateAndRenderVisual(){
       terminalPanel.setNewBuffer(contentBuffer);
       terminalPanel.cursorRow = cursorRow;
       terminalPanel.cursorCol = cursorCol;
       //terminalPanel.bufferToDisplay[cursorRow][cursorCol] = '*';
       terminalPanel.repaint();
   }

    public void updateBuffer(String toWriteMessage, int dstBeginCol, int dstBeginRow) {
        toWriteMessage.getChars(0, Math.min(toWriteMessage.length(), contentBuffer[0].length), contentBuffer[dstBeginRow], dstBeginCol);
        updateAndRenderVisual();
   }

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
    public SwingEditableTerminalApp() {
        //TODO: FIX LISTENERS TO USE COPY LIST INSTEAD OF THE ACTIVE LIST WHEN ITERATING OVER THEM
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
                for(ResizeListener l : resizeListeners){
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

    public Coords getSwingTerminalCharSize(){
       return new Coords(0, 0, contentBuffer[0].length, contentBuffer.length);
    }

    public char[][] getContentBuffer(){
       char[][] toReturn = new char[contentBuffer.length][contentBuffer[0].length];
        for(int i = 0; i<contentBuffer.length; i++){
            for(int j = 0; j< contentBuffer[0].length; j++){
                toReturn[i][j] = contentBuffer[i][j];
            }
        }
        return toReturn;
    }

    int getCursorRow(){
       return cursorRow;
    }

    public void subscribeToResize(ResizeListener r){
        this.resizeListeners.add(r);
    }

    public void subscribeToASCIIKeyEnters(ASCIIKeyEventListener asciiKeyEventListener) {
        this.asciiListenerArrayList.add(asciiKeyEventListener);
    }
    private void notifyASCIIBasic(int b){
        for(ASCIIKeyEventListener l : asciiListenerArrayList){
            l.notifyNormalKey(b);
        }
    }

    private void notifyASCIISurrogate(int first, int second){
        for(ASCIIKeyEventListener l : asciiListenerArrayList){
            l.notifySurrogateKeys(first, second);
        }
    }

    public TerminalPanel getPanel(){
        return this.terminalPanel;
    }

    public void unsubscribeFromASCIIKeyEnters(ASCIIKeyEventListener listenerToRemove) {
        this.asciiListenerArrayList.remove(listenerToRemove);
    }

    public void unsubscribeFromResize(ResizeListener listenerToRemove) {
        this.resizeListeners.remove(listenerToRemove);
    }
}
