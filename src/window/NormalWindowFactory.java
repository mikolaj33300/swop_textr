package window;

import ioadapter.TermiosTerminalAdapter;

import java.io.IOException;

public class NormalWindowFactory {
    public Window createWindowOnPath(String path, byte[] lineSeparatorArg, TermiosTerminalAdapter adapter) throws IOException {
            return new FileBufferWindow(path, lineSeparatorArg, adapter);
    }

    public Window createSnakeGameWindow(int width, int height, TermiosTerminalAdapter termiosTerminalAdapter) throws IOException {
            return new SnakeWindow(width, height, termiosTerminalAdapter);
    }
}
