package controller;

import controller.adapter.RealTermiosTerminalAdapter;
import controller.adapter.TermiosTerminalAdapter;
import controller.adapter.SwingTerminalAdapter;
import io.github.btj.termios.Terminal;
import controller.TSMediator;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.ArrayList;

public class TextR {
    protected UseCaseController activeUseCaseController;
    public final ControllerFacade facade;
    public ArrayList<TermiosTerminalAdapter> adapter = new ArrayList<TermiosTerminalAdapter>(1);
    private int activeAdapter = 0;

    /**
     * Creates a controller object.
     */
    public TextR(String[] args, TermiosTerminalAdapter termiosTerminalAdapter) throws IOException {
        this.adapter.add(termiosTerminalAdapter);
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
     * @throws IOException RuntimeException
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
        adapter.get(activeAdapter).enterRawInputMode();
        adapter.get(activeAdapter).clearScreen();
        // Reading terminal dimensions for correct rendering
        activeUseCaseController.paintScreen();
        // Main loop
        for ( ; ; ) {
            int b = -3;
            try {
                b = adapter.get(activeAdapter).readByte(System.currentTimeMillis()+1);
            } catch (TimeoutException e) {
                // Do nothing
            }
            if (b == 27) {
                adapter.get(activeAdapter).readByte();
                activeUseCaseController.handleSurrogate(b, adapter.get(activeAdapter).readByte());
            } else if (b == -3){
                activeUseCaseController.handleIdle();
            } else if(b == -2) {
                /*Useful for testing, or if we needed a way to abruptly stop the constant loop on program force close
                from above in the future*/
                break;
            } else {
                activeUseCaseController.handle(b);
            }
            if(activeUseCaseController.getNeedsRenderSinceLast()){
                this.activeUseCaseController.paintScreen();
            }


            // Flush stdIn & Recalculate dimensions
            System.in.read(new byte[System.in.available()]);
        }
    }

    public TermiosTerminalAdapter getAdapter() {
	return adapter.get(activeAdapter);
    }

    public void setAdapter(int a) {
	activeAdapter = a;
	facade.setActive(a);
    }

    public void addSwingAdapter() {
	int size = adapter.size();
	System.out.println(size);
	adapter.add(new SwingTerminalAdapter(new TSMediator(this, size)));
	activeAdapter = adapter.size() - 1;
	facade.addDisplay(adapter.get(activeAdapter));
    }
}
