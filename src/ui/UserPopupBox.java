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
        Coords coords = null;
        try {
            coords = adapter.getTextAreaSize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < coords.height; i++){
            adapter.printText(i+1, 1, " ".repeat(coords.width));
        }
        Terminal.printText(1,1, message);
    }

}
