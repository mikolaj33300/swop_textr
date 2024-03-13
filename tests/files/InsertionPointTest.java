package files;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class InsertionPointTest {

    Statusbar sb1;

    @BeforeEach
    void setUp(){
        String path1 = "testresources/test.txt";
        FileBuffer fb1 = new FileBuffer(path1);
        sb1 = new Statusbar(fb1);
    }

    @Test
    void testGetScroll(){
        assertEquals(sb1.getScroll(),0);
    }
    @Test
    void testGetLines(){

    }
    @Test
    void testRenderStatus(){

    }
    @Test
    void testGetScrollbar(){

    }
}
/*
the number of lines and number of characters
in the file buffer, the line and column of the insertion point, and, optionally, a
horizontal scroll bar.
 */