package ui;

import io.github.btj.termios.Terminal;
import ioadapter.TermiosTerminalAdapter;
import util.Coords;

import java.io.IOException;

public class UserPopupBox {

    /**
     * The message to be displayed by the pop-up box
     */
    String message;
    /**
     * the adapter used to display the message
     */
    TermiosTerminalAdapter adapter;

    /**
     * @param message the message to be displayed
     */
    public UserPopupBox(String message, TermiosTerminalAdapter adapter){
        this.message = message;
        this.adapter = adapter;
    }

    /**
     * render this popup
     */
    public void render(){
        adapter.clearScreen();
        adapter.printText(1,1, message);
    }

}
