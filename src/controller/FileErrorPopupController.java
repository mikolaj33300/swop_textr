package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class FileErrorPopupController extends UseCaseController {
    UserPopupBox userPopupBox = new UserPopupBox("Error: invalid file");

    protected FileErrorPopupController(TextR coreControllerParent) {
        super(coreControllerParent);
    }

    /**
     * pass the input to the correct controller
     * @param b the int input
     * @throws IOException
     */
    @Override
    public void handle(int b) throws IOException {
        Runtime.getRuntime().halt(1);
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
