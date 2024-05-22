package controller;

import controller.adapter.TermiosTerminalAdapter;
import util.RenderIndicator;
import ui.UserPopupBox;

import java.io.IOException;

public class FileErrorPopupController extends UseCaseController {
    UserPopupBox userPopupBox = new UserPopupBox("Error: invalid file");

    public FileErrorPopupController(TextR coreControllerParent, TermiosTerminalAdapter termiosTerminalAdapter) {
        super(coreControllerParent, null);
    }

    /**
     * pass the input to the correct controller
     * @param b the int input
     * @throws IOException
     */
    @Override
    public RenderIndicator handle(int b) throws IOException {
        Runtime.getRuntime().halt(1);
        return null;
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

    /**
     * remove the popup
     * @throws IOException
     */
    public void clearContent() throws IOException {
        coreControllerParent.getAdapter().clearScreen();
    }

}
