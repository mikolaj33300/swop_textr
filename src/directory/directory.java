package directory;

import java.nio.file.Files;
import java.io.File;
import java.util.ArrayList;

public class directory {
  private final String path;
  private int x,y;
  private ArrayList<String> files;

  public directory (String path) {
    this.path = path;
  }

  private void updateFiles() {
    File folder = new File(this.path);
    this.files = new ArrayList<String>(10);
    
    for (final File fileEntry : folder.listFiles()) {
      this.files.add(fileEntry.getName());
    }
  }

  public ArrayList<String> getFilesList () {
    if (this.files == null) updateFiles();

    return this.files;
  }

  public String getCrnt() { return files.get(y); }

  private void cToEnd() { if (x > files.get(y).length()) x = files.get(y).length()-1; }
  public void scrollDown(){ if (y < files.size()-1) { y++; cToEnd(); } }
  public void scrollUp(){ if (y > 0) { y--; cToEnd(); } }
  public void scrollLeft() { if (x > 0) x--; }
  public void scrollRight() { if (x < files.get(y).length()-1) x++; }

  public int getX() { return x; }
  public int getY() { return y; }
}
