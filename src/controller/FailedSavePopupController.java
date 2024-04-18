package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class FailedSavePopupController extends UseCaseController {

    boolean needsRenderSinceLast;

    UserPopupBox userPopupBox = new UserPopupBox("Error: save failed. Press any key to continue.");

    /**
     * @param coreControllerParent
     */
    protected FailedSavePopupController(TextR coreControllerParent) {
        super(coreControllerParent);
        needsRenderSinceLast = true;
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
        clearContent();
        userPopupBox.render();
        needsRenderSinceLast = false;
    }

    /**
     * remove the popup
     * @throws IOException
     */
    public void clearContent() throws IOException {
        coreControllerParent.adapter.clearScreen();
    }

    @Override
    public void handleIdle() {

    }

    @Override
    public boolean getNeedsRenderSinceLast() {
        return needsRenderSinceLast;
    }
}
