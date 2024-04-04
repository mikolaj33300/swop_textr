package controller;

import java.io.IOException;

public abstract class UseCaseController {
    TextR coreControllerParent;
    ControllerFacade facade;

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
    public abstract void handle(int b) throws IOException;

    /**
     * Prints contents on a screen
     * @throws IOException
     */
    public abstract void render() throws IOException;

    /**
     * Clears contents from screen entirely
     * @throws IOException
     */
    public abstract void clearContent() throws IOException;

}
