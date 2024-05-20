package controller;

import util.RenderIndicator;

import java.io.IOException;

public abstract class UseCaseController {

    TextR coreControllerParent;

    /**
     * Constructor that sets the main constructor reference of this to the given reerence.
     * @param coreControllerParent
     */
    protected UseCaseController(TextR coreControllerParent){
        this.coreControllerParent = coreControllerParent;
    }

    /**
     * Generic handler of the input byte from terminal
     * @param b
     * @throws IOException
     */
    public RenderIndicator handle(int b) throws IOException{
        return RenderIndicator.NONE;
    }

    public RenderIndicator handleSurrogate(int first, int second){
        return RenderIndicator.NONE;
    }

    /**
     * Prints contents on a screen
     * @throws IOException a
     */
    public abstract void paintScreen() throws IOException;

    public RenderIndicator handleIdle() throws IOException {
        return RenderIndicator.NONE;
    };
}
