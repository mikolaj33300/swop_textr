package inputhandler;

/*
 * commands next
 */
abstract public class InputHandler{

	/*
	 * handles all input
	 */
	abstract public void Input(byte b);

	/*
	 * handles general input
	 */
	abstract void asciiInput(byte b);

	/*
	 * handles surrogate input
	 */
	abstract void surrogateKeysInput(byte b);
	
}
