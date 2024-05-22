package controller.usecasecontroller;

import controller.ControllerFacade;
import controller.TextR;
import controller.usecasecontroller.InspectContentsController;
import controller.usecasecontroller.UseCaseController;
import ui.UserPopupBox;

import java.io.IOException;

public class FailedSavePopupController extends UseCaseController {

    boolean needsRenderSinceLast;

    UserPopupBox userPopupBox = new UserPopupBox("Error: save failed. Press any key to continue.");

    /**
     * @param coreControllerParent
     */
    protected FailedSavePopupController(TextR coreControllerParent, ControllerFacade facade) {
        super(coreControllerParent, facade);
    }

    /**
     * pass the input to the correct controller
     *
     * @param b the int input
     * @throws IOException
     */
    @Override
    public void handle(int b) throws IOException {
        unsubscribeFromFacadeAscii();
        coreControllerParent.setActiveUseCaseController(new InspectContentsController(coreControllerParent, facade));
    }

    /**
     * render the popup
     * @throws IOException
     */
    @Override
    public void paintScreen() throws IOException {
        clearContent();
        userPopupBox.render();
        needsRenderSinceLast = false;
    }

    /**
     * remove the popup
     * @throws IOException
     */
    public void clearContent() throws IOException {
        coreControllerParent.getAdapter().clearScreen();
    }
}
