package controller.adapter;

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

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import util.Coords;
import ui.swing.SwingTerminal;

public class SwingTerminalAdapter extends JFrame implements TermiosTerminalAdapter {
    private SwingTerminal terminal = new SwingTerminal();
    Timer timer = new Timer(1000, e -> updateBuffer());
    int starRow = 0;
    int starCol = 0;
    long startTime = System.currentTimeMillis();

    public SwingTerminalAdapter() {
	super("Swing Terminal App");
	//setDefaultCloseOperation(null);
	
	getContentPane().add(terminal);
	updateBuffer();
	
	terminal.resizeListeners.add(() -> updateBuffer());
	
	timer.start();
	
	pack();
	
	setLocationRelativeTo(null);
	java.awt.EventQueue.invokeLater(() -> this.setVisible(true));
    }

  void updateBuffer() {
      terminal.clearBuffer();
      String usageMessage = "Use the arrow keys to move the star; press Ctrl+R to reset the timer";
      usageMessage.getChars(0, Math.min(usageMessage.length(), terminal.buffer[0].length), terminal.buffer[terminal.buffer.length - 1], 0);
      String timeMessage = ((System.currentTimeMillis() - startTime) / 1000) + "s passed";
      timeMessage.getChars(0, Math.min(timeMessage.length(), terminal.buffer[0].length), terminal.buffer[0], terminal.buffer[0].length - timeMessage.length());
      terminal.buffer[starRow][starCol] = '*';
      terminal.repaint();
  }
  @Override
  public void clearScreen() {
		terminal.clearBuffer();
    terminal.repaint();
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
      terminal.setY(row);
      terminal.setX(column);
  }

  @Override
  public void printText(int row, int column, String text){

  }

  @Override
  public int readByte() throws IOException {
      return 0;
  }

  @Override
  public int readByte(long deadline) throws IOException, TimeoutException{
      return 0;
  }

  @Override
  public Coords getTextAreaSize() throws IOException {
      return terminal.getTextAreaSize();
  }
}
