package inputhandler;

import directory.directory;

public class directoryInputHandler extends InputHandlingElement {
  directory dirCntnt;

  public directoryInputHandler (String path){ this.dirCntnt = new directory(path); }
  
  @Override
  public void handleSeparator(){// open the current open file
    dirCntnt.enterDir();
    return;
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
    return;
  }

  @Override
  public void save(){
    return;
  }

  @Override
  public int forcedClose(){
    return 1;//?? what to return + enum???
  }

  public directory getDir() {
      return this.dirCntnt;
  }
}
