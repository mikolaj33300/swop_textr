package controller;

import controller.adapter.TermiosTerminalAdapter;
import files.BufferCursorContext;
import inputhandler.FileBufferInputHandler;
import inputhandler.InputHandlingElement;
import inputhandler.SnakeInputHandler;
import util.Coords;
import util.Rectangle;
import ui.FileBufferView;
import ui.SnakeView;
import ui.View;

import exception.PathNotFoundException;
import java.io.IOException;
import java.util.HashMap;

class Window {
    public final View view;
    public final InputHandlingElement handler;
    private Window next = null;

    public Window(View v, InputHandlingElement h) {
        this.view = v;
        this.handler = h;
    }

    /**
     *  used for creating fist window witouth ever making view/InputHandlingElement
     */
    public Window(String path, byte[] lineSeparator, TermiosTerminalAdapter term) throws IOException, PathNotFoundException {
      FileBufferInputHandler handler = null;
      FileBufferView view = null;
      try {
        handler = new FileBufferInputHandler(path, lineSeparator);
        view = new FileBufferView(handler.getFileBufferContextTransparent(), term);
        insertWindow(new Window(view, handler));
      } catch (PathNotFoundException e) {
          throw e;
      }
      this.view = view;
      this.handler = handler;
    }

    public void insertWindow(Window window) {
      if (next == null) {
        this.next = window;
      } else {
        this.next.insertWindow(window);
      }
      return;
    }

    public int createFileBuffer(String path, byte[] lineSeparator, TermiosTerminalAdapter term) throws IOException, PathNotFoundException {
      FileBufferInputHandler handler;
      try {
        handler = new FileBufferInputHandler(path, lineSeparator);
      } catch (PathNotFoundException e) {
          throw e;
      }
      View view = new FileBufferView(handler.getFileBufferContextTransparent(), term);
      insertWindow(new Window(view, handler));
      return this.hashCode();
    }

    public int createSnakeGame(Coords crds, TermiosTerminalAdapter term) {
      SnakeInputHandler handler = new SnakeInputHandler(crds.width, crds.height);
      SnakeView view = new SnakeView(handler.getSnakeGame(), term);
      insertWindow(new Window(view, handler));
      return this.hashCode();
    }

    public int duplicate(TermiosTerminalAdapter term) {
      if (this.handler instanceof FileBufferInputHandler fbh) {
        BufferCursorContext dupedContext = new BufferCursorContext(fbh.getFileBufferContextTransparent());
        FileBufferView view = new FileBufferView(dupedContext, term);
        FileBufferInputHandler handler = new FileBufferInputHandler(dupedContext);
        insertWindow(new Window(view, handler));
        return this.hashCode();
      }
    return 0;
    }

    public int duplicateActive(TermiosTerminalAdapter term, int hash) {
      if (this.hashCode() == hash)
        return duplicate(term);
      else if (next != null)
        return next.duplicateActive(term, hash);
      return 0;
    }

    public void render(int hash) throws IOException {
      if (this.hashCode() == hash)
        view.render(this.hashCode());
      else if (next != null)
        next.render(hash);
    }

    public void renderAll() throws IOException {
      view.render(this.hashCode());
      if (next != null)
        next.renderAll();
    }

    public void save(int hash) {
      if (this.hashCode() == hash)
        handler.save();
      else if (next != null)
        next.save(hash);
    }
    
    public void renderCursor(int hash) throws IOException {
      if (this.hashCode() == hash)
        view.renderCursor();
      else if (next != null)
        next.renderCursor(hash);
    }

    public int forceClose(int hash) {
      if (next.hashCode() == hash){
        next = next.next;
        return next.hashCode();
      } else if (next != null)
        return next.forceClose(hash);
      return 0;
    }

    public boolean close(int hash) {
      if (this.hashCode() == hash)
        return handler.isSafeToClose();
      else if (next != null)
        return next.close(hash);
      return false;
    }

    public void handleArrowRight(int hash) {
      if (this.hashCode() == hash)
        handler.handleArrowRight();
      else if (next != null)
        next.handleArrowRight(hash);
    }

    public void handleArrowLeft(int hash) {
      if (this.hashCode() == hash)
        handler.handleArrowLeft();
      else if (next != null)
        next.handleArrowLeft(hash);
    }

    public void handleArrowUp(int hash) {
      if (this.hashCode() == hash)
        handler.handleArrowUp();
      else if (next != null)
        next.handleArrowUp(hash);
    }

    public void handleArrowDown(int hash) {
      if (this.hashCode() == hash)
        handler.handleArrowDown();
      else if (next != null)
        next.handleArrowDown(hash);
    }

    public void handleSeparator(int hash) throws IOException {
      if (this.hashCode() == hash)
        handler.handleSeparator();
      else if (next != null)
        next.handleSeparator(hash);
    }

    public void updateAllViewCords(HashMap<Integer, Rectangle> crdMap) {
      view.setScaledCoords(crdMap.get(this.hashCode()));
      if (next != null)
        next.updateAllViewCords(crdMap);
    }

    public void passToActive(int hash, byte b) throws IOException {
      if (this.hashCode() == hash)
        handler.input(b);
      else if (next != null)
        next.passToActive(hash, b);
    }

    public Coords getRealUICoordsFromScaled(int hash, TermiosTerminalAdapter term) throws IOException {
      if (this.hashCode() == hash)
        return view.getRealUICoordsFromScaled(term);
      else if (next != null)
        return next.getRealUICoordsFromScaled(hash, term);
      return new Coords(0, 0, 0, 0);
    }
}
