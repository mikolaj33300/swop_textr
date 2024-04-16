package controller;

import layouttree.MOVE_DIRECTION;
import layouttree.ROT_DIRECTION;

import java.io.IOException;

public class SnakeController extends InspectContentsController {

    protected SnakeController(TextR coreControllerParent){
        super(coreControllerParent);
    }

    @Override
    public void handle(int b) throws IOException {
        // Sends rotation & focus change & line separator to super method
        // All other cases are ignored as they are invalid inputs for snake
        switch(b) {
            case 16, 14, 18, 20, 4, 13 -> super.handle(b);
        }
    }

    @Override
    public void handleSurrogate(int first, int second){
        super.handleSurrogate(first, second);
    }


    @Override
    public void paintScreen() {
        try {
            coreControllerParent.facade.renderContent();
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
        coreControllerParent.facade.clearContent();
    }

    @Override
    public void handleIdle() throws IOException {
        coreControllerParent.facade.passToActive((byte) -1);
    }
}
