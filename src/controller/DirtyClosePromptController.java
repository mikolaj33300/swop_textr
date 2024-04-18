package controller;

import io.github.btj.termios.Terminal;
import ui.UserPopupBox;

import java.io.IOException;

public class DirtyClosePromptController extends UseCaseController {
    private boolean needsRenderSinceLast;
    UserPopupBox userPopupBox = new UserPopupBox("Unsaved changes will be lost. Continue? (Y/N)");

    protected DirtyClosePromptController(TextR coreControllerParent) {
        super(coreControllerParent);
        this.needsRenderSinceLast = true;
    }

    @Override
    public void handle(int b) throws IOException {
        switch(b) {
            // N
            case 110:
                coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
                break;
            // Y
            case 121:
                int result = coreControllerParent.facade.forceCloseActive();
                if(result == 0){
                    coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
                } else {
                    Terminal.clearScreen();
                    System.exit(0);
                }
                break;
        }
    }

    @Override
    public void paintScreen() throws IOException {
        clearContent();
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
        return needsRenderSinceLast;
    }
}
