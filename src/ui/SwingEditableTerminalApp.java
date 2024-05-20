package ui;

import util.Coords;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

class TerminalPanel extends JPanel {
    char[][] bufferToDisplay = new char[1][1];
    ArrayList<Runnable> resizeListeners = new ArrayList<>();

    void clearBuffer() {
        for (int i = 0; i < bufferToDisplay.length; i++)
            Arrays.fill(bufferToDisplay[i], ' ');
    }

    public void setNewBuffer(char[][] newBuffer){
        bufferToDisplay = new char[newBuffer.length][newBuffer[0].length];
        for(int i = 0; i<newBuffer.length; i++){
            for(int j = 0; j< newBuffer[0].length; j++){
                bufferToDisplay[i][j] = newBuffer[i][j];
            }
        }
    }

    TerminalPanel() {
        setFont(new Font("Monospaced", Font.PLAIN, 12));
        clearBuffer();

/*        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                FontMetrics metrics = TerminalPanel.this.getFontMetrics(getFont());
                int nbRows = TerminalPanel.this.getHeight() / metrics.getHeight();
                int nbCols = TerminalPanel.this.getWidth() / metrics.charWidth('m');
                bufferToDisplay = new char[nbRows][nbCols];
                List<Runnable> resizeListenersCopy = List.copyOf(resizeListeners);
                for (Runnable listener : resizeListenersCopy)
                    listener.run();
            }
        });*/
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int baseLineOffset = fontHeight - fontMetrics.getDescent();

        int y = baseLineOffset;
        for (int lineIndex = 0; lineIndex < bufferToDisplay.length; lineIndex++) {
            g.drawChars(bufferToDisplay[lineIndex], 0, bufferToDisplay[lineIndex].length, 0, y);
            y += fontHeight;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fontMetrics = this.getFontMetrics(getFont());
        int width = fontMetrics.charWidth('m');
        return new Dimension(width * bufferToDisplay[0].length, fontMetrics.getHeight() * bufferToDisplay.length);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }
}

public class SwingEditableTerminalApp extends JFrame {
    private char[][] contentBuffer = new char[25][160];

    private int cursorRow = 0;
    private int cursorCol = 0;

    TerminalPanel terminalPanel = new TerminalPanel();

   public void moveCursor(int cursorRow, int cursorCol){
       this.cursorRow = cursorRow;
       this.cursorCol = cursorCol;
       updateAndRenderVisual();
   }

   private void updateAndRenderVisual(){
       terminalPanel.setNewBuffer(contentBuffer);
       terminalPanel.bufferToDisplay[cursorRow][cursorCol] = '*';
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

    public SwingEditableTerminalApp() {
        super("Swing Terminal App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        getContentPane().add(terminalPanel);
        clearBuffer();

        terminalPanel.resizeListeners.add(() -> {});

        terminalPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

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
}