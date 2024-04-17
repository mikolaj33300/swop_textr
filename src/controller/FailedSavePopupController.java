package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class FailedSavePopupController extends UseCaseController {

    UserPopupBox userPopupBox = new UserPopupBox("Error: save failed. Press any key to continue.");

    /**
     * @param coreControllerParent
     */
    protected FailedSavePopupController(TextR coreControllerParent) {
        super(coreControllerParent);
    }

    /**
     * pass the input to the correct controller
     * @param b the int input
     * @throws IOException
     */
    @Override
    public void handle(int b) throws IOException {
        coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
    }

    /**
     * render the popup
     * @throws IOException
     */
    @Override
    public void paintScreen() throws IOException {
        userPopupBox.render();
    }

    /**
     * remove the popup
     * @throws IOException
     */
    @Override
    public void clearContent() throws IOException {
        userPopupBox.clearContent();
    }

    @Override
    public void handleIdle() {

    }
}
