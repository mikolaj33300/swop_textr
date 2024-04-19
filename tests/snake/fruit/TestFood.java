package snake.fruit;

import org.junit.jupiter.api.Test;
import util.Pos;
import snake.food.Food;

import static org.junit.jupiter.api.Assertions.*;

public class TestFood {


    @Test
    public void testGetters() {
        Food f = new Food("a", 1, 1, new Pos(1,1));
        assertEquals(f.getCharacter(), "a");
        assertEquals(f.getPosition(), new Pos(1,1));
        assertEquals(f.getScore(), 1);
        assertEquals(f.getGrowAmount(), 1);
    }

    @Test
    public void testClone() {
        Food f = new Food("b", 2, 4, new Pos(1, -1));
        Food b = f.clone();
        assertNotSame(f, b);
    }

    @Test
    public void testEquals() {
        Food f = new Food("b", 2, 4, new Pos(1, -1));
        Food b = f.clone();
        assertEquals(f, b);
        assertNotEquals(f, new String("a"));
    }

}
