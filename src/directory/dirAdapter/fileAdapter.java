package directory;

import java.util.ArrayList;
import java.nio.file.Files;
import java.io.File;

class fileAdapter extends dirEntry {
  private File f;

  public fileAdapter(String p) { this.f = new File(p); }
  public String getParent() { return f.getParent(); }
  public dirEntry[] listFiles() { 
    ArrayList<fileAdapter> res = new ArrayList<fileAdapter>(10);
    for (final File file : f.listFiles())
      res.add(new fileAdapter(file.getName()));
    return res.toArray(new dirEntry[res.size()]);
  }
  public boolean isHidden() { return f.isHidden(); }
  public boolean isDirectory() { return f.isDirectory(); }
  public String getName() { return f.isDirectory() ? f.getName() + "/" : f.getName(); }
}
