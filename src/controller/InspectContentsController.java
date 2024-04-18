package controller;

import io.github.btj.termios.Terminal;
import layouttree.MOVE_DIRECTION;
import layouttree.ROT_DIRECTION;

import java.io.IOException;

public class InspectContentsController extends UseCaseController {

    protected InspectContentsController(TextR coreControllerParent){
        super(coreControllerParent);
    }

    @Override
    public void handle(int b) throws IOException {
        switch(b) {
            case 8, 127, 10, 62, 26, 21, 1, -1:
                coreControllerParent.facade.passToActive((Integer.valueOf(b)).byteValue());
                break;
            // Control + S
            case 19:
                coreControllerParent.facade.passToActive((Integer.valueOf(b)).byteValue());
                break;
            // Control + P
            case 16:
                coreControllerParent.facade.moveFocus(MOVE_DIRECTION.LEFT);
                break;
            // Control + N
            case 14:
                coreControllerParent.facade.moveFocus(MOVE_DIRECTION.RIGHT);
                break;
            // Control + R
            case 18:
                coreControllerParent.facade.rotateLayout(ROT_DIRECTION.COUNTERCLOCKWISE);
                break;
            // Control + T
            case 20:
                coreControllerParent.facade.rotateLayout(ROT_DIRECTION.CLOCKWISE);
                break;
            // Control + G
            case 7:
                coreControllerParent.facade.openSnakeGame();
                break;
            // Control + D
            case 4:
                coreControllerParent.facade.duplicateActive();
                break;
            // Line separator
            case 13:
                coreControllerParent.facade.handleSeparator();
                break;
            // Character input
            default:
                if(b < 32 && b != 10 && b != 13 && b==26 && b!=21 || 127 <= b){
                    break;
                }
                coreControllerParent.facade.passToActive((Integer.valueOf(b)).byteValue());
                break;
        }
    }

    @Override
    public void handleSurrogate(int first, int second){
        // Surrogate keys
        switch (first) {
            case 27:
                switch ((char) second) {
                    // Right
                    case 'C':
                        coreControllerParent.facade.handleArrowRight();
                        break;
                    // Left
                    case 'D':
                        coreControllerParent.facade.handleArrowLeft();
                        break;
                    // Up
                    case 'A':
                        coreControllerParent.facade.handleArrowUp();
                        break;
                    // Down
                    case 'B':
                        coreControllerParent.facade.handleArrowDown();
                        break;
                    case 'S':// F4
                        int result = coreControllerParent.facade.closeActive();
                        if (result == 1) { //If was dirty
                            coreControllerParent.activeUseCaseController = new DirtyClosePromptController(coreControllerParent);
                        } else if (result == 2) {
                            //TODO Delegate clearing to more specialized class, idem in dirty close
                            Terminal.clearScreen();
                            System.exit(0);
                        }
                        break;
                }
                break;
        }
    }

    /**
     * Renders the layout with the terminal current height & width
     */
    @Override
    public void paintScreen() {
            if(coreControllerParent.facade.getContentsChangedSinceLastRender()){
                try {
                    clearContent();
                    coreControllerParent.facade.renderContent();
                    coreControllerParent.facade.renderCursor();
                } catch (IOException e){
                    coreControllerParent.activeUseCaseController = new FileErrorPopupController(coreControllerParent);
                }
            }

    }

    public void clearContent() throws IOException {
        coreControllerParent.adapter.clearScreen();
    }

    @Override
    public void handleIdle() throws IOException {
        coreControllerParent.facade.passToActive((byte) -3);
    }

}