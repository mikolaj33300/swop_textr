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
    char[][] bufferToDisplay = new char[1][1];
    ArrayList<ResizeListener> resizeListeners = new ArrayList<>();

    void clearBuffer() {
        for (int i = 0; i < bufferToDisplay.length; i++)
            Arrays.fill(bufferToDisplay[i], ' ');
    }

    public void setNewBuffer(char[][] newBuffer) {
        bufferToDisplay = new char[newBuffer.length][newBuffer[0].length];
        for (int i = 0; i < newBuffer.length; i++) {
            for (int j = 0; j < newBuffer[0].length; j++) {
                bufferToDisplay[i][j] = newBuffer[i][j];
            }
        }
    }

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int baseLineOffset = fontHeight - fontMetrics.getDescent();

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

    public void subscribeToResize(ResizeListener r) {
        this.resizeListeners.add(r);
    }
}
