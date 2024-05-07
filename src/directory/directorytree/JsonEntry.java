package directory.directorytree;

import directory.directorytree.DirEntry;

import java.util.ArrayList;

class JsonEntry extends DirEntry {
    private ArrayList<JsonEntry> children;
    private JsonEntry parent;
    private String name;
    private boolean nested;

    public JsonEntry(String p) {
        name = p;
    }

    public String getParent() {
        return parent.getName();
    }

    public DirEntry[] listFiles() {
        ArrayList<JsonEntry> res = new ArrayList<JsonEntry>(10);
        for (final JsonEntry file : children)
            res.add(new JsonEntry(file.getName()));
        return (DirEntry[]) res.toArray();
    }

    public boolean isHidden() {
        return false;
    }

    public boolean isDirectory() {
        return nested;
    }

    public String getName() {
        return name;
    }

}