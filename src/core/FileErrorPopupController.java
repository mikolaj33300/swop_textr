package core;

import ui.UserPopupBox;

import java.io.IOException;

public class FileErrorPopupController extends UseCaseController {
    UserPopupBox userPopupBox = new UserPopupBox("Error: invalid file");

    protected FileErrorPopupController(TextR coreControllerParent) {
        super(coreControllerParent);
    }

    @Override
    public void handle(int b) throws IOException {
        Runtime.getRuntime().halt(1);
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