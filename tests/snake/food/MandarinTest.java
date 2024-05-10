package snake.food;

import org.junit.jupiter.api.Test;
import util.Pos;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MandarinTest {
    @Test
    public void testConstructor() {
        Food food = new Mandarin(new Pos(1,1));
        assertEquals(food.getCharacter(), "M");
        assertEquals(food.getPosition(), new Pos(1,1));
        assertEquals(food.getScore(), 300);
        assertEquals(food.getGrowAmount(), 30);
    }
}
