package controller;

import io.github.btj.termios.Terminal;
import util.RenderIndicator;
import ui.UserPopupBox;

import java.io.IOException;

public class DirtyClosePromptController extends UseCaseController {
    private boolean needsRenderSinceLast;
    UserPopupBox userPopupBox = new UserPopupBox("Unsaved changes will be lost. Continue? (Y/N)");

    /**
     * @param coreControllerParent
     */
    protected DirtyClosePromptController(TextR coreControllerParent) {
        super(coreControllerParent);
        this.needsRenderSinceLast = true;
    }

    /**
     * pass the input to the correct controller
     * @param b the int input
     * @throws IOException
     */
    @Override
    public RenderIndicator handle(int b) throws IOException {
        switch(b) {
            // N
            case 110:
                coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
                break;
            // Y
            case 121:
                int result = coreControllerParent.facade.forceCloseActive().b;
                if(result == 0){
                    coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent);
                } else {
                    Terminal.clearScreen();
                    System.exit(0);
                }
                break;
        }
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
        this.needsRenderSinceLast = false;
    }

    /**
     * remove the popup
     * @throws IOException
     */
    public void clearContent() throws IOException {
        coreControllerParent.getAdapter().clearScreen();
    }
}
