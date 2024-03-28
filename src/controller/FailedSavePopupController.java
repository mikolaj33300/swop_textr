package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class FailedSavePopupController extends UseCaseController {

    UserPopupBox userPopupBox = new UserPopupBox("Error: save failed. Press any key to continue.");

    protected FailedSavePopupController(TextR coreControllerParent) {
        super(coreControllerParent);
    }

    @Override
    public void handle(int b) throws IOException {
        coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
    }

    @Override
    public void render() throws IOException {
        userPopupBox.render();
    }

    @Override
    public void clearContent() throws IOException {
        userPopupBox.clearContent();
    }
}
