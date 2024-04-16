package controller;

import io.github.btj.termios.Terminal;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class TextR {
    protected UseCaseController activeUseCaseController;
    public final ControllerFacade facade;

    /**
     * Creates a controller object.
     */
    public TextR(String[] args) {
        ControllerFacade containedAppFacade;
        try {
            String[] paths = Arrays.copyOfRange(args, 1, args.length);
            containedAppFacade = new ControllerFacade(args);// TODO first remove flags? + pass terminal as object in some magic way
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
     */
    public static void main(String[] args) throws IOException, RuntimeException {
        // If no arguments given
        if(args.length == 0 ||
                (
                        // Or --lf || --crlf is given
                        (args[0].equals("--lf") || (args[0].equals("--crlf")))
                                // But amount of args is 1
                                && args.length == 1
                )
        ) { // Then no path is specified, and we cannot open
            throw new RuntimeException("TextR needs at least one specified file");
        }

        TextR textR = new TextR(args);
        textR.activeUseCaseController = new InspectContentsController(textR);
        textR.loop();

    }

    /**
     * Contains the main input loop
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
                b = Terminal.readByte(1);
            } catch(TimeoutException e) {
                // Do nothing
            }
            if(b == 27) {
                Terminal.readByte();
                activeUseCaseController.handleSurrogate(b, Terminal.readByte());
            }
            if(b == -1)
                activeUseCaseController.handleIdle();
            else {
                activeUseCaseController.handle(b);
                Terminal.clearScreen();
                activeUseCaseController.paintScreen();
            }
            // Flush stdIn & Recalculate dimensions
            if(System.in.available() != 0) System.in.skipNBytes(System.in.available());
        }
    }
}
