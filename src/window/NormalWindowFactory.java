package window;

import directory.directorytree.FileSystemEntry;
import files.OpenFileOnPathRequestListener;
import inputhandler.*;
import ioadapter.TermiosTerminalAdapter;

import java.io.IOException;

//This is so that windows can create other windows when needed to request to the facade to open one.
//But we don't want windows to know about the specific types of other windows. (eg. a file window may request
//to create a directory window, and it's okay if it knows that it can make such a request and takes part in the flow,
//but not the implementation and specific classes for directories, as these can get changed, deleted etc.)
public class NormalWindowFactory {
    public Window createWindowOnPath(String path, byte[] lineSeparatorArg, TermiosTerminalAdapter adapter) throws IOException {
            return new FileBufferWindow(path, lineSeparatorArg, adapter);
    }

    public Window createSnakeGameWindow(int width, int height, TermiosTerminalAdapter termiosTerminalAdapter) throws IOException {
            return new SnakeWindow(width, height, termiosTerminalAdapter);
    }

    public Window createDirectoryOnFileStructure(FileSystemEntry entry, TermiosTerminalAdapter adapter){
            return new DirectoryWindow(new DirectoryInputHandler(entry), adapter);
    }


    //By using an inputhandler visitor, Windows that use this factory:
    // -dont need to know about low level model objects
    // -dont need to know about other types of Windows
    //And while passing objects around, an inputhandler can create a generic InputHandlingElement without knowing
    //its type, to create a window for it without the window above having to know.
    public Window createWindowFromInputHandler(InputHandlingElement ih, TermiosTerminalAdapter adapter){
        final Window[] windowToReturn = new Window[1];
        ih.accept(new InputHandlerVisitor() {
            @Override
            public void visitFileInputHandler(FileBufferInputHandler fh) {
                windowToReturn[0]=new FileBufferWindow(fh, adapter);
            }

            @Override
            public void visitSnakeInputHandler(SnakeInputHandler sh) {
                //idem
            }

            @Override
            public void visitDirectoryInputHandler(DirectoryInputHandler dh) {
                windowToReturn[0] = new DirectoryWindow(dh, adapter);
            }

        });
        return windowToReturn[0];
    }

    public Window createRealDirectory(OpenFileOnPathRequestListener listener, TermiosTerminalAdapter adapter) {
        return new DirectoryWindow(listener, adapter);
    }
}
