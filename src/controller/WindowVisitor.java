package controller;

import java.io.IOException;

/**
 * Will visit implementations of {@link Window} objects for duplication in {@link DisplayFacade#duplicateActive()}.
 * The {@link DisplayFacade.DuplicateWindowVisitor} is a specific implementation of {@link WindowVisitor}.
 */
public interface WindowVisitor {
    void visitFileWindow(FileBufferWindow fbw) throws IOException;
    void visitSnakeWindow(SnakeWindow sw);
}
