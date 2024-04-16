package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UICoordsTest {
    UICoords uiCoords;

    @BeforeEach
    public void setUp(){
        uiCoords = new UICoords(1,2,3,4);
    }

    @Test
    public void testConstructor(){
        assertEquals(uiCoords.startX,1);
        assertEquals(uiCoords.startY,2);
        assertEquals(uiCoords.width,3);
        assertEquals(uiCoords.height,4);
    }

    @Test
    public void testClone(){
        UICoords uiCoords_clone = uiCoords.clone();
        assertEquals(uiCoords_clone,uiCoords);
        assertNotSame(uiCoords_clone,uiCoords);
    }

    @Test
    public void testEqualsSame(){
        assertEquals(uiCoords, new UICoords(1,2,3,4));
    }

    @Test
    public void testEqualsDifObject(){
        assertNotEquals(uiCoords, new Rectangle(1.0,2.0,3.0,4.0));
    }
    @Test
    public void testEqualsDifStartX(){
        assertNotEquals(uiCoords, new UICoords(0,2,3,4));
    }

    @Test
    public void testEqualsDigStartY(){
        assertNotEquals(uiCoords, new UICoords(1,0,3,4));
    }

    @Test
    public void testEqualsDifWidth(){
        assertNotEquals(uiCoords, new UICoords(1,2,0,4));
    }

    @Test
    public void testEqualsDifHeight(){
        assertNotEquals(uiCoords, new UICoords(1,2,3,0));
    }
}



