package core;

import java.io.IOException;

public abstract class UseCaseController {
    protected TextR coreControllerParent;

    protected UseCaseController(TextR coreControllerParent){
        this.coreControllerParent = coreControllerParent;
    }

    public abstract void handle(int b) throws IOException;
    public abstract void render() throws IOException;
    public abstract void clearContent() throws IOException;
}
