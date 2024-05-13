package ui;

import util.Coords;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

//TODO: make methods protected/private? But depends on where we use this class
public class TerminalPanel extends JPanel {
    public char[][] buffer = new char[25][80];

    public void clearBuffer() {
        for (int i = 0; i < buffer.length; i++)
            Arrays.fill(buffer[i], ' ');
    }

    public TerminalPanel() {
        setFont(new Font("Monospaced", Font.PLAIN, 12));
        clearBuffer();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fontMetrics = g.getFontMetrics();
        int fontHeight = fontMetrics.getHeight();
        int baseLineOffset = fontHeight - fontMetrics.getDescent();

        int y = baseLineOffset;
        for (int lineIndex = 0; lineIndex < buffer.length; lineIndex++) {
            g.drawChars(buffer[lineIndex], 0, buffer[lineIndex].length, 0, y);
            y += fontHeight;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fontMetrics = this.getFontMetrics(getFont());
        int width = fontMetrics.charWidth('m');
        return new Dimension(width * buffer[0].length, fontMetrics.getHeight() * buffer.length);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    public Coords getCoords() {
        return new Coords(0,0,buffer[0].length, buffer.length);
    }
}