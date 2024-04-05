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
      coreControllerParent.facade.passToActive((byte) b);
    }

    @Override
    /**
     * Renders the layout with the terminal current height & width
     */
    public void render() {
        try{
            coreControllerParent.facade.renderContent();
            coreControllerParent.facade.renderCursor();
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

}
