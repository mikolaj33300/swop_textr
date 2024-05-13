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
    public final int hash;
    private Window next = null;

    public Window(View v, InputHandlingElement h) {
        this.view = v;
        this.handler = h;
        this.hash=v.hashCode();
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
      this.hash = view.hashCode();
    }

    public void insertWindow(Window window) {
      if (next == null) {
        this.next = window;
      } else {
        this.next.insertWindow(window);
      }
      return;
    }

    public Window getNext() {
      return next;
    }

    public int createFileBuffer(String path, byte[] lineSeparator, TermiosTerminalAdapter term) throws IOException, PathNotFoundException {
      FileBufferInputHandler handler;
      try {
        handler = new FileBufferInputHandler(path, lineSeparator);
      } catch (PathNotFoundException e) {
          throw e;
      }
      View view = new FileBufferView(handler.getFileBufferContextTransparent(), term);
      Window win = new Window(view, handler);
      insertWindow(win);
      return win.hashCode();
    }

    public int createSnakeGame(Coords crds, TermiosTerminalAdapter term) {
      SnakeInputHandler handler = new SnakeInputHandler(crds.width, crds.height);
      SnakeView view = new SnakeView(handler.getSnakeGame(), term);
      Window win = new Window(view, handler);
      insertWindow(win);
      return win.hashCode();
    }

    public int duplicate(TermiosTerminalAdapter term) {
      if (this.handler instanceof FileBufferInputHandler fbh) {
        BufferCursorContext dupedContext = new BufferCursorContext(fbh.getFileBufferContextTransparent());
        FileBufferView view = new FileBufferView(dupedContext, term);
        FileBufferInputHandler handler = new FileBufferInputHandler(dupedContext);
        Window win = new Window(view, handler);
        insertWindow(win);
        return win.hashCode();
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
      if (next != null && next.hashCode() == hash){
        if (next.next != null){
          next = next.next;
          return next.hashCode();
        }
        return this.hashCode();
      } else if (next != null) {
        return next.forceClose(hash);
      }
    }

    public boolean close(int hash) {
      if (hashCode() == hash)
        return handler.isSafeToClose();
      else if (next != null)
        return next.close(hash);
      return false;// should not be reached
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
      Rectangle crd = crdMap.get(this.hashCode());
      if (crd == null){
        System.out.println("hash not found");
        System.exit(1);
      } else {
       view.setScaledCoords(crd);
       if (next != null)
        next.updateAllViewCords(crdMap);
      }
    }

    public void passToActive(int hash, byte b) throws IOException {
      if (this.hashCode() == hash)
        handler.input(b);
      else if (next != null)
        next.passToActive(hash, b);
    }

    public Coords getRealUICoordsFromScaled(int hash, TermiosTerminalAdapter term) throws IOException {
      if (this.hashCode() == hash){
        Coords res = view.getRealUICoordsFromScaled(term);
        System.out.printf("returning: %d, %d\n", res.width, res.height);
        return res;
      } else if (next != null) {
        return next.getRealUICoordsFromScaled(hash, term);
      }
      System.out.println("could not return correct coordinates");
      System.exit(1);
      return new Coords(0, 0, 0, 0);
    }

    @Override
    public int hashCode() {
      return hash;
    }
}
