package inputhandler;

import directory.directorytree.FileSystemEntry;
import files.BufferCursorContext;

public class InputhandlersFromModelObjectsFactory {
  /**
   * create FileBufferInputHandler out of a BufferCursorContext
   * @param b
   */
    public InputHandlingElement createFileInputHandler(BufferCursorContext b){
        return new FileBufferInputHandler(b);
    }

    /**
     * create DirectoryInputHandler out of a filesystementry
     * @param fe
     */
    public InputHandlingElement createDirectoryInputHandler(FileSystemEntry fe){
        //Or a Directory (that's what inputhandler takes as param)? Didn't implement directory
        //since the filesystementry entity is a less variable
        //thing than Directory (which is a frontend for a filesystementry). You can kindof compare the fe to a path
        //that also has to be passed around sometimes to open a file for it
        //return new DirectoryInputHandler(fe);
        return new DirectoryInputHandler(fe);
    }
}
