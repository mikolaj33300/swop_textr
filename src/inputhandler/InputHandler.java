package inputhandler;

import java.io.IOException;

/*
 * commands next
 */
abstract public class InputHandler{

  public int getHash(){
    return this.hashCode();
  }

	/*
	 * handles all input
	 */
	abstract public void Input(byte b) throws IOException;

	/*
	 * handles general input
	 */
	abstract void asciiInput(byte b) throws IOException;

	/*
	 * handles surrogate input
	 */
	abstract void surrogateKeysInput(byte b) throws IOException;
	
}
