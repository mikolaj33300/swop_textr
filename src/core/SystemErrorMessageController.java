package core;

import ui.UserPopupBox;

import java.io.IOException;

public class SystemErrorMessageController extends UseCaseController {
    UserPopupBox userPopupBox = new UserPopupBox("Error: invalid file provided");

    protected SystemErrorMessageController(TextR coreControllerParent) {
        super(coreControllerParent);
    }

    @Override
    public void handle(int b) throws IOException {
        System.exit(1);
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