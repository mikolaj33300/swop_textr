package ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InsertionPointTest {
    @Test
    public void InsertionPointTest(){
        InsertionPoint insertionPoint = new InsertionPoint(0,1);
        assertEquals(insertionPoint.row,1);
        assertEquals(insertionPoint.col,0);
    }
}
