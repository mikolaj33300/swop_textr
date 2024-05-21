package controller;

import java.io.IOException;

public interface DisplayOpeningRequestListener {
    void notifyRequestOpenDisplay(DisplayFacade displayToOpen) throws IOException;
    DisplayFacade getListenedToDisplay();
}
