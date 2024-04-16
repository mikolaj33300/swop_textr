package inputhandler;

import snake.MoveDirection;
import snake.SnakeGame;
import ui.Rectangle;

import java.io.IOException;

public class SnakeInputHandler extends InputHandlingElement {

    private SnakeGame game;
    private int currentWait = 0;
    private final int MILLISECOND_BASE = 1000;
    private Rectangle playField;

    public SnakeInputHandler(int maxX, int maxY) {
        this.playField = new Rectangle(0,0, maxX, maxY);
        this.game = new SnakeGame(5, maxX, maxY);
    }

    public SnakeGame getSnakeGame() {
        return game;
    }

    @Override
    public int close() {
        return 1;
    }

    @Override
    public void save() {
        return;
    }

    @Override
    public void input(byte b) throws IOException {
        // Called on idle
        idle();
        return;
    }

    @Override
    public boolean isSafeToClose() {
        return true;
    }

    @Override
    public void handleArrowRight() {
        this.move(MoveDirection.RIGHT);
    }

    @Override
    public void handleArrowLeft() {
        this.move(MoveDirection.LEFT);
    }

    @Override
    public void handleArrowDown() {
        this.move(MoveDirection.DOWN);
    }

    @Override
    public void handleArrowUp() {
        this.move(MoveDirection.UP);
    }

    @Override
    public void handleSeparator() throws IOException {
        if(!this.game.canContinue()) this.game = new SnakeGame(5, (int) this.playField.height, (int) this.playField.width);
    }

    /**
     * Called from {@link inputhandler.SnakeInputHandler#input(byte)}
     *
     * We calculate here if the snake game should be ticked {@link SnakeGame#tick()} by calculating the
     * wait time: {@link SnakeInputHandler#currentWait} + {@link SnakeGame#getRemovedDelay()} >= {@link SnakeInputHandler#MILLISECOND_BASE}
     */
    private void idle() {
        this.currentWait++;
        // We add one millisecond to our current wait time.
        currentWait++;

        // We check if our current wait time minus our removed delay exceeded the wait time to tick the game
        if(this.currentWait + this.game.getRemovedDelay() >= this.MILLISECOND_BASE) {
            this.game.tick();
            currentWait = 0;
        }
    }

    private void move(MoveDirection dir) {
        this.game.move(dir);
        this.game.tick();
        this.currentWait = 0;
    }

}
