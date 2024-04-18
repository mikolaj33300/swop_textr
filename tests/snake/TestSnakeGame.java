package snake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import snake.food.Apple;
import snake.food.Food;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSnakeGame {

    SnakeGame game;

    @BeforeEach
    public void init() {
        this.game = new SnakeGame(5, 20, 20);
    }

    @Test
    public void testSnakeInitialization() {

        assertEquals(game.getFruits().size(), game.MAX_FRUITS);
        assertEquals(game.getScore(), 0);
        assertEquals(game.getSnake().getEnd(), new Pos(10, 10));

    }

    @Test
    public void testMove() {
        game.move(MoveDirection.LEFT);
        assertEquals(game.getSnake().getDirection(), MoveDirection.RIGHT);
        game.move(MoveDirection.UP);
        assertEquals(game.getSnake().getDirection(), MoveDirection.UP);
        game.move(MoveDirection.RIGHT);
        assertEquals(game.getSnake().getDirection(), MoveDirection.RIGHT);
        game.move(MoveDirection.DOWN);
        assertEquals(game.getSnake().getDirection(), MoveDirection.DOWN);
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

}