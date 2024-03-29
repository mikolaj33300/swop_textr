package inputhandler;

/*
 * commands next
 */
abstract public class InputHandler{

	boolean surrogate;

	/*
	 * handles all input
	 */
	public void Input(byte b) {
		switch(b) {
			case 27:
				this.surrogate = true;
				break;
			default:
				if (this.surrogate){
					surrogateKeysInput(byte b);
					this.surrogate = false;
				} else {
					public void asciiInput(byte b);
				}
				break;
		}
	}

	/*
	 * handles general input
	 */
	public void asciiInput(byte b);

	/*
	 * handles surrogate input
	 */
	public void surrogateKeysInput(byte b);
	
}
