package ui;

import io.github.btj.termios.Terminal;

public class UserPopupBox {
    String message;

    /**
     * Renders a message on the screen
     */
    public void render(){
        Terminal.printText(1,1, message);
    }

    /**
     * Clears the terminalsceen
     */
    public void clearContent(){
        Terminal.clearScreen();
    }

    /**
     *
     */
    public UserPopupBox(String message){
        this.message = message;
    }
}
