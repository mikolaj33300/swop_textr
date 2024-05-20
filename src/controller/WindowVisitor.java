package controller;

import java.io.IOException;

public interface WindowVisitor {
    public void visitFileWindow(FileBufferWindow fbw) throws IOException;
    public void visitSnakeWindow(SnakeWindow sw);
}
