package controller;

import io.github.btj.termios.Terminal;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

public class TextR {
    protected UseCaseController activeUseCaseController;
    public final ControllerFacade facade;
    public final TermiosTerminalAdapter adapter;

    /**
     * Creates a controller object.
     */
    public TextR(String[] args, TermiosTerminalAdapter termiosTerminalAdapter) throws IOException {
        this.adapter = termiosTerminalAdapter;
        ControllerFacade containedAppFacade;
        try {
            String[] paths = Arrays.copyOfRange(args, 1, args.length);
            containedAppFacade = new ControllerFacade(args, termiosTerminalAdapter);// TODO first remove flags? + pass terminal as object in some magic way
        } catch (IOException e) {
            this.activeUseCaseController = new FileErrorPopupController(this);
            containedAppFacade = null;
            Terminal.clearScreen();
            throw e;
        }
        this.facade = containedAppFacade;

        if(this.activeUseCaseController == null) this.activeUseCaseController = new InspectContentsController(this);
    }

    /**
     * A beautiful start for a beautiful project
     */
    public static void main(String[] args) throws IOException, RuntimeException {
        TextR textR;
        textR = new TextR(args, new RealTermiosTerminalAdapter());

        textR.activeUseCaseController = new InspectContentsController(textR);
        textR.loop();
    }

    /**
     * Contains the main input loop
     */
    public void loop() throws IOException {
        // Terminal moet in rawInput staan voor dimensies te kunnen lezen!
        adapter.enterRawInputMode();
        adapter.clearScreen();
        // Reading terminal dimensions for correct rendering
        activeUseCaseController.paintScreen();
        // Main loop
        for ( ; ; ) {
            int b = -3;
            try {
                b = adapter.readByte(System.currentTimeMillis()+1);
            } catch (TimeoutException e) {
                // Do nothing
            }
            if (b == 27) {
                adapter.readByte();
                activeUseCaseController.handleSurrogate(b, adapter.readByte());
            } else if (b == -3){
                activeUseCaseController.handleIdle();
            } else if(b == -2) {
                /*Useful for testing, or if we needed a way to abruptly stop the constant loop on program force close
                from above in the future*/
                break;
            }else {
                activeUseCaseController.handle(b);
            }
            if(activeUseCaseController.getNeedsRenderSinceLast()){
                this.activeUseCaseController.paintScreen();
            }


            // Flush stdIn & Recalculate dimensions
            System.in.read(new byte[System.in.available()]);
        }
    }
}
