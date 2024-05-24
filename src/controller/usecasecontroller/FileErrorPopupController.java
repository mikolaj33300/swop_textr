package controller.usecasecontroller;

import controller.TextR;
import ioadapter.TermiosTerminalAdapter;
import ui.UserPopupBox;

import java.io.IOException;

public class FileErrorPopupController extends UseCaseController {
    UserPopupBox userPopupBox = new UserPopupBox("Error: invalid file", coreControllerParent.getAdapter());

    public FileErrorPopupController(TextR coreControllerParent, TermiosTerminalAdapter termiosTerminalAdapter) {
        super(coreControllerParent, null);
    }

    /**
     * pass the input to the correct controller
     *
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
        coreControllerParent.getAdapter().clearScreen();
        userPopupBox.render();
    }

}