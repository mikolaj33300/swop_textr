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
import javax.swing.Timer;

class TerminalPanel extends JPanel {
	char[][] buffer = new char[25][80];
	ArrayList<Runnable> resizeListeners = new ArrayList<Runnable>();
	
	void clearBuffer() {
		for (int i = 0; i < buffer.length; i++)
			Arrays.fill(buffer[i], ' ');
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
				buffer = new char[nbRows][nbCols];
				List<Runnable> resizeListenersCopy = List.copyOf(resizeListeners);
				for (Runnable listener : resizeListenersCopy)
					listener.run();
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
}

public class SwingTerminalApp extends JFrame {
	
	TerminalPanel terminalPanel = new TerminalPanel();
	
	int starRow = 0;
	int starCol = 0;
	long startTime = System.currentTimeMillis();
	Timer timer = new Timer(1000, e -> updateBuffer());
	
	void updateBuffer() {
		terminalPanel.clearBuffer();
		String usageMessage = "Use the arrow keys to move the star; press Ctrl+R to reset the timer";
		usageMessage.getChars(0, Math.min(usageMessage.length(), terminalPanel.buffer[0].length), terminalPanel.buffer[terminalPanel.buffer.length - 1], 0);
		String timeMessage = ((System.currentTimeMillis() - startTime) / 1000) + "s passed";
		timeMessage.getChars(0, Math.min(timeMessage.length(), terminalPanel.buffer[0].length), terminalPanel.buffer[0], terminalPanel.buffer[0].length - timeMessage.length());
		terminalPanel.buffer[starRow][starCol] = '*';
		terminalPanel.repaint();
	}
	
	SwingTerminalApp() {
		super("Swing Terminal App");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		getContentPane().add(terminalPanel);
		updateBuffer();
		
		terminalPanel.resizeListeners.add(() -> updateBuffer());
		
		terminalPanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_H -> {
					starCol = Math.floorMod(starCol - 1, terminalPanel.buffer[0].length);
				}
				case KeyEvent.VK_L -> {
					starCol = Math.floorMod(starCol + 1, terminalPanel.buffer[0].length);
				}
				case KeyEvent.VK_K -> {
					starRow = Math.floorMod(starRow - 1, terminalPanel.buffer.length);
				}
				case KeyEvent.VK_J -> {
					starRow = Math.floorMod(starRow + 1, terminalPanel.buffer.length);
				}
				case KeyEvent.VK_R -> {
					if ((e.getModifiersEx() & (KeyEvent.SHIFT_DOWN_MASK | KeyEvent.ALT_DOWN_MASK | KeyEvent.CTRL_DOWN_MASK)) == KeyEvent.CTRL_DOWN_MASK) {
						startTime = System.currentTimeMillis();
					}
				}
				}
				updateBuffer();
			}
		});
		
		timer.start();
		
		pack();
		
		setLocationRelativeTo(null);
	}
	
	private static void awtMain(String[] args) {
		new SwingTerminalApp().setVisible(true);
	}
	
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(() -> awtMain(args));
	}

}
