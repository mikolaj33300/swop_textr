package controller;

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
    public void handle(int b) throws IOException {
        switch(b) {
            case 8, 127, 10, 62, 26, 21, 1, -1:
                coreControllerParent.facade.passToActive((byte) b);
		this.needsRenderSinceLast = true;
                break;
            // Control + S
            case 19:
                coreControllerParent.facade.passToActive((byte) b);
		this.needsRenderSinceLast = true;
                break;
	    // Control + O
            case 15:
                  coreControllerParent.facade.openDirectory(System.getProperty("user.dir"));
		this.needsRenderSinceLast = true;
		break;
            // Control + P
            case 16:
                coreControllerParent.facade.moveFocus(MoveDirection.LEFT);
		this.needsRenderSinceLast = true;
                break;
            // Control + N
            case 14:
                coreControllerParent.facade.moveFocus(MoveDirection.RIGHT);
		this.needsRenderSinceLast = true;
                break;
            // Control + R
            case 18:
                coreControllerParent.facade.rotateLayout(RotationDirection.COUNTERCLOCKWISE);
		this.needsRenderSinceLast = true;
                break;
            // Control + T
            case 20:
                coreControllerParent.facade.rotateLayout(RotationDirection.CLOCKWISE);
		this.needsRenderSinceLast = true;
                break;
            // Control + G
            case 7:
                coreControllerParent.facade.openSnakeGame();
		this.needsRenderSinceLast = true;
                break;
            // Control + D
            case 4:
                coreControllerParent.facade.duplicateActive();
		this.needsRenderSinceLast = true;
                break;
            // Line separator
            case 13:
                coreControllerParent.facade.handleSeparator();
		this.needsRenderSinceLast = true;
                break;
            // Character input
            default:
                if(b < 32 && b != 10 && b != 13 && b==26 && b!=21 || 127 <= b){
                    break;
                }
                coreControllerParent.facade.passToActive((byte) b);
		this.needsRenderSinceLast = true;
                break;
        }
    }

    /**
     * pass the input to the correct controller
     * @param first the surrogate type
     * @param second the character after the surrogate
     */
    @Override
    public void handleSurrogate(int first, int second){
        // Surrogate keys
        switch (first) {
            case 27:
                switch ((char) second) {
                    // Right
                    case 'C':
                        coreControllerParent.facade.handleArrowRight();
			this.needsRenderSinceLast = false;
                        break;
                    // Left
                    case 'D':
                        coreControllerParent.facade.handleArrowLeft();
			this.needsRenderSinceLast = false;
                        break;
                    // Up
                    case 'A':
                        coreControllerParent.facade.handleArrowUp();
			this.needsRenderSinceLast = false;
                        break;
                    // Down
                    case 'B':
                        coreControllerParent.facade.handleArrowDown();
			this.needsRenderSinceLast = false;
                        break;
                    case 'S':// F4
                        int result = coreControllerParent.facade.closeActive();
                        if (result == 1) { //If was dirty
                            coreControllerParent.activeUseCaseController = new DirtyClosePromptController(coreControllerParent);
                        } else if (result == 2) {
                            clearContent();
                            System.exit(0);
                        }
			this.needsRenderSinceLast = true;
                        break;
                }
                break;
        }
    }

    /**
     * Renders the layout with the terminal current height & width
     */
    @Override
    public void paintScreen() throws IOException {
        clearContent();
        coreControllerParent.facade.renderContent();
        coreControllerParent.facade.renderCursor();
        this.needsRenderSinceLast = false;
    }

    private void clearContent() {
        coreControllerParent.adapter.clearScreen();
    }

    @Override
    public void handleIdle() throws IOException {
        coreControllerParent.facade.passToActive((byte) -3);
        this.needsRenderSinceLast = coreControllerParent.facade.getContentsChangedSinceLastRender();
    }

    @Override
    public boolean getNeedsRenderSinceLast() {
        return this.needsRenderSinceLast;
    }

}
