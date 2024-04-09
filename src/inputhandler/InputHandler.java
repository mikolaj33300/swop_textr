package inputhandler;

import java.io.IOException;

/*
 * commands next
 */
abstract public class InputHandler{

  public int getHash(){
    return this.hashCode();
  }

  public abstract int close();

  public abstract void save();

	/*
	 * handles all input
	 */
	abstract public void input(byte b) throws IOException;
	
}
