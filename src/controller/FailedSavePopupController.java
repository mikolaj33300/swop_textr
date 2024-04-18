package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class FailedSavePopupController extends UseCaseController {

    boolean needsRenderSinceLast;

    UserPopupBox userPopupBox = new UserPopupBox("Error: save failed. Press any key to continue.");

    protected FailedSavePopupController(TextR coreControllerParent) {
        super(coreControllerParent);
        needsRenderSinceLast = true;
    }

    @Override
    public void handle(int b) throws IOException {
        coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
    }

    @Override
    public void paintScreen() throws IOException {
        clearContent();
        userPopupBox.render();
        needsRenderSinceLast = false;
    }

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
