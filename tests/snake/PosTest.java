package snake;

import org.junit.jupiter.api.Test;
import util.Pos;

import static org.junit.jupiter.api.Assertions.*;

public class PosTest {

    @Test
    public void testClone() {
        Pos a = new Pos(1,2);
        Pos b = a.clone();
        assertEquals(a, b);
        assertNotSame(a, b);
    }

    @Test
    public void testEquals() {
        Pos a = new Pos(1,2);
        Pos b = new Pos(1,3);
        assertNotEquals(a, b);
        assertEquals(a, new Pos(1,2));
        assertEquals(a.clone(), a);
        assertNotEquals(a, new String("hello mister"));
    }

    @Test
    public void testGetters() {
        Pos a = new Pos(1,3);
        assertEquals(a.x(), 1);
        assertEquals(a.y(), 3);
    }

    @Test
    public void testDistance1() {
        Pos a = new Pos(0,0);
        Pos test = new Pos(1,3);
        assertEquals(a.distanceX(test), 1);
        assertEquals(a.distanceY(test), 3);
    }

    @Test
    public void testDistance2() {
        Pos a = new Pos(0,0);
        Pos test = new Pos(-1,3);
        assertEquals(a.distanceX(test), 1);
        assertEquals(a.distanceY(test), 3);
    }

    @Test
    public void testInBetween1() {
        Pos a = new Pos(0,0);
        Pos b = new Pos(0, 2);
        assertEquals(Pos.isBetween1D(a, b, new Pos(0,3)), false);
    }

    @Test
    public void testInBetween2() {
        Pos a = new Pos(0,0);
        Pos b = new Pos(0, 2);
        assertEquals(Pos.isBetween1D(a, b, new Pos(0,1)), true);
    }

    @Test
    public void testInBetween3() {
        Pos a = new Pos(0,0);
        Pos b = new Pos(0, 2);
        assertEquals(Pos.isBetween1D(a, b, new Pos(1,1)), false);
    }

    @Test
    public void testInBetween4() {
        Pos a = new Pos(0,0);
        Pos b = new Pos(0, 2);
        assertEquals(Pos.isBetween1D(a, b, new Pos(0,0)), true);
    }

    @Test
    public void testInBetween5() {
        Pos a = new Pos(0,2);
        Pos b = new Pos(1, 2);
        assertEquals(Pos.isBetween1D(a, b, new Pos(1,2)), true);
    }

    @Test
    public void testPrint() {
        Pos a = new Pos(1,1);
        assertEquals(a.getPrint(), "[1, 1]");
    }

}
