package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class FileErrorPopupController extends UseCaseController {
    UserPopupBox userPopupBox = new UserPopupBox("Error: invalid file");
    private boolean needsRenderSinceLast;

    protected FileErrorPopupController(TextR coreControllerParent) {
        super(coreControllerParent);
        this.needsRenderSinceLast=true;
    }

    /**
     * pass the input to the correct controller
     * @param b the int input
     * @throws IOException
     */
    @Override
    public void handle(int b) throws IOException {
        Runtime.getRuntime().halt(1);
        this.needsRenderSinceLast = true;
    }

    /**
     * render the popup
     * @throws IOException
     */
    @Override
    public void paintScreen() throws IOException {
        coreControllerParent.adapter.clearScreen();
        userPopupBox.render();
        this.needsRenderSinceLast = false;
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
        return this.needsRenderSinceLast;
    }
}
