package inputhandler;

import util.MoveDirection;
import snake.SnakeGame;
import util.Rectangle;
import util.RenderIndicator;

import java.io.IOException;

public class SnakeInputHandler extends InputHandlingElement {

    private SnakeGame game;
    private int currentWait = 0;
    private final int MILLISECOND_BASE;
    private Rectangle playField;

    public SnakeInputHandler(int maxX, int maxY) {
        this.playField = new Rectangle(0,0, maxX, maxY);
        this.game = new SnakeGame(5, maxX, maxY);
        this.contentsChangedSinceRender = true;
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
            contentsChangedSinceRender = true;
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
    public void handleArrowRight() {
        this.move(MoveDirection.RIGHT);
    }

    /**
     * handle the left arrow
     */
    @Override
    public void handleArrowLeft() {
        this.move(MoveDirection.LEFT);
    }

    /**
     * handle the down arrow
     */
    @Override
    public void handleArrowDown() {
        this.move(MoveDirection.DOWN);
    }

    /**
     * handle the up arrow
     */
    @Override
    public void handleArrowUp() {
        this.move(MoveDirection.UP);
    }

    /**
     * handle the separator
     */
    @Override
    public void handleSeparator() throws IOException {
        if(!this.game.canContinue()) {
            this.currentWait = 0;
            this.game.reset();
            this.contentsChangedSinceRender = true;
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
        this.contentsChangedSinceRender = true;
    }

}
