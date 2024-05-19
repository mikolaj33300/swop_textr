package controller;

import util.RenderIndicator;
import util.MoveDirection;
import util.RotationDirection;

import java.io.IOException;

public class InspectContentsController extends UseCaseController {

    private boolean needsRenderSinceLast;

    protected InspectContentsController(TextR coreControllerParent){
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
            case 8, 127, 10, 62, 26, 21, 1, -1:
                return coreControllerParent.facade.passToActive((Integer.valueOf(b)).byteValue());
            // Control + S
            case 19:
                return coreControllerParent.facade.passToActive((Integer.valueOf(b)).byteValue());
            // Control + P
            case 16:
                return coreControllerParent.facade.moveFocus(MoveDirection.LEFT);
            // Control + N
            case 14:
                return coreControllerParent.facade.moveFocus(MoveDirection.RIGHT);
            // Control + R
            case 18:
                return coreControllerParent.facade.rotateLayout(RotationDirection.COUNTERCLOCKWISE);
                // Control + W
            case 23:
                return coreControllerParent.facade.openNewSwingFromActiveWindow();
            // Control + T
            case 20:
                return coreControllerParent.facade.rotateLayout(RotationDirection.CLOCKWISE);
            // Control + G
            case 7:
                return coreControllerParent.facade.openSnakeGame();
            // Control + D
            case 4:
                return coreControllerParent.facade.duplicateActive();
            // Line separator
            case 13:
                return coreControllerParent.facade.handleSeparator();
            // Character input
            default:
                if(b < 32 && b != 10 && b != 13 && b==26 && b!=21 || 127 <= b){
                    return RenderIndicator.NONE;
                }
                return coreControllerParent.facade.passToActive((Integer.valueOf(b)).byteValue());
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
                        return coreControllerParent.facade.handleArrowRight();
                    // Left
                    case 'D':
                        return coreControllerParent.facade.handleArrowLeft();
                    // Up
                    case 'A':
                        return coreControllerParent.facade.handleArrowUp();
                    // Down
                    case 'B':
                        return coreControllerParent.facade.handleArrowDown();
                    case 'S':// F4
                        int result = coreControllerParent.facade.closeActive().b;
                        if (result == 1) { //If was dirty
                            coreControllerParent.activeUseCaseController = new DirtyClosePromptController(coreControllerParent);
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
        coreControllerParent.facade.renderContent();
        coreControllerParent.facade.renderCursor();
    }

    private void clearContent() {
        coreControllerParent.getAdapter().clearScreen();
    }

    @Override
    public RenderIndicator handleIdle() throws IOException {
        return coreControllerParent.facade.passToActive((byte) -3);

    }

}
