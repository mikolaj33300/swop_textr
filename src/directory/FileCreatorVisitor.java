package directory;

import directory.directorytree.FileCreator;
import files.FileHolder;

public interface FileCreatorVisitor {

    FileHolder createFile(FileCreator creator);

}
