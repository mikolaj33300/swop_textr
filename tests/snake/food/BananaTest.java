package snake.food;

import org.junit.jupiter.api.Test;
import util.Pos;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BananaTest {
    @Test
    public void testConstructor() {
        Food food = new Banana(new Pos(1,1));
        assertEquals(food.getCharacter(), "B");
        assertEquals(food.getPosition(), new Pos(1,1));
        assertEquals(food.getScore(), 100);
        assertEquals(food.getGrowAmount(), 2);
    }
}
