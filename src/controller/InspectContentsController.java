package controller;

import controller.adapter.TermiosTerminalAdapter;
import util.RenderIndicator;
import util.MoveDirection;
import util.RotationDirection;

import java.io.IOException;

public class InspectContentsController extends UseCaseController {


    protected InspectContentsController(TextR coreControllerParent, ControllerFacade facade){
        super(coreControllerParent, facade);
    }


    public InspectContentsController(TextR textR, String[] args, TermiosTerminalAdapter termiosTerminalAdapter) throws IOException {
        super(textR, new ControllerFacade(args, termiosTerminalAdapter));
    }

    /**
     * pass the input to the correct controller
     * @param b the int input
     * @throws IOException
     */
    @Override
    public RenderIndicator handle(int b) throws IOException {
        switch(b) {
            case 8, 127, 10, 62, 26, 21, 1, -1:
                return facade.passToActive((Integer.valueOf(b)).byteValue());
            // Control + S
            case 19:
                return facade.passToActive((Integer.valueOf(b)).byteValue());
            // Control + P
            case 16:
                return facade.moveFocus(MoveDirection.LEFT);
            // Control + N
            case 14:
                return facade.moveFocus(MoveDirection.RIGHT);
            // Control + R
            case 18:
                return facade.rotateLayout(RotationDirection.COUNTERCLOCKWISE);
                // Control + W
            case 23:
                return facade.openNewSwingFromActiveWindow();
            // Control + T
            case 20:
                return facade.rotateLayout(RotationDirection.CLOCKWISE);
            // Control + G
            case 7:
                return facade.openSnakeGame();
            // Control + D
            case 4:
                return facade.duplicateActive();
            // Line separator
            case 13:
                return facade.handleSeparator();
            // Character input
            default:
                if(b < 32 && b != 10 && b != 13 && b==26 && b!=21 || 127 <= b){
                    return RenderIndicator.NONE;
                }
                return facade.passToActive((Integer.valueOf(b)).byteValue());
        }
    }

    /**
     * pass the input to the correct controller
     * @param first the surrogate type
     * @param second the character after the surrogate
     */
    @Override
    public RenderIndicator handleSurrogate(int first, int second){
        // Surrogate keys
        switch (first) {
            case 27:
                switch ((char) second) {
                    // Right
                    case 'C':
                        return facade.handleArrowRight();
                    // Left
                    case 'D':
                        return facade.handleArrowLeft();
                    // Up
                    case 'A':
                        return facade.handleArrowUp();
                    // Down
                    case 'B':
                        return facade.handleArrowDown();
                    case 'S':// F4
                        int result = facade.closeActive().b;
                        if (result == 1) { //If was dirty
                            coreControllerParent.activeUseCaseController = new DirtyClosePromptController(coreControllerParent, facade);
                        } else if (result == 2) {
                            clearContent();
                            System.exit(0);
                        }
                        return RenderIndicator.FULL;
                }
                break;
        }
        return RenderIndicator.NONE;
    }

    /**
     * Renders the layout with the terminal current height and width
     */
    @Override
    public void paintScreen() throws IOException {
        clearContent();
        facade.paintScreen();
        //facade.renderCursor();
    }

    private void clearContent() {
        coreControllerParent.getAdapter().clearScreen();
    }

    @Override
    public RenderIndicator handleIdle() throws IOException {
        return facade.passToActive((byte) -3);

    }

}
