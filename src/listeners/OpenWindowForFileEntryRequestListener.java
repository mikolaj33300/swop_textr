package listeners;

import directory.directorytree.FileSystemEntry;

public interface OpenWindowForFileEntryRequestListener {
    public void notifyRequestToOpenWindow(FileSystemEntry entry);
}
