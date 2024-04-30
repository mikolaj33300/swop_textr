package dir;

import java.nio.file.Files;

public class directory {
  private final string path;

  public directory (string path) {
    this.path = path;
  }

  private string getFilesList () {
    File folder = new File(this.path);
    string res = "";
    
    for (final File fileEntry : folder.listFiles()) {
      res += fileEntry.getName();
    }

    return res;
  }

}
