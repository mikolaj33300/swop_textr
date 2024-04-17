package inputhandler;

import files.BufferCursorContext;

import java.io.IOException;

public class FileBufferInputHandler extends InputHandlingElement {

	BufferCursorContext fb;
	boolean surrogate;

	public FileBufferInputHandler(String path, byte[] lineSeparator) throws IOException {
		this.fb = new BufferCursorContext(path, lineSeparator);
	}

	public FileBufferInputHandler(BufferCursorContext dupedContext) {
		this.fb = dupedContext;
	}

	public BufferCursorContext getFileBufferContextTransparent(){
		return fb;
	}

	public void input(byte b) {
		switch(b) {
			case -1:
				// Idle
				break;
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

			//TODO: MAKE TERMINAL USE REAL CTRL Z CODE
			// Control + Z or Control + Y
			case 26, 25:
				fb.undo();
				break;
				//Control + U
			case 21:
				fb.redo();
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
		contentsChangedSinceRender = true;
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
		contentsChangedSinceRender = true;
	}

	@Override
	public void handleArrowRight(){
		fb.moveCursorRight();
		contentsChangedSinceRender = true;
	}

	@Override
	public void handleArrowLeft(){
		fb.moveCursorLeft();
		contentsChangedSinceRender = true;
	}

	@Override
	public void handleArrowDown(){
		fb.moveCursorDown();
		contentsChangedSinceRender = true;
	}

	@Override
	public void handleArrowUp(){
		fb.moveCursorUp();
		contentsChangedSinceRender = true;
	}

	@Override
	public void handleSeparator() throws IOException {
		fb.enterSeparator();
		fb.moveCursorRight();
		contentsChangedSinceRender = true;
	}

	@Override
	public int close(){
		return fb.close();
	}

	@Override
	public void save() {
		this.fb.save();
		contentsChangedSinceRender = true;
	}

}
