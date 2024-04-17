package snake.fruit;

import org.junit.jupiter.api.Test;
import snake.Pos;
import snake.fruits.Fruit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class TestFruit {


    @Test
    public void testGetters() {
        Fruit f = new Fruit("a", 1, 1, new Pos(1,1));
        assertEquals(f.getCharacter(), "a");
        assertEquals(f.getPosition(), new Pos(1,1));
        assertEquals(f.getScore(), 1);
        assertEquals(f.getGrowAmount(), 1);
    }

    @Test
    public void testClone() {
        Fruit f = new Fruit("b", 2, 4, new Pos(1, -1));
        Fruit b = f.clone();
        assertNotSame(f, b);
    }

    @Test
    public void testEquals() {
        Fruit f = new Fruit("b", 2, 4, new Pos(1, -1));
        Fruit b = f.clone();
        assertEquals(f, b);
    }

}
