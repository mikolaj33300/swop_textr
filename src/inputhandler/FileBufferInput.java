package inputhandler;

import files.FileBuffer;

public class FileBufferInput extends InputHandler {
	FileBuffer fb;
	boolean surrogate;

	public void FileBufferInput(String path) {
		this.fb = new FileBuffer(path);
	}

	public void Input(byte b) {
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

    /*
     * handles normal input
     */
	public void asciiInput(byte b) {
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
			this.fb.insert(b);
			break;
		}
	}
	/*
	 * handles surrogate input
	 */
	public void surrogateKeysInput(byte b) {
		switch((char) b) {
                    case 'A', 'B', 'C', 'D':
                        fb.moveCursor((char) b);
                        break;
		}
	}

    /**
     * Line separator is non-ASCII, so cannot enter through {@link TextR#enterText(byte)}
     */
  public void enterInsertionPoint(){
    fb.enterinsertionPoint();
  }
}
