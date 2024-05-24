package controller.usecasecontroller;

import controller.ControllerFacade;
import controller.TextR;
import ui.UserPopupBox;

import java.io.IOException;

public class FailedSavePopupController extends UseCaseController {

    /**
     * The popup displayed upon entering this controller
     */
    UserPopupBox userPopupBox = new UserPopupBox("Error: save failed. Press any key to continue.", coreControllerParent.getAdapter());

    /**
     * Constructor for the FailedSavePopupController
     * @param coreControllerParent the parent controller
     * @param facade the ControllerFacade linked to this FailedSavePopupController
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
        userPopupBox.render();
    }

}
