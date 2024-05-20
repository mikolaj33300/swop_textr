package controller;

import util.RenderIndicator;
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
    public RenderIndicator handle(int b) throws IOException {
        coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
        return RenderIndicator.FULL;
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
