package controller;

import io.github.btj.termios.Terminal;
import layouttree.DIRECTION;
import layouttree.ROT_DIRECTION;

import java.io.IOException;

public class InspectContentsController extends UseCaseController {

    protected InspectContentsController(TextR coreControllerParent){
        super(coreControllerParent);
    }

    @Override
    public void handle(int b) throws IOException {
        switch(b) {
            case 8, 127, 10, 62:
                coreControllerParent.rootLayout.deleteCharacter();
                break;
            // Control + S
            case 19:
                coreControllerParent.rootLayout.saveActiveBuffer();
                break;
            // Control + P
            case 16:
                coreControllerParent.rootLayout.moveFocus(DIRECTION.LEFT);
                break;
            // Control + N
            case 14:
                coreControllerParent.rootLayout.moveFocus(DIRECTION.RIGHT);
                break;
            // Control + R
            case 18:
                coreControllerParent.rootLayout.rotateRelationshipNeighbor(ROT_DIRECTION.COUNTERCLOCKWISE);
                break;
            // Control + T
            case 20:
                coreControllerParent.rootLayout.rotateRelationshipNeighbor(ROT_DIRECTION.CLOCKWISE);
                break;
            // Surrogate keys
            case 27:
                Terminal.readByte();
                int c = Terminal.readByte();

                switch ((char) c) {
                    case 'A', 'B', 'C', 'D':
                        coreControllerParent.rootLayout.moveCursor((char) c);
                        break;
                    case 'S':// F4
                        int result = coreControllerParent.rootLayout.closeActive();
                        if(result == 1){ //If was dirty
                            coreControllerParent.activeUseCaseController = new DirtyClosePromptController(coreControllerParent);
                        } else if(result == 2){
                            coreControllerParent.rootLayout=null;
                            System.out.println("22222222222222");
                            System.exit(0);
                        }
                        break;
                }
                break;
            // Line separator
            case 13:
                coreControllerParent.rootLayout.enterInsertionPoint();
                Terminal.clearScreen();
                break;
            // Character input
            default:
                Terminal.clearScreen();
                if(b < 32 && b != 10 && b != 13 || 127 <= b)
                    break;
                coreControllerParent.rootLayout.enterText((Integer.valueOf(b)).byteValue());
                break;
        }
    }

    @Override
    /**
     * Renders the layout with the terminal current height & width
     */
    public void render() {
        try{
            facade.renderContent();
            facade.renderCursor();
        } catch (IOException e){
            coreControllerParent.activeUseCaseController = new FileErrorPopupController(coreControllerParent);
        }
    }

    /**
     * Clears the active layouttree of text
     * @throws IOException
     */
    @Override
    public void clearContent() throws IOException {
        coreControllerParent.rootLayout.clearContent();
    }

}
