package controller;

import io.github.btj.termios.Terminal;
import util.RenderIndicator;
import ui.UserPopupBox;

import java.io.IOException;

public class DirtyClosePromptController extends UseCaseController {
    UserPopupBox userPopupBox = new UserPopupBox("Unsaved changes will be lost. Continue? (Y/N)");

    /**
     * @param coreControllerParent
     */
    protected DirtyClosePromptController(TextR coreControllerParent, ControllerFacade facade) {
        super(coreControllerParent, facade);
    }

    /**
     * pass the input to the correct controller
     *
     * @param b the int input
     * @throws IOException
     */
    @Override
    public void handle(int b) throws IOException {
        RenderIndicator opRenderIndicator = RenderIndicator.NONE;
        switch(b) {
            // N
            case 110:
                unsubscribeFromFacadeAscii();
                coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent, facade);
                coreControllerParent.activeUseCaseController.paintScreen();
                return;
            // Y
            case 121:
                int result = facade.forceCloseActive().b;
                if(result == 0){
                    unsubscribeFromFacadeAscii();
                    coreControllerParent.activeUseCaseController = new InspectContentsController(coreControllerParent, facade);
                    coreControllerParent.activeUseCaseController.paintScreen();
                    return;
                } else {
                    System.exit(0);
                }
                break;
        }
        if(opRenderIndicator != RenderIndicator.NONE){
            this.paintScreen();
        }
    }

    /**
     * render the popup
     * @throws IOException
     */
    @Override
    public void paintScreen() throws IOException {
        userPopupBox.render();
    }

}
