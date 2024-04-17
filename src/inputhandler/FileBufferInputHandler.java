package inputhandler;

import files.BufferCursorContext;
import ui.Rectangle;

import java.io.IOException;

public class FileBufferInputHandler extends InputHandlingElement {

	BufferCursorContext fb;
	boolean surrogate;

	/**
	 * constructor
	 * @param path the path of our file
	 * @param lineSeparator the separator to insert for newlines (dos/linux)
	 */
	public FileBufferInputHandler(String path, byte[] lineSeparator) throws IOException {
		this.fb = new BufferCursorContext(path, lineSeparator);
	}

	/**
	 * constructor
	 * @param dupedContext the cursorContext to use for this inputhandler
	 */
	public FileBufferInputHandler(BufferCursorContext dupedContext) {
		this.fb = dupedContext;
	}

	/**
	 * @return the current buffercontext
	 */
	public BufferCursorContext getFileBufferContextTransparent(){
		return fb;
	}

  /**
   * handle input
   * @param b the input
   */
    public void input(byte b) {
	    switch(b) {
		    case 27:
			    this.surrogate = true;
			    break;
		    default:
			    if (this.surrogate){
				    surrogateKeysInput(b);
				    this.surrogate = false;
			    } else {
				    asciiInput(b);
			    }
			    break;
	    }
	}

  /**
   * @return if the buffer is dirty
   */
	@Override
	public boolean isSafeToClose() {
		return !fb.getDirty();
	}

    /**
     * handles normal input
     * @param b the input byte
     */
	public void asciiInput(byte b) {
		switch(b) {
		    case 8, 127, 10, 62:
			this.fb.deleteCharacter();
			break;
		    // Control + P
		    case 16:
			break;
		    // Control + N
		    case 14:
			break;
		    // Control + R
		    case 18:
			break;
		    // Control + S
		    case 19:
			this.fb.save();
			break;
		    // Control + T
		    case 20:
			break;
		    // Control + Z
		    case 26:
			fb.undo();
			break;
			    //Control + U
		    case 25:
			fb.redo();
			break;
		    // Line separator
		    case 13:
		    // Character input
		    default:
			this.fb.write(b);
			break;
		}
	}

	/**
	 * handles surrogate input
   * @param b the input byte
	 */
	public void surrogateKeysInput(byte b) {
		switch (b) {
			// Right
			case 'C':
				fb.moveCursorRight();
				break;
			// Left
			case 'D':
				fb.moveCursorLeft();
				break;
			// Up
			case 'A':
				fb.moveCursorUp();
				break;
			// Down
			case 'B':
				fb.moveCursorDown();
				break;
		}
	}

  /**
   * handle right arrow input
   */
	@Override
	public void handleArrowRight(){
		fb.moveCursorRight();
	}

  /**
   * handle left arrow input
   */
	@Override
	public void handleArrowLeft(){
		fb.moveCursorLeft();
	}

  /**
   * handle down arrow input
   */
	@Override
	public void handleArrowDown(){
		fb.moveCursorDown();
	}

  /**
   * handle up arrow input
   */
	@Override
	public void handleArrowUp(){
		fb.moveCursorUp();
	}

  /**
   * enter a separator in the buffer
   */
	@Override
	public void handleSeparator() throws IOException {
		fb.enterSeparator();
		fb.moveCursorRight();
	}

  /**
   * close the filebuffer
   * @return if the filebuffer was closed
   */
	@Override
	public int close(){
		return fb.close();
	}

  /**
   * save the filebuffer
   */
	@Override
	public void save() {
    this.fb.save(); 
  }

}
