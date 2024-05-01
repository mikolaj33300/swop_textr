package inputhandler;

import directory.directory;

public class directoryInputHandler extends InputHandlingElement {
  directory dirCntnt;
  int x, y;// make separate class cursor and include it here

  public directoryInputHandler (String path){ this.dirCntnt = new directory(path); }
  
  @Override
  public void handleSeparator(){// open the current open file
    return;
  }

  @Override
  public void handleArrowUp(){
    this.y--;
    return;
  }
  @Override
  public void handleArrowDown(){
    this.y++;
  }

  @Override
  public void handleArrowLeft(){
    this.x++;
  }

  @Override
  public void handleArrowRight(){
    this.x--;
  }

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
}
