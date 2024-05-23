package controller.usecasecontroller;

import controller.ControllerFacade;
import controller.TextR;
import ioadapter.ASCIIKeyEventListener;
import util.RenderIndicator;

import java.io.IOException;

public abstract class UseCaseController {

    final ASCIIKeyEventListener asciiEventListener;
    ControllerFacade facade;
    TextR coreControllerParent;

    /**
     * Constructor that sets the main constructor reference of this to the given reerence.
     * @param coreControllerParent
     */
    protected UseCaseController(TextR coreControllerParent, ControllerFacade facade){
        this.coreControllerParent = coreControllerParent;
        this.facade = facade;
        this.asciiEventListener = new ASCIIKeyEventListener() {
            @Override
            public void notifyNormalKey(int byteInt) {
                try {
                    handle(byteInt);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void notifySurrogateKeys(int first, int second) {
                handleSurrogate(first, second);
            }
        };
        facade.subscribeToKeyPresses(asciiEventListener);
    }

    /**
     * Generic handler of the input byte from terminal
     *
     * @param b
     * @throws IOException
     */
    public void handle(int b) throws IOException{
    }

    public void handleSurrogate(int first, int second){
    }

    /**
     * Prints contents on a screen
     * @throws IOException a
     */
    public abstract void paintScreen() throws IOException;

    public RenderIndicator handleIdle() throws IOException {
        return RenderIndicator.NONE;
    }

    public ControllerFacade getFacade(){
        return facade;
    }

    void unsubscribeFromFacadeAscii(){
        this.facade.unsubscribeFromKeyPresses(asciiEventListener);
    }
}
