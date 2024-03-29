package inputhandler;

/*
 * commands next
 */
abstract public class InputHandler{

	/*
	 * handles all input
	 */
	public void Input(byte b);

	/*
	 * handles general input
	 */
	public void asciiInput(byte b);

	/*
	 * handles surrogate input
	 */
	public void surrogateKeysInput(byte b);
	
}
