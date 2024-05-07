package inputhandler;

import inputhandler.FileBufferInputHandler;
import controller.ControllerFacade;
import ui.FileBufferView;
import directory.directory;

public class directoryInputHandler extends InputHandlingElement {
  directory dirCntnt;
  private ControllerFacade parent;

  public directoryInputHandler (String path, ControllerFacade prnt){ 
      this.dirCntnt = new directory(path); 
      this.parent = prnt;
  }
  
    @Override
    public void handleSeparator(){// open the current open file
	String path = dirCntnt.handleEnter();
	if (path != null)
	    try {
		FileBufferInputHandler fh = new FileBufferInputHandler(path, System.lineSeparator().getBytes());
		parent.replaceActive(new FileBufferView(fh.fb, parent.getTerminal()), fh);
	    } catch (Exception e) {
		System.out.println(path);
		System.out.println(e);
		System.exit(1);
	    }
    }

  @Override public void handleArrowUp(){ this.dirCntnt.scrollUp(); }
  @Override public void handleArrowDown(){ this.dirCntnt.scrollDown(); }
  @Override public void handleArrowLeft(){ this.dirCntnt.scrollLeft(); }
  @Override public void handleArrowRight(){ this.dirCntnt.scrollRight(); }

  @Override
  public boolean isSafeToClose(){
    return true;
  }

  @Override
  public void input(byte b){
      switch (b) {
        case 104:
            dirCntnt.toggleHide();
            break;
        case 114:
            dirCntnt.updateFiles();
            break;
      }

    this.contentsChangedSinceRender = false;
    return;
  }

  @Override
  public void save(){
      this.contentsChangedSinceRender = false;
    return;
  }

  @Override
  public int forcedClose(){
      this.contentsChangedSinceRender = false;
    return 1;//?? what to return + enum???
  }

  public directory getDir() {
      this.contentsChangedSinceRender = false;
      return this.dirCntnt;
  }
}
