package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class PairTest {

    @Test
    public void testPair_Integer_TypesCorrect() {
        Pair p = new Pair(1, 1);
        assertInstanceOf(Integer.class, p.a);
        assertInstanceOf(Integer.class, p.b);
    }

    @Test
    public void testPair_StringInteger_TypesCorrect() {
        Pair p = new Pair("str", 1);
        assertInstanceOf(String.class, p.a);
        assertInstanceOf(Integer.class, p.b);
    }

    @Test
    public void testPair_StringInteger_ValuesCorrect() {
        Pair p = new Pair("str", 1);
        assertEquals(p.a, "str");
        assertEquals(p.b, 1);
    }

    @Test
    public void testPair_Integer_ValuesCorrect() {
        Pair p = new Pair(2, 1);
        assertEquals(p.a, 2);
        assertEquals(p.b, 1);
    }


}
