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

    @Override
    public void handle(int b) throws IOException {
        Runtime.getRuntime().halt(1);
        this.needsRenderSinceLast = true;
    }

    @Override
    public void paintScreen() throws IOException {
        coreControllerParent.adapter.clearScreen();
        userPopupBox.render();
        this.needsRenderSinceLast = false;
    }

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