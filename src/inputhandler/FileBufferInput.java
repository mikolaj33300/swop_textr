package inputhandler;

public class FileBufferInput extends InputHandler {
	FileBuffer fb;
	public void FileBufferInput(String path) {
		this.fb = new FileBuffer(file);
	}

	public void asciiInput(byte b) {
		switch(b) {
		    case 8, 127, 10, 62:
			    this.fb.deleteCharacter();
			break;
		    // Control + S
		    case 19:
			this.fb.save();
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
		case(b) {
		}
	}
}
