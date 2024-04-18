package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class FailedSavePopupRenderer implements ViewRenderer {
    ControllerFacade controllerFacade;
    UserPopupBox userPopupBox = new UserPopupBox("Error: save failed. Press any key to continue.");
    @Override
    public void paintView() throws IOException {
        controllerFacade.getTermiosTerminalAdapter().clearScreen();
        userPopupBox.render();
    }

    public FailedSavePopupRenderer(ControllerFacade controllerFacade){
        this.controllerFacade = controllerFacade;
    }
}
