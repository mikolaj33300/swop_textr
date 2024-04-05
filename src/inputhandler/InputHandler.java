package inputhandler;

import java.io.IOException;

/*
 * commands next
 */
abstract public class InputHandler{

	/*
	 * handles all input
	 */
	abstract public void Input(byte b) throws IOException;

	/*
	 * handles general input
	 */
	abstract void asciiInput(byte b);

	/*
	 * handles surrogate input
	 */
	abstract void surrogateKeysInput(byte b) throws IOException;
	
}
