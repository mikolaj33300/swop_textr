package controller;

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
    public void handle(int b) throws IOException{

    }

    public void handleSurrogate(int first, int second){

    }

    public abstract void handleIdle() throws IOException;

}
