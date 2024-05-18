package controller.adapter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import util.Coords;
import ui.swing.SwingTerminal;
import controller.TSMediator;

public class SwingTerminalAdapter extends JFrame implements TermiosTerminalAdapter {
    private SwingTerminal terminal = new SwingTerminal();
    //Timer timer = new Timer(1000, e -> updateBuffer());
    int starRow = 0;
    int starCol = 0;
    long startTime = System.currentTimeMillis();
    private Queue<Integer> keyQueue = new LinkedList<Integer>();
    private final TSMediator ctl;

    public SwingTerminalAdapter(TSMediator med) {
	super("Swing Terminal App");
	this.ctl = med;
	//setDefaultCloseOperation(null);
	
	getContentPane().add(terminal);
	updateBuffer();
	
	terminal.resizeListeners.add(() -> updateBuffer());
	terminal.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
		    keyQueue.add((int) e.getKeyChar());
		}
	});
	this.addWindowFocusListener(new WindowAdapter () {
	      public void windowGainedFocus(WindowEvent e) {
		  System.out.println("gained focus");
		  ctl.gainFocus();
	      }
	});
	this.addWindowFocusListener(new WindowAdapter () {
	      public void windowLostFocus(WindowEvent e) {
		  System.out.println("Lost focus");
		  ctl.loseFocus();
	      }
	});
	
	//timer.start();
	
	pack();
	
	setLocationRelativeTo(null);
	java.awt.EventQueue.invokeLater(() -> this.setVisible(true));
    }

  void updateBuffer() {
      //terminal.clearBuffer();
      terminal.repaint();
  }

  @Override
  public void clearScreen() {
    terminal.clearBuffer();
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
      terminal.setX(column);
      terminal.setY(row);
      terminal.repaint();
  }

  @Override
  public void printText(int row, int column, String text){
	terminal.resize();
      terminal.addString(column, row, text);
    terminal.repaint();
  }

  @Override
  public int readByte() throws IOException {
      while (keyQueue.isEmpty()) {
	  try {
	      TimeUnit.MILLISECONDS.sleep(10);
	  } catch (Exception e) {
	      System.out.println("sleep failed");
	      System.exit(1);
	  }
      }
      return keyQueue.remove();
  }

  /*
   * less than perfect, but how to get the input directly here?
   */
  @Override
  public int readByte(long deadline) throws IOException, TimeoutException{
      while (keyQueue.isEmpty()) {
	  try {
	      TimeUnit.MILLISECONDS.sleep(10);
	  } catch (Exception e) {
	      System.out.println("sleep failed");
	      System.exit(1);
	  }
      }// TODO: deadline
      return keyQueue.remove();
  }

  @Override
  public Coords getTextAreaSize() throws IOException {
      return terminal.getTextAreaSize();
  }

}
