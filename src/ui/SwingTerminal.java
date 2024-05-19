package ui;

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
import java.util.Arrays;

import util.Coords;

public class SwingTerminal extends JPanel {
	private char[][] buffer = new char[25][80];
	public ArrayList<Runnable> resizeListeners = new ArrayList<>();
	private int x = 0, y = 0; // cursor
	
	public void clearBuffer() {
		for (int i = 0; i < buffer.length; i++)
			Arrays.fill(buffer[i], ' ');
	}

	public void setX(int x) { this.x = x; }

	public void setY(int y) { this.y = y; }
	
	public void SwingTerminal() {
		setFont(new Font("Monospaced", Font.PLAIN, 12));
		clearBuffer();
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				FontMetrics metrics = SwingTerminal.this.getFontMetrics(getFont());
				int h = SwingTerminal.this.getHeight() / metrics.getHeight();
				int w = SwingTerminal.this.getWidth() / metrics.charWidth('m');
				buffer = new char[h][w];
				List<Runnable> resizeListenersCopy = List.copyOf(resizeListeners);
				for (Runnable listener : resizeListenersCopy)
					listener.run();
			}
		});
	}

	public void resize() {
	    FontMetrics metrics = SwingTerminal.this.getFontMetrics(getFont());
	    int h = SwingTerminal.this.getHeight() / metrics.getHeight();
	    int w = SwingTerminal.this.getWidth() / metrics.charWidth('m');
	    if (h != buffer.length || w != buffer[0].length){
		buffer = new char[h][w];
		clearBuffer();
	    }
	}
	
	@Override
	protected void paintComponent(Graphics g) {
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

	public Coords getTextAreaSize() {
		return new Coords(0, 0, buffer[0].length, buffer.length);
	}
	
	@Override
	public boolean isFocusable() {
		return true;
	}

	public void addString(int x, int y, String in){
	    char[] in2 = in.toCharArray();
	    if (y >= buffer.length || x >= buffer[y].length)
		return;
	    for (int i = 0; i < in2.length && i + x < buffer[y].length; i++)
		buffer[y][x+i] = in2[i];
	}
}
