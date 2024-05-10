package snake.food;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Pos;

import static org.junit.jupiter.api.Assertions.*;

public class TestFood {
    Food food;
    @BeforeEach
    public void setUp(){
        food = new Food("a", 1, 1, new Pos(1,1));
    }

    @Test
    public void testGetCharacter(){
        assertEquals(food.getCharacter(), "a");
    }

    @Test
    public void testGetPosition(){
        Pos position = new Pos(1,1);
        Food food1 = new Food("a", 1, 1, position);

        assertEquals(food.getPosition(), position);
        assertNotSame(food.getPosition(), position);
    }

    @Test
    public void testGetScore(){
        assertEquals(food.getScore(), 1);
    }

    @Test
    public void testGetGrowAmount(){
        assertEquals(food.getGrowAmount(), 1);
    }



    @Test
    public void testClone() {
        Food clone_food = food.clone();
        assertEquals(food, clone_food);
        assertNotSame(food, clone_food);
    }

    @Test
    public void testEqualsSame() {
        Food same_food = new Food("a", 1, 1, new Pos(1,1));
        assertEquals(food, same_food);
    }

    @Test
    public void testEqualsDifferentCharacter(){
        Food dif_food = new Food("b", 1, 4, new Pos(1, -1));
        assertNotEquals(dif_food, food);
    }

    @Test
    public void testEqualsDifferentGrowAmount(){
        Food dif_food = new Food("a", 3, 4, new Pos(1, -1));
        assertNotEquals(dif_food, food);
    }

    @Test
    public void testEqualsDifferentScore(){
        Food dif_food = new Food("a", 1, 5, new Pos(1, -1));
        assertNotEquals(dif_food, food);
    }

    @Test
    public void testEqualsDifferentPosContent(){
        Food dif_food = new Food("a", 1, 4, new Pos(1, 1));
        assertNotEquals(dif_food, food);
    }

    @Test
    public void testGetMillisecondDecrease(){
        assertEquals(food.getMillisecondDecrease(),0.01f);
    }
}
