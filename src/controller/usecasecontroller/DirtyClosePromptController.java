package controller.usecasecontroller;

import controller.ControllerFacade;
import controller.TextR;
import io.github.btj.termios.Terminal;
import util.GlobalCloseStatus;
import util.RenderIndicator;
import ui.UserPopupBox;

import java.io.IOException;

public class DirtyClosePromptController extends UseCaseController {
    /**
     * The pop up displayed upon entering this controller
     */
    UserPopupBox userPopupBox = new UserPopupBox("Unsaved changes will be lost. Continue? (Y/N)", coreControllerParent.getAdapter());

    /**
     * Constructor for the DirtyClosePromptController
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
                coreControllerParent.setActiveUseCaseController(new InspectContentsController(coreControllerParent, facade));
                return;
            // Y
            case 121:
                GlobalCloseStatus result = facade.forceCloseActive().b;
                if(result != GlobalCloseStatus.CLOSED_ALL_DISPLAYS){
                    unsubscribeFromFacadeAscii();
                    coreControllerParent.setActiveUseCaseController(new InspectContentsController(coreControllerParent, facade));
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
