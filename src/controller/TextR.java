package controller;

import io.github.btj.termios.Terminal;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public class TextR {
    protected UseCaseController activeUseCaseController;
    public final ControllerFacade facade;

    /**
     * Creates a controller object.
     */
    public TextR(String[] args, TermiosTerminalAdapter termiosTerminalAdapter) {
        ControllerFacade containedAppFacade;
        try {
            String[] paths = Arrays.copyOfRange(args, 1, args.length);
            containedAppFacade = new ControllerFacade(args, termiosTerminalAdapter);// TODO first remove flags? + pass terminal as object in some magic way
        } catch (IOException e) {
            this.activeUseCaseController = new FileErrorPopupController(this);
            System.out.println("wtf am I doing here?");
            containedAppFacade = null;
            Terminal.clearScreen();
            System.exit(1);
        }
        this.facade = containedAppFacade;
    }

    /**
     * A beautiful start for a beautiful project
     * @throws IOException, RuntimeException
     */
    public static void main(String[] args) throws IOException, RuntimeException {
        TextR textR;
        textR = new TextR(args, new RealTermiosTerminalAdapter());

        textR.activeUseCaseController = new InspectContentsController(textR);
        textR.loop();

    }

    /**
     * Contains the main input loop
     * @throws IOException
     */
    public void loop() throws IOException {
        // Terminal moet in rawInput staan voor dimensies te kunnen lezen!
        Terminal.enterRawInputMode();
        Terminal.clearScreen();
        // Reading terminal dimensions for correct rendering
        activeUseCaseController.paintScreen();
        // Main loop
        for ( ; ; ) {
            int b = -1;
            try {
                b = Terminal.readByte(System.currentTimeMillis()+1);
            } catch (TimeoutException e) {
                // Do nothing
            }
            if (b == 27) {
                Terminal.readByte();
                activeUseCaseController.handleSurrogate(b, Terminal.readByte());
            }
            if (b == -1) {
                activeUseCaseController.handleIdle();
            } else {
                activeUseCaseController.handle(b);
                Terminal.clearScreen();
                activeUseCaseController.paintScreen();
            }
            // Flush stdIn & Recalculate dimensions
            if(System.in.available() > 0) System.in.skipNBytes(System.in.available());
            if(b == -2){
                /*Useful for testing, or if we needed a way to abruptly stop the constant loop on program force close
                from above in the future*/
                break;
            }
        }
    }
}
