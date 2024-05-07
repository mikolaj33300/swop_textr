package directory;

import directory.directorytree.DirEntry;
import directory.directorytree.FileEntry;

import java.io.File;
import java.util.ArrayList;

public class Directory {
    private String path;
    private int x, y;
    private boolean hidden = false;
    private ArrayList<String> files;
    private DirEntry crnt;

    public Directory(String path) {
        this.path = path;
        this.crnt = new FileEntry(path);
    }

    public void updateFiles() {
        this.files = new ArrayList<String>(10);

        if (crnt.getParent() != null)
            this.files.add("../");
        for (final DirEntry fileEntry : crnt.listFiles()) {
            if (!fileEntry.isHidden() || hidden)
                if (fileEntry.isDirectory())
                    this.files.add(fileEntry.getName() + "/");
                else
                    this.files.add(fileEntry.getName());
        }
    }

    public ArrayList<String> getFilesList() {
        if (this.files == null) updateFiles();

        return this.files;
    }

    public void enterDir() {
        String dir = getCrnt();
        if (dir.equals("../")) {
            File tmp = new File(path);
            path = tmp.getParent();
        } else if (dir.substring(dir.length() - 1).equals("/")) {
            path += "/" + dir.substring(0, dir.length() - 1);
        }
        crnt = new FileEntry(path);
        updateFiles();
        x = 0;
        y = 0;
    }

    /**
     * Vond ik leuk om te doen, groetjes tom
     */
    public void toggleHide() {
        hidden = !hidden;
        updateFiles();
    }

    public String getCrnt() {
        return files.get(y);
    }

    private void cToEnd() {
        if (x > files.get(y).length()) x = files.get(y).length() - 1;
    }

    public void scrollDown() {
        if (y < files.size() - 1) {
            y++;
            cToEnd();
        }
    }

    public void scrollUp() {
        if (y > 0) {
            y--;
            cToEnd();
        }
    }

    public void scrollLeft() {
        if (x > 0) x--;
    }

    public void scrollRight() {
        if (x < files.get(y).length() - 1) x++;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getPath() {
        return path;
    }
}

