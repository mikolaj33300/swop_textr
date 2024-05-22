package window;

import java.io.IOException;

/**
 * Will visit implementations of {@link Window} objects.
 */
public interface WindowVisitor {
    void visitFileWindow(FileBufferWindow fbw) throws IOException;
    void visitSnakeWindow(SnakeWindow sw);
}
