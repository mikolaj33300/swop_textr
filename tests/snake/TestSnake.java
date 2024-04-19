package snake;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.MoveDirection;
import util.Pos;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestSnake {

    SnakeHead snake, snakemoved;
    Pos startPos;

    @BeforeEach
    public void init() {
        this.startPos = new Pos(4,3);
        this.snake = new SnakeHead(5, startPos.x(), startPos.y());

        this.snakemoved = new SnakeHead(3, 4, 5);
        this.snakemoved.move(MoveDirection.UP);
        this.snakemoved.tick();
    }

    @Test
    public void testConstructorTasks() {
        assertEquals(snake.getStart(), new Pos(4-5, 3));
        assertEquals(snake.getEnd(), new Pos(4, 3));
        assertEquals(snake.getSegments().length, 0);
        assertEquals(snake.getDirection(), MoveDirection.RIGHT);
    }

    @Test
    public void testMoveDirectionStringDefault() {
        assertEquals(snake.getHeadString(), ">");
    }

    @Test
    public void testMoveDirectionStringUp() {
        snake.move(MoveDirection.UP);
        assertEquals(snake.getHeadString(), "^");
    }

    @Test
    public void testMoveDirectionStringDown() {
        snake.move(MoveDirection.DOWN);
        assertEquals(snake.getHeadString(), "v");
    }

    @Test
    public void testMoveDirectionStringLeft() {
        snake.move(MoveDirection.LEFT);
        assertEquals(snake.getHeadString(), ">");
        snake.move(MoveDirection.UP);
        snake.move(MoveDirection.LEFT);
        assertEquals(snake.getHeadString(), "<");
    }

    @Test
    public void testMoveRight() {
        snake.move(MoveDirection.RIGHT);
        assertEquals(snake.getDirection(), MoveDirection.RIGHT);
        assertEquals(snake.getSegments().length, 0);
    }

    @Test
    public void testMoveLeft() {
        snake.move(MoveDirection.RIGHT);
        snake.move(MoveDirection.LEFT);
        assertEquals(snake.getDirection(), MoveDirection.RIGHT);
        snake.move(MoveDirection.UP);
        snake.move(MoveDirection.LEFT);
        assertEquals(snake.getDirection(), MoveDirection.LEFT);
        assertEquals(snake.getSegments().length, 2);
    }

    @Test
    public void testGetNextRight() {
        Pos next = snake.getNext(startPos);
        assertEquals(next, new Pos(startPos.x()+1, startPos.y()));
    }

    @Test
    public void testGetNextLeft() {
        snake.move(MoveDirection.UP);
        snake.move(MoveDirection.LEFT);
        Pos next = snake.getNext(startPos);
        assertEquals(next, new Pos(startPos.x()-1, startPos.y()));
    }

    @Test
    public void testGetNextUp() {
        snake.move(MoveDirection.UP);
        Pos next = snake.getNext(startPos);
        assertEquals(next, new Pos(startPos.x(), startPos.y()-1));
    }

    @Test
    public void testGetNextDown() {
        snake.move(MoveDirection.DOWN);
        Pos next = snake.getNext(startPos);
        assertEquals(next, new Pos(startPos.x(), startPos.y()+1));
    }

    @Test
    public void testProgressRight() {
        snake.tick();
        assertEquals(snake.getEnd(), new Pos(
                startPos.x()+1, startPos.y()
        ));
    }

    @Test
    public void testProgressLeft() {
        snake.move(MoveDirection.UP);
        snake.move(MoveDirection.LEFT);
        snake.tick();
        assertEquals(snake.getEnd(), new Pos(
                startPos.x()-1, startPos.y()
        ));
    }

    @Test
    public void testProgressUp() {
        snake.move(MoveDirection.UP);
        snake.tick();
        assertEquals(snake.getEnd(), new Pos(
                startPos.x(), startPos.y()-1
        ));
    }

    @Test
    public void testProgressDown() {
        snake.move(MoveDirection.DOWN);
        snake.tick();
        assertEquals(snake.getEnd(), new Pos(
                startPos.x(), startPos.y()+1
        ));
    }

    @Test
    public void testEat() {
        snake.grow(1);
        // Test length before == 5
        assertEquals(
                snake.getStart().distanceX(snake.getEnd()), 5);
        snake.tick();
        // Test length has increased
        assertEquals(
                snake.getStart().distanceX(snake.getEnd()), 6
        );
        // Test if grow amount decreased correctly and snake doesn't grow in size again
        snake.tick();
        assertEquals(
                snake.getStart().distanceX(snake.getEnd()), 6
        );
    }

    @Test
    public void testTick() {
        assertEquals(snakemoved.getSegments().length, 1);
        snakemoved.tick();
        snakemoved.tick();
        snakemoved.tick();
        assertEquals(snakemoved.getSegments().length, 0);
    }

}
