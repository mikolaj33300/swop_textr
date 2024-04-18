package ui;

import io.github.btj.termios.Terminal;

public class UserPopupBox {
    String message;

    /**
     * @param message the message to be displayed
     */
    public UserPopupBox(String message){
        this.message = message;
    }

    /**
     * render this popup
     */
    public void render(){
        Terminal.printText(1,1, message);
    }

    /**
     * clear the screen
     */
    public void clearContent(){
        Terminal.clearScreen();
    }

}
