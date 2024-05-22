package controller;

import controller.usecasecontroller.FileErrorPopupController;
import controller.usecasecontroller.InspectContentsController;
import controller.usecasecontroller.UseCaseController;
import ioadapter.RealTermiosTerminalAdapter;
import ioadapter.TermiosTerminalAdapter;
import util.RenderIndicator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class TextR {

    public void setActiveUseCaseController(UseCaseController newUseCaseController){
        this.activeUseCaseController = newUseCaseController;
        try {
            this.activeUseCaseController.paintScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The active use case controller.
     */
    protected UseCaseController activeUseCaseController;

    /**
     * The adapter allows interaction with the terminal. This is either on a real terminal or a simulated one.
     */
    public ArrayList<TermiosTerminalAdapter> adapter = new ArrayList<TermiosTerminalAdapter>(1);

    /**
     * The initial adapter.
     */
    private TermiosTerminalAdapter adapterToStartWith;

    /**
     * The adapter that is currently active.
     */
    private int activeAdapter = 0;

    /**
     * Creates a TextR controller object.
     */
    public TextR(String[] args, TermiosTerminalAdapter termiosTerminalAdapter) {
        this.adapter.add(termiosTerminalAdapter);
        this.adapterToStartWith = termiosTerminalAdapter;
        try {
            if(this.activeUseCaseController == null){
                this.activeUseCaseController = new InspectContentsController(this, args, termiosTerminalAdapter);
            }
        } catch (IOException e) {
            this.activeUseCaseController = new FileErrorPopupController(this, termiosTerminalAdapter);
        }
    }

    /**
     * Main method for the awt part of the program
     * Allows the program to be divided over multiple terminals running on different AWT dispatch threads
     * @throws IOException RuntimeException
     */
    public static void awtMain(String[] args) {
        if (!java.awt.EventQueue.isDispatchThread()){
            throw new AssertionError("Should run in AWT dispatch thread!");
        }

        try {
            TextR textR;
            textR = new TextR(args, new RealTermiosTerminalAdapter());
            //Fix recommended on sample Swing app
            JFrame dummyFrame = new JFrame();
            dummyFrame.pack();

            textR.startListenersAndHandlers();
        } catch (IOException e) {
            throw new RuntimeException("Issue on startup. Are we initializing everything?");
        }
    }

    /**
     * Main method for the program, starting point
     * @param args the files to be opened and optionallyy a line separator
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {awtMain(args);});
    }

    /**
     * Contains the main input loop
     * @throws IOException
     */
    public void startListenersAndHandlers() throws IOException {
        // Terminal moet in rawInput staan voor dimensies te kunnen lezen!
        adapter.get(activeAdapter).enterRawInputMode();
        // Reading terminal dimensions for correct rendering
        activeUseCaseController.paintScreen();

        addTimerListener();
        addTerminalInputListener();
    }

    /**
     * Adds a timer listener to swing
     */
    private void addTimerListener() {
        javax.swing.Timer timer = new javax.swing.Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleIdleEvent();
            }
        });
        timer.start();
    }

    /**
     * Handles idle events. (like snake moving)
     */
    private void handleIdleEvent() {
            try {
                if(activeUseCaseController.handleIdle() != RenderIndicator.NONE){
                    activeUseCaseController.paintScreen();
                }

/*              For testing if the timer fires visually without affecting terminal
                JFrame dummyFrame = new JFrame();
                dummyFrame.pack();
                dummyFrame.setVisible(true);*/
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Adds an input listener to the terminal.
     */
    private void addTerminalInputListener() {
        adapterToStartWith.setInputListener(new Runnable() {
            public void run() {
                java.awt.EventQueue.invokeLater(() -> {
                    try {
                        handleTerminalInputEvent();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    adapterToStartWith.setInputListener(this);
                });
            }
        });
    }

    /**
     * Handles terminal input events.
     * @return true if the program should stop, false otherwise
     * @throws IOException
     */
    private boolean handleTerminalInputEvent() throws IOException {
        int b;

        b = adapter.get(activeAdapter).readByte();

        if (b == 27) {
            adapter.get(activeAdapter).readByte();
            activeUseCaseController.handleSurrogate(b, adapter.get(activeAdapter).readByte());
        } else if(b == -2) {
            /*Useful for testing, or if we needed a way to abruptly stop the constant loop on program force close
            from above in the future*/
            return true;
        } else {
            activeUseCaseController.handle(b);
        }

        // Flush stdIn & Recalculate dimensions
        System.in.read(new byte[System.in.available()]);
        //activeAdapter++;
        //activeAdapter%=adapter.size();
        //facade.setActive(activeAdapter);
        return false;
    }

    /**
     * Returns the active adapter.
     */
    public TermiosTerminalAdapter getAdapter() {
	    return adapterToStartWith;
    }

    /**
     * Returns the active use case controller.
     */
    public UseCaseController getActiveUseCaseController(){
        return this.activeUseCaseController;
    }

/*    public void setAdapter(int a) {
	    activeAdapter = a;
	    facade.setActive(a);
	    System.out.printf("facade active: %d, textr active: %d\n", facade.getActive(), activeAdapter);
    }*/

/*    public void addSwingAdapter() {
	    int size = adapter.size();
	    System.out.println(size);
	    adapter.add(new SwingTerminalAdapter());
	    activeAdapter = adapter.size() - 1;
	    facade.addDisplay(adapter.get(activeAdapter));
    }*/
}
