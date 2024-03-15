package core;

import java.io.IOException;

public interface UseCaseController {

    public abstract void handle(int b) throws IOException;

    public abstract void render() throws IOException;

    public abstract void clearContent() throws IOException;

}
