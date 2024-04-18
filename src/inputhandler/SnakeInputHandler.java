package inputhandler;

import snake.MoveDirection;
import snake.SnakeGame;
import snake.SnakeHead;
import ui.Rectangle;

import java.io.IOException;

public class SnakeInputHandler extends InputHandlingElement {

    private SnakeGame game;
    private int currentWait = 0;
    private final int MILLISECOND_BASE;
    private Rectangle playField;

    public SnakeInputHandler(int maxX, int maxY) {
        this.playField = new Rectangle(0,0, maxX, maxY);
        this.game = new SnakeGame(5, maxX, maxY);
        this.MILLISECOND_BASE = this.game.getMillisecondThreshold();
        this.needsRerender = true;
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
        if(!game.canContinue()) return;
        currentWait++;
        if(currentWait + (MILLISECOND_BASE*game.getRemovedDelay()) >= this.MILLISECOND_BASE) {
            game.tick();
            currentWait = 0;
            needsRerender = true;
        }
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
        if(!this.game.canContinue()) {
            this.currentWait = 0;
            this.game.reset();
            this.needsRerender = true;
        }
    }

    private void move(MoveDirection dir) {
        if(!this.game.canContinue()) return;
        this.game.move(dir);
        this.game.tick();
        this.currentWait = 0;
        this.needsRerender = true;
    }

}
