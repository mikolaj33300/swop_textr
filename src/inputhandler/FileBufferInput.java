package inputhandler;

import java.io.IOException;
import java.util.List;

import files.FileBuffer;
import observer.FileBufferListener;

public class FileBufferInput extends InputHandler {
	private FileBuffer fb;
	private boolean surrogate;
	

	public void FileBufferInput(String path, FileBufferListener listener) throws IOException {
		this.fb = new FileBuffer(path, listener);
	}

	public void Input(byte b) throws IOException {
    if (this.surrogate){
      surrogateKeysInput(b);
      this.surrogate = false;
    } else if (b == 27) {
      this.surrogate = true;
    } else {
      asciiInput(b);
    }
	}

    /*
     * handles normal input
     */
	void asciiInput(byte b) throws IOException {
		switch(b) {
		    case 8, 127, 10, 62:
			    this.fb.deleteCharacter();
			break;
		    // Control + S
		    case 19:
        this.fb.save();
			break;
      // TODO where does this need to be handled => listner
		    // Control + P
		    case 16:
			break;
		    // Control + N
		    case 14:
			break;
		    // Control + R
		    case 18:
			break;
		    // Control + T
		    case 20:
			break;
		    // Line separator
		    case 13:
		    // Character input
		    default:
			this.fb.write(b);
			break;
		}
	}
	/*
	 * handles surrogate input
	 */
	void surrogateKeysInput(byte b) throws IOException {
		switch((char) b) {
            // Right
            case 'C':
                this.fb.moveCursorRight();
                break;
            // Left
            case 'D':
                this.fb.moveCursorLeft();
                break;
            // Up
            case 'A':
                this.fb.moveCursorUp();
                break;
            // Down
            case 'B':
                this.fb.moveCursorDown();
                break;
        }
	}

    /**
     * Line separator is non-ASCII, so cannot enter through {@link TextR#enterText(byte)}
     */
  public void enterInsertionPoint() throws IOException {
    fb.enterInsertionPoint();
  }
}
