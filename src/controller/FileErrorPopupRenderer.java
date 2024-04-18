package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class FileErrorPopupRenderer implements ViewRenderer {
    ControllerFacade controllerFacade;
    UserPopupBox userPopupBox = new UserPopupBox("Error: invalid file");

    public FileErrorPopupRenderer(ControllerFacade controllerFacade) {
        this.controllerFacade = controllerFacade;
    }

    @Override
    public void paintView() throws IOException {
        controllerFacade.getTermiosTerminalAdapter().clearScreen();
        userPopupBox.render();
    }
}
