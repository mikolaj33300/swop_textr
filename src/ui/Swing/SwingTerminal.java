package ui.Swing;

public class SwingTerminal extends JPanel {
	char[][] buffer = new char[25][80];
	ArrayList<Runnable> resizeListeners = new ArrayList<>();
	
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
