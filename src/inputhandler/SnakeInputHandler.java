package inputhandler;

import util.MoveDirection;
import snake.SnakeGame;
import util.Rectangle;
import util.RenderIndicator;

import java.io.IOException;

public class SnakeInputHandler extends InputHandlingElement {

  /**
   * the contained SnakeGame
   */
    private SnakeGame game;
    /**
     * the amount of milliseconds waited
     */
    private int currentWait = 0;
    /**
     * the amount of millis to wait till updating the game
     */
    private final int MILLISECOND_BASE;
    /**
     * the rectangle the snake must keep in
     */
    private Rectangle playField;

    /**
     * create a new SnakeInputHandler with the maxX and maxY boundaries
     * @param maxX the maximum the snake can go in horizontal direction
     * @param maxY the maximum the snake can go in vertical direction
     */
    public SnakeInputHandler(int maxX, int maxY) {
        this.playField = new Rectangle(0,0, maxX, maxY);
        this.game = new SnakeGame(5, maxX, maxY);
        this.MILLISECOND_BASE = this.game.getMillisecondThreshold();
    }

    /**
     * @return the enclosed snakegame
     */
    public SnakeGame getSnakeGame() {
        return game;
    }

    /**
     * @return not closable
     */
    @Override
    public int forcedClose() {
        return 1;
    }

    /**
     * snake can't be saved at the moment
     */
    @Override
    public void save() {
        return;
    }

    /**
     * @param b the input to be handled by snake
     */
    @Override
    public RenderIndicator input(byte b) throws IOException {
        if(!game.canContinue()) return RenderIndicator.NONE;
        currentWait++;
        if(currentWait + game.getRemovedDelay() >= this.MILLISECOND_BASE && this.game.canContinue()) {
            game.tick();
            currentWait = 0;
	        return RenderIndicator.FULL;
        }
        return RenderIndicator.NONE;
    }

    /**
     * @return snake is safe to close
     */
    @Override
    public boolean isSafeToClose() {
        return true;
    }

    /**
     * handle the right arrow
     */
    @Override
    public RenderIndicator handleArrowRight() {
        this.move(MoveDirection.RIGHT);
	return RenderIndicator.WINDOW;
    }

    /**
     * handle the left arrow
     */
    @Override
    public RenderIndicator handleArrowLeft() {
        this.move(MoveDirection.LEFT);
	return RenderIndicator.WINDOW;
    }

    /**
     * handle the down arrow
     */
    @Override
    public RenderIndicator handleArrowDown() {
        this.move(MoveDirection.DOWN);
	return RenderIndicator.WINDOW;
    }

    /**
     * handle the up arrow
     */
    @Override
    public RenderIndicator handleArrowUp() {
        this.move(MoveDirection.UP);
	return RenderIndicator.WINDOW;
    }

    /**
     * handle the separator
     */
    @Override
    public RenderIndicator handleSeparator() throws IOException {
        if(!this.game.canContinue()) {
            this.currentWait = 0;
            this.game.reset();
	    return  RenderIndicator.WINDOW;
        } else {
	    return  RenderIndicator.NONE;
	}
    }

    /**
     * @param dir the new direction for snake
     */
    private void move(MoveDirection dir) {
        if(!this.game.canContinue()) return;
        this.game.move(dir);
        this.game.tick();
        this.currentWait = 0;
    }

    /**
     * run the visitors code on this object
     * @param v
     */
    public void accept(InputHandlerVisitor v){
        v.visitSnakeInputHandler(this);
    }

}
