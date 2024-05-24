package listeners;

import window.Window;

public interface OpenWindowRequestListener {

    /**
     * Listener for when a window is to be opened
     * @param window the window to be opened
     */
    void openWindow(Window window);

}
