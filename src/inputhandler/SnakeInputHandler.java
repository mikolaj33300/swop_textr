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
        this.contentsChangedSinceRender = true;
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
        currentWait++;
        if(currentWait + game.getRemovedDelay() >= this.MILLISECOND_BASE) {
            game.tick();
            currentWait = 0;
            contentsChangedSinceRender = true;
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
        if(!this.game.canContinue()) this.game = new SnakeGame(5, (int) this.playField.height, (int) this.playField.width);
    }

    private void move(MoveDirection dir) {
        if(!this.game.canContinue()) return;
        this.game.move(dir);
        this.game.tick();
        this.currentWait = 0;
        this.contentsChangedSinceRender = true;
    }

}
