package directory;

import java.util.ArrayList;

class jsonAdapter extends dirEntry {
  private ArrayList<jsonAdapter> children;
  private jsonAdapter parent;
  private String name;
  private boolean nested;

  public jsonAdapter(String p) { name = p; }
  public String getParent() { return parent.getName(); }
  public dirEntry[] listFiles() { 
    ArrayList<jsonAdapter> res = new ArrayList<jsonAdapter>(10);
    for (final jsonAdapter file : children)
      res.add(new jsonAdapter(file.getName()));
    return (dirEntry[]) res.toArray();
  }
  public boolean isHidden() { return false; }
  public boolean isDirectory() { return nested; }
  public String getName() { return name; }
}
