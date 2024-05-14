package superWindow;

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

public class superWindow {
  private Layout rootLayout;
  private ArrayList windows;
  private int active;
  private TermiosTerminalAdapter swingTerminalAdapter;// swing adapter
  private superWindow next == null;// linked list

  public void addWindow(){
    
  }

  private boolean isActive() {

  }

  public void renderAll() { windows.renderAll(); }
  public void save() { windows.save(active); }
  public void renderCursor() { windows.renderCursor(active); }
  public void forceClose() { windows.forceClose(active); }
}
