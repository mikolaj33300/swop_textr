package directory;

import listeners.directoryListener;

import java.nio.file.Files;
import java.io.File;
import java.util.ArrayList;

public class directory {
  private final String path;
  private ArrayList<directoryListener> rListener;

  public directory (String path) {
    this.path = path;
  }

  private String getFilesList () {
    File folder = new File(this.path);
    String res = "";
    
    for (final File fileEntry : folder.listFiles()) {
      res += fileEntry.getName();
    }

    return res;
  }

  public void addListener(directoryListener lsnr) {
    this.rListener.add(this.rListener.size(), lsnr);
  }

  public void render(int hash) {
    String cntnt = getFilesList();
    for (directoryListener lr : this.rListener) {
      lr.setContent(cntnt);
      lr.render(hash);
    }
  }
}
