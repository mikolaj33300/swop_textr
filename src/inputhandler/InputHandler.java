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
	
}
