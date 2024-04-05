package controller;

import io.github.btj.termios.Terminal;
import layouttree.*;
import controller.ControllerFacade;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class TextR {
    private static boolean testMode_NoTerminal;
    /**
     * Holds the line separator for this application
     */
    private static byte[] lineSeparatorArg = System.lineSeparator().getBytes();
    protected UseCaseController activeUseCaseController;
    public final ControllerFacade facade;

    /**
     * Creates a controller object.
     * Creates a {@link Layout} object which represents the root layout.
     * its children {@link LayoutLeaf} will be assigned according to arguments given by {@link TextR#main(String[])}
     */
    public TextR(String[] args) {
      ControllerFacade tmp;
      try {
         tmp = new ControllerFacade(args);// TODO first remove flags? + pass terminal as object in some magic way
      } catch (IOException e) {
        this.activeUseCaseController = new FileErrorPopupController(this);
        System.out.println("wtf am I doing here?");
        System.exit(1);
        tmp = null;
      }
      System.out.println(tmp);
      this.facade = tmp;
      System.out.println(facade);
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
        setLineSeparatorFromArgs(args);

        TextR textR = new TextR(args);
        if(!args[args.length-1].equals("noterminal")) {
            textR.activeUseCaseController = new InspectContentsController(textR);
            textR.loop();
        }
    }

    /**
     * Creates an instance of {@link Layout} representing a {@link LayoutNode} containing {@link LayoutLeaf} depending on
     * main input arguments.
    private Layout initRootLayout(String[] args) {
        ArrayList<Layout> leaves = new ArrayList<>();
        for(int i = args[0].equals("--lf") || args[0].equals("--crlf") ? 1 : 0 ; i < args.length; i++) {
            Path checkPath = Paths.get(args[i]);
            if (!Files.exists(checkPath))
                this.activeUseCaseController = new FileErrorPopupController(this);
            else
                try {
                    leaves.add(new LayoutLeaf(args[i], i == 0));
                } catch (IOException e){
                    activeUseCaseController = new FileErrorPopupController(this);
                }

        }

        if(leaves.size() == 1)
            return leaves.get(0);
        else
            return new VerticalLayoutNode(leaves);
    }
     */

    /**
     * Contains the main input loop
     */
    public void loop() throws IOException {
        // Terminal moet in rawInput staan voor dimensies te kunnen lezen!
        if(!testMode_NoTerminal){

            Terminal.enterRawInputMode();
            activeUseCaseController.clearContent();
            // Reading terminal dimensions for correct rendering
            activeUseCaseController.render();
            // Main loop
            for ( ; ; ) {
                int b = Terminal.readByte();
                activeUseCaseController.handle(b);
                activeUseCaseController.clearContent();
                activeUseCaseController.render();
                // Flush stdIn & Recalculate dimensions
                System.in.skipNBytes(System.in.available());
            }
        }
    }

    // Test functions

    static void setLineSeparatorFromArgs(String[] args) {
        if(args[0].equals("--lf"))
            TextR.lineSeparatorArg = new byte[]{0x0a};
        else if(args[0].equals("--crlf"))
            TextR.lineSeparatorArg = new byte[]{0x0d, 0x0a};
        else TextR.lineSeparatorArg = System.lineSeparator().getBytes();
    }

    public static byte[] getLineSeparatorArg(){
        return TextR.lineSeparatorArg;
    }

}
