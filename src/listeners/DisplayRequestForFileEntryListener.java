package listeners;

import directory.directorytree.FileSystemEntry;

public interface DisplayRequestForFileEntryListener {
    public void notifyRequestToOpen(FileSystemEntry entry);
}
