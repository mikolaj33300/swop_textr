package controller.usecasecontroller;

import controller.ControllerFacade;
import controller.TextR;
import ioadapter.TermiosTerminalAdapter;
import util.*;

import java.io.IOException;

public class InspectContentsController extends UseCaseController {

    @Override
    public void focusTerminal() {
        this.facade.focusTerminal();
    }

    protected InspectContentsController(TextR coreControllerParent, ControllerFacade facade){
        super(coreControllerParent, facade);
    }


    public InspectContentsController(TextR textR, String[] args, TermiosTerminalAdapter termiosTerminalAdapter) throws IOException {
        super(textR, new ControllerFacade(args, termiosTerminalAdapter));
    }

    /**
     * pass the input to the correct controller
     *
     * @param b the int input
     * @throws IOException
     */
    @Override
    public void handle(int b) throws IOException {
        RenderIndicator opRenderIndicator;
        switch(b) {
            case 8, 127, 10, 62, 26, 21, 1, -1:
                opRenderIndicator = facade.passToActive((Integer.valueOf(b)).byteValue());
                break;
            // Control + S
            case 19:
                opRenderIndicator = facade.passToActive((Integer.valueOf(b)).byteValue());
                break;
            // Control + P
            case 16:
                opRenderIndicator = facade.moveFocus(MoveDirection.LEFT);
                break;
            // Control + N
            case 14:
                opRenderIndicator = facade.moveFocus(MoveDirection.RIGHT);
                break;

            case 15:
                opRenderIndicator = facade.openRealDirectory();
                break;
            // Control + R
            case 18:
                opRenderIndicator = facade.rotateLayout(RotationDirection.COUNTERCLOCKWISE);
                break;
                // Control + W
            case 23:
                opRenderIndicator = facade.openNewSwingFromActiveWindow();
                break;
            // Control + T
            case 20:
                opRenderIndicator = facade.rotateLayout(RotationDirection.CLOCKWISE);
                break;
            // Control + G
            case 7:
                opRenderIndicator = facade.openSnakeGame();
                break;
            // Control + D
            case 4:
                opRenderIndicator = facade.duplicateActive();
                break;
            // Line separator
            case 13:
                opRenderIndicator = facade.handleSeparator();
                break;
            // Character input
            default:
                if(b < 32 && b != 10 && b != 13 && b==26 && b!=21 || 127 <= b){
                    opRenderIndicator = RenderIndicator.NONE;
                    break;
                }
                opRenderIndicator = facade.passToActive((Integer.valueOf(b)).byteValue());
                break;
        }
        if(opRenderIndicator != RenderIndicator.NONE){
            try {
                this.paintScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * pass the input to the correct controller
     *
     * @param first  the surrogate type
     * @param second the character after the surrogate
     */
    @Override
    public void handleSurrogate(int first, int second){
        RenderIndicator opRenderIndicator = RenderIndicator.NONE;
        // Surrogate keys
        switch (first) {
            case 27:
                switch ((char) second) {
                    // Right
                    case 'C':
                        opRenderIndicator = facade.handleArrowRight();
                        break;
                    // Left
                    case 'D':
                        opRenderIndicator = facade.handleArrowLeft();
                        break;
                    // Up
                    case 'A':
                        opRenderIndicator = facade.handleArrowUp();
                        break;
                    // Down
                    case 'B':
                        opRenderIndicator = facade.handleArrowDown();
                        break;
                    case 'S':// F4
                        Pair<RenderIndicator, GlobalCloseStatus> results = facade.closeActive();
                        opRenderIndicator = (RenderIndicator)results.a;
                        GlobalCloseStatus closingResult = results.b;
                        if (closingResult == GlobalCloseStatus.DIRTY_CLOSE_PROMPT) { //If was dirty
                            unsubscribeFromFacadeAscii();
                            coreControllerParent.setActiveUseCaseController(new DirtyClosePromptController(coreControllerParent, facade));
                            return;
                        } else if (closingResult == GlobalCloseStatus.CLOSED_ALL_DISPLAYS) { //If was last window
                            System.exit(0);
                        }
                }
                break;
        }
        if(opRenderIndicator != RenderIndicator.NONE){
            try {
                this.paintScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Renders the layout with the terminal current height and width
     */
    @Override
    public void paintScreen() throws IOException {
        facade.paintScreen();
        //facade.renderCursor();
    }

    /**
     * HandleIdle passes a tick from the "system clock" down as a special argument to the facade.
     * The facade passes it to the active display, making sure only the active snake game ticks
     * @return
     * @throws IOException
     */
    @Override
    public RenderIndicator handleIdle() throws IOException {
        return facade.passToActive((byte) -3);

    }

}
