package directory;

import files.FileHolder;

public interface FileCreatorVisitor {

    FileHolder createFile(FileCreator creator);

}
