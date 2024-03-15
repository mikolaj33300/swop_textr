package ui;

import io.github.btj.termios.Terminal;

public class UserPopupBox {
    String message;

    public void render(){
        Terminal.printText(1,1, message);
    }

    public void clearContent(){
        Terminal.clearScreen();
    }

    public UserPopupBox(String message){
        this.message = message;
    }
}
