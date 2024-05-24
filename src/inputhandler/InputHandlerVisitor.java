package inputhandler;

public interface InputHandlerVisitor {
  /**
   * code to run on FileBufferInputHandler
   * @param fh
   */
    void visitFileInputHandler(FileBufferInputHandler fh);
    /**
     * Code to run on Snake
     * @param sh
     */
    void visitSnakeInputHandler(SnakeInputHandler sh);
    /**
     * Code to run on Directory
     * @param dh
     */
    void visitDirectoryInputHandler(DirectoryInputHandler dh);
}
