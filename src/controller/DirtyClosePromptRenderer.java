package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class DirtyClosePromptRenderer implements ViewRenderer{

    ControllerFacade controllerFacade;
    UserPopupBox userPopupBox = new UserPopupBox("Error: save failed. Press any key to continue.");
    @Override
    public void paintView() throws IOException {
        controllerFacade.getTermiosTerminalAdapter().clearScreen();
        userPopupBox.render();
    }

    public DirtyClosePromptRenderer(ControllerFacade controllerFacade){
        this.controllerFacade = controllerFacade;
    }
}
