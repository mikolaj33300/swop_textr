package snake.food;

import org.junit.jupiter.api.Test;
import util.Pos;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppleTest {
    @Test
    public void testConstructor() {
        Food food = new Apple(new Pos(1,1));
        assertEquals(food.getCharacter(), "A");
        assertEquals(food.getPosition(), new Pos(1,1));
        assertEquals(food.getScore(), 150);
        assertEquals(food.getGrowAmount(), 1);
    }
}
