package controller;

import ui.UserPopupBox;

import java.io.IOException;

public class DirtyClosePromptController extends UseCaseController {
    UserPopupBox userPopupBox = new UserPopupBox("Unsaved changes will be lost. Continue? (Y/N)");

    protected DirtyClosePromptController(TextR coreControllerParent) {
        super(coreControllerParent);
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
                int result = coreControllerParent.rootLayout.forcedCloseActive();
                if(result == 0){
                    coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
                } else {
                    System.exit(0);
                }
                break;
        }
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
