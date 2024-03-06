import io.github.btj.termios.Terminal;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Controller {

    /**
     * A beautiful start for a beautiful project
     */
    public static void main(String[] args) throws IOException {
        //Controller controller = new Controller();
        //controller.run();
        run();
    }

    public static void run() throws IOException {

        Terminal.enterRawInputMode();

        for(;;) {

            int c = Terminal.readByte();

            if(c == '\033')
                System.out.println("Pressed");

        }

    }


}