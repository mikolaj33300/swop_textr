package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Coords;
import util.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

public class CoordsTest {
    Coords coords;

    @BeforeEach
    public void setUp(){
        coords = new Coords(1,2,3,4);
    }

    @Test
    public void testConstructor(){
        assertEquals(coords.startX,1);
        assertEquals(coords.startY,2);
        assertEquals(coords.width,3);
        assertEquals(coords.height,4);
    }

    @Test
    public void testClone(){
        Coords coords_clone = coords.clone();
        assertEquals(coords_clone, coords);
        assertNotSame(coords_clone, coords);
    }

    @Test
    public void testEqualsSame(){
        assertEquals(coords, new Coords(1,2,3,4));
    }

    @Test
    public void testEqualsDifObject(){
        assertNotEquals(coords, new Rectangle(1.0,2.0,3.0,4.0));
    }
    @Test
    public void testEqualsDifStartX(){
        assertNotEquals(coords, new Coords(0,2,3,4));
    }

    @Test
    public void testEqualsDigStartY(){
        assertNotEquals(coords, new Coords(1,0,3,4));
    }

    @Test
    public void testEqualsDifWidth(){
        assertNotEquals(coords, new Coords(1,2,0,4));
    }

    @Test
    public void testEqualsDifHeight(){
        assertNotEquals(coords, new Coords(1,2,3,0));
    }
}



