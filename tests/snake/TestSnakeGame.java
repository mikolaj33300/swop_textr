package snake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snake.food.Apple;
import snake.food.Food;

import static org.junit.jupiter.api.Assertions.*;

public class TestSnakeGame {

    SnakeGame game;

    @BeforeEach
    public void init() {
        this.game = new SnakeGame(5, 50, 50);
    }

    @Test
    public void testSnakeInitialization() {

        assertEquals(game.getFruits().size(), game.MAX_FRUITS);
        assertEquals(game.getScore(), 0);
        assertEquals(game.getSnake().getEnd(), new Pos(25, 25));

    }

    @Test
    public void testMoveLeft() {
        game.move(MoveDirection.LEFT);
        assertEquals(game.getSnake().getDirection(), MoveDirection.RIGHT);
    }
    @Test
    public void testMoveUp() {
        game.move(MoveDirection.UP);
        assertEquals(game.getSnake().getDirection(), MoveDirection.UP);
    }

    @Test
    public void testMoveRight() {
        game.move(MoveDirection.RIGHT);
        assertEquals(game.getSnake().getDirection(), MoveDirection.RIGHT);
    }
    @Test
    public void testMoveDown() {
        game.move(MoveDirection.DOWN);
        assertEquals(game.getSnake().getDirection(), MoveDirection.DOWN);
    }

    @Test
    public void testMoveDownLeft() {
        game.move(MoveDirection.DOWN);
        game.tick();
        game.move(MoveDirection.LEFT);
        assertEquals(game.getSnake().getDirection(), MoveDirection.LEFT);
    }

    @Test
    public void testEat() {
        assertEquals(game.getSnake().getStart().distanceX(game.getSnake().getEnd()), 5);
        // Generate fruit
        Pos nextPos = game.getSnake().getEnd();
        Food apple = new Apple(game.getSnake().getNext(nextPos));
        game.foods.add(apple);
        // Progress snake
        game.tick(); // eat
        game.tick(); // grow
        // Test length
        assertEquals(game.getSnake().getStart().distanceX(game.getSnake().getEnd()), 5 + apple.getGrowAmount());
        assertEquals(game.getScore(), 0 + apple.getScore() + 1);
        assertEquals(game.getFruits().size(), game.MAX_FRUITS);
    }

    @Test
    public void testStarve() {
        assertEquals(this.game.getSnake().getLength(), 5);
        for(int i = 0; i < 30; i++)
            this.game.tick();
        assertTrue(this.game.getSnake().getLength() < 5);
    }

    @Test
    public void testOutOfPlayField() {
        for(int i = 0; i < 60; i++)
            this.game.tick();
        assertEquals(game.canContinue(), false);
    }

    @Test
    public void loseDueStarvation() {
        this.game = new SnakeGame(1, 50, 50);
        for(int i = 0; i < 40; i++)
            this.game.tick();
        assertFalse(game.canContinue());
    }

    @Test
    public void testWin() {
        this.game = new SnakeGame(game.WIN_LENGTH, 50, 50);
        this.game.tick();
        assertTrue(game.isWon());
    }

    @Test
    public void testReset() {
        this.game.tick();
        this.game.reset();
        assertFalse(game.isWon());
        assertTrue(game.canContinue());
        assertEquals(game.getSnake().getLength(), 5);
        assertEquals(game.getScore(), 0);
        assertEquals(game.getRemovedDelay(), 0);
    }

    @Test
    public void testRemovedDelayIncrease() {
        this.game = new SnakeGame(5, 50, 50);
        assertEquals(game.getRemovedDelay(), 0);
        this.game.foods.add(new Apple(new Pos(40, 25)));
        for(int i = 0; i < 20; i++)
            game.tick();
        assertNotEquals(game.getRemovedDelay(), 0);
    }

    @Test
    public void testMillisecondThreshold() {
        assertEquals(game.getMillisecondThreshold(), game.MILLISECOND_THRESHOLD);
    }

}
