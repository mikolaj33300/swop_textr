package core;

import java.io.IOException;

public abstract class UseCaseController {
    protected Controller coreControllerParent;

    protected UseCaseController(Controller coreControllerParent){
        this.coreControllerParent = coreControllerParent;
    }

    public abstract void handle(int b) throws IOException;

    public abstract void render() throws IOException;

    public abstract void clearContent() throws IOException;

}
