package window;

import java.io.IOException;

/**
 * Will visit implementations of {@link Window} objects.
 */
public interface WindowVisitor {

    /**
     * Visitor for a FileWindow
     * @param fbw
     */
    void visitFileWindow(FileBufferWindow fbw) throws IOException;

    /**
     * Visitor for a SnakeWindow
     * @param sw
     */
    void visitSnakeWindow(SnakeWindow sw);

    /**
     * Visitor for a DirectoryWindow
     * @param dw
     */
    void visitDirectoryWindow(DirectoryWindow dw);
}
