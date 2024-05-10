package controller;

import inputhandler.InputHandlingElement;
import ui.View;

class Window {
    public final View view;
    public final InputHandlingElement handler;
    private Window next;

    public Window(View v, InputHandlingElement h) {
        this.view = v;
        this.handler = h;
    }

    /*
     * sander?
     */
    public Window(String path, byte[] lineSeparator){

    }

    public insertWindow(Window new) {
      if (next == null) {
        next = new;
      } else {
        new.insertWindow(new);
      }
      return;
    }

    public createFileBuffer(String path, byte[] lineSeparator, TermiosTerminalAdapter termiosTerminalAdapter) {
      this.handler = new FileBufferInputHandler(checkPath, lineSeparatorArg);
      this.view = new FileBufferView(openedFileHandler.getFileBufferContextTransparent(), termiosTerminalAdapter);
    }
}
