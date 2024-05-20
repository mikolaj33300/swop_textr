package controller;

import controller.adapter.RealTermiosTerminalAdapter;
import controller.adapter.TermiosTerminalAdapter;
import controller.adapter.SwingTerminalAdapter;
import io.github.btj.termios.Terminal;
import util.RenderIndicator;

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

        if(this.activeUseCaseController == null){
            this.activeUseCaseController = new InspectContentsController(this);
        }
    }

    /**
     * A beautiful start for a beautiful project
     * @throws IOException RuntimeException
     */
    public static void awtMain(String[] args) {
        if (!java.awt.EventQueue.isDispatchThread())
            throw new AssertionError("Should run in AWT dispatch thread!");
        try {
            TextR textR;
            textR = new TextR(args, new RealTermiosTerminalAdapter());

            textR.activeUseCaseController = new InspectContentsController(textR);
            textR.loop();
        } catch (IOException e) {
            throw new RuntimeException("Issue on startup. Are we initializing everything?");
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {awtMain(args);});
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


        addTimerListener();

        addTerminalInputListener();
    }

    private void addTerminalInputListener() {
        Terminal.setInputListener(new Runnable() {
            public void run() {
                java.awt.EventQueue.invokeLater(() -> {
                    try {
                        handleTerminalInputEvent();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Terminal.setInputListener(this);
                });
            }
        });
    }

    private void handleIdleEvent() throws IOException {
        if(activeUseCaseController.handleIdle() != RenderIndicator.NONE){
            activeUseCaseController.paintScreen();
        }
    }

    private void addTimerListener() {
        javax.swing.Timer timer = new javax.swing.Timer(1, e -> {
            try {
                handleIdleEvent();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        timer.start();
    }

    private boolean handleTerminalInputEvent() throws IOException {
        RenderIndicator operationNeedsRerender = RenderIndicator.NONE;
        int b;

        b = adapter.get(activeAdapter).readByte();

        if (b == 27) {
            adapter.get(activeAdapter).readByte();
            operationNeedsRerender = activeUseCaseController.handleSurrogate(b, adapter.get(activeAdapter).readByte());
        } else if(b == -2) {
            /*Useful for testing, or if we needed a way to abruptly stop the constant loop on program force close
            from above in the future*/
            return true;
        } else {
            operationNeedsRerender = activeUseCaseController.handle(b);
        }
        if(operationNeedsRerender != RenderIndicator.NONE){
            activeUseCaseController.paintScreen();
        }
        // Flush stdIn & Recalculate dimensions
        System.in.read(new byte[System.in.available()]);
        //activeAdapter++;
        //activeAdapter%=adapter.size();
        //facade.setActive(activeAdapter);
        return false;
    }

    public TermiosTerminalAdapter getAdapter() {
	    return adapter.get(activeAdapter);
    }

    public void setAdapter(int a) {
	    activeAdapter = a;
	    facade.setActive(a);
	    System.out.printf("facade active: %d, textr active: %d\n", facade.getActive(), activeAdapter);
    }

    public void addSwingAdapter() {
	    int size = adapter.size();
	    System.out.println(size);
	    adapter.add(new SwingTerminalAdapter());
	    activeAdapter = adapter.size() - 1;
	    facade.addDisplay(adapter.get(activeAdapter));
    }
}
