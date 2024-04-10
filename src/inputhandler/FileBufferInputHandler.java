package inputhandler;

import files.BufferCursorContext;

import java.io.IOException;

public class FileBufferInputHandler extends InputHandlingElement {
	BufferCursorContext fb;
	boolean surrogate;

	public FileBufferInputHandler(String path) throws IOException {
		this.fb = new BufferCursorContext(path);
	}

	public BufferCursorContext getFileBufferContextTransparent(){
		return fb;
	}

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

	@Override
	public boolean isSafeToClose() {
		return !fb.getDirty();
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

	@Override
	public void handleArrowRight(){

	}

	@Override
	public void handleArrowLeft(){

	}

	@Override
	public void handleArrowDown(){

	}

	@Override
	public void handleArrowUp(){

	}

	@Override
	public void handleSeparator(){

	}

	public int close(){
		return fb.close();
	}

	public void save() {this.fb.save();}
}
