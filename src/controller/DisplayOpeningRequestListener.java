package controller;

import java.io.IOException;

public interface DisplayOpeningRequestListener {
    public void notifyRequestOpenDisplay(DisplayFacade displayToOpen) throws IOException;
    public DisplayFacade getListenedToDisplay();
}
