package directory;

abstract class dirEntry {
 abstract public String getParent();
 abstract public dirEntry[] listFiles();
 abstract public boolean isHidden();
 abstract public boolean isDirectory();
 abstract public String getName();
}
