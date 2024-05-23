package inputhandler;

public interface InputHandlerVisitor {
    void visitFileInputHandler(FileBufferInputHandler fh);
    void visitSnakeInputHandler(SnakeInputHandler sh);
    void visitDirectoryInputHandler(DirectoryInputHandler dh);
}
