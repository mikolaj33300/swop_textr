package ui;

import ioadapter.ResizeListener;
import util.Coords;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerminalPanel extends JPanel {
  /**
   * our buffer of displayed content
   */
    char[][] bufferToDisplay = new char[1][1];
    /**
     * our listeners in case we have resized
     */
    ArrayList<ResizeListener> resizeListeners = new ArrayList<>();

    /**
     * the cursorRow
     */
    int cursorRow = -1;// start at -1 for no cursor for snake
    /**
     * the cursorColumn
     */
    int cursorCol = -1;

    /**
     * fill our buffer with spaces to clear it
     */
    void clearBuffer() {
        for (int i = 0; i < bufferToDisplay.length; i++)
            Arrays.fill(bufferToDisplay[i], ' ');
    }

    /**
     * update our buffer to newBuffer
     * @param newBuffer
     */
    public void setNewBuffer(char[][] newBuffer) {
        bufferToDisplay = new char[newBuffer.length][newBuffer[0].length];
        for (int i = 0; i < newBuffer.length; i++) {
            for (int j = 0; j < newBuffer[0].length; j++) {
                bufferToDisplay[i][j] = newBuffer[i][j];
            }
        }
    }

    /**
     * create a terminalPanel and its resizeListeners
     */
    TerminalPanel() {
        setFont(new Font("Monospaced", Font.PLAIN, 12));
        clearBuffer();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                FontMetrics metrics = TerminalPanel.this.getFontMetrics(getFont());
                int nbRows = TerminalPanel.this.getHeight() / metrics.getHeight();
                int nbCols = TerminalPanel.this.getWidth() / metrics.charWidth('m');
                bufferToDisplay = new char[nbRows][nbCols];
                java.util.List<ResizeListener> resizeListenersCopy = List.copyOf(resizeListeners);
                for (ResizeListener listener : resizeListenersCopy) {
                    try {
                        listener.notifyNewCoords(new Coords(0, 0, nbCols - 3, nbRows));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    /**
     * update our window
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
	int fontWidth = fontMetrics.charWidth('m');
        int baseLineOffset = fontHeight - fontMetrics.getDescent();
	if (cursorRow >= 0 && cursorCol >= 0)
	    g.drawRect(fontMetrics.charsWidth(bufferToDisplay[cursorRow], 0, cursorCol), cursorRow*fontHeight,fontMetrics.charWidth(bufferToDisplay[cursorRow][cursorCol]), fontHeight);

        int y = baseLineOffset;
        for (int lineIndex = 0; lineIndex < bufferToDisplay.length; lineIndex++) {
            char[] toDisplay = new char[bufferToDisplay[lineIndex].length + 1];
            System.arraycopy(bufferToDisplay[lineIndex], 0, toDisplay, 0, bufferToDisplay[lineIndex].length);
            for (int i = bufferToDisplay[lineIndex].length; i < toDisplay.length; i++) {
                toDisplay[i] = ' ';
            }
            g.drawChars(toDisplay, 0, toDisplay.length, 0, y);
            y += fontHeight;
        }
    }

    /**
     * @return the Dimensions of our window/display
     */
    @Override
    public Dimension getPreferredSize() {
        FontMetrics fontMetrics = this.getFontMetrics(getFont());
        int width = fontMetrics.charWidth('m');
        return new Dimension(width * bufferToDisplay[0].length, fontMetrics.getHeight() * bufferToDisplay.length);
    }

    /**
     * our window is always focusable
     * @return true
     */
    @Override
    public boolean isFocusable() {
        return true;
    }

    /**
     * add r to our resizeListeners so it gets an update whenever the window is resized
     * @param r the ResizeListener
     */
    public void subscribeToResize(ResizeListener r) {
        this.resizeListeners.add(r);
    }
}
