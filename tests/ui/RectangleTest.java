package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RectangleTest {
    Rectangle rectangle;

    @BeforeEach
    public void setUp(){
        rectangle = new Rectangle(1,2,3,4);
    }

    @Test
    public void testConstructor(){
        assertEquals(rectangle.startX,1);
        assertEquals(rectangle.startY,2);
        assertEquals(rectangle.width,3);
        assertEquals(rectangle.height,4);
    }

    @Test
    public void testClone(){
        Rectangle rectangle_clone = rectangle.clone();
        assertEquals(rectangle_clone,rectangle);
        assertNotSame(rectangle_clone,rectangle);
    }

    @Test
    public void testEqualsSame(){
        assertEquals(rectangle, new Rectangle(1.0,2.0,3.0,4.0));
    }

    @Test
    public void testEqualsDifObject(){
        assertNotEquals(rectangle, new UICoords(1,2,3,4));
    }
    @Test
    public void testEqualsDifStartX(){
        assertNotEquals(rectangle, new Rectangle(0.0,2.0,3.0,4.0));
    }

    @Test
    public void testEqualsDigStartY(){
        assertNotEquals(rectangle, new Rectangle(1.0,0.0,3.0,4.0));
    }

    @Test
    public void testEqualsDifWidth(){
        assertNotEquals(rectangle, new Rectangle(1.0,2.0,0.0,4.0));
    }

    @Test
    public void testEqualsDifHeight(){
        assertNotEquals(rectangle, new Rectangle(1.0,2.0,3.0,0.0));
    }
}