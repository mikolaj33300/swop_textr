package directory;

import directory.directorytree.FileEntry;
import directory.directorytree.JsonFileEntry;
import files.FileHolder;

public class FileCreator {

    /**
     * Visits the {@link FileEntry} to create a {@link files.FileHolder}
     * @param entry the {@link FileEntry} where we will extract information from to create a {@link files.FileHolder}
     */
    public FileHolder createDefault(FileEntry entry) {
        return null;
    }

    /**
     * Visits the {@link JsonFileEntry} to create a {@link files.JsonFileHolder}
     * @param entry the {@link JsonFileEntry} where we will extract information from to create a {@link files.JsonFileHolder}
     */
    public FileHolder createJson(JsonFileEntry entry) {
        return null;
    }

}
