package files;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static files.FileAnalyserUtil.getContentLines;
import static org.junit.jupiter.api.Assertions.*;

public class FileAnalyserUtilTest {
    @Test
    public void testGetContentLines() {
        byte[] testingByteArr = {65, 65, 65};
        ArrayList<ArrayList<Byte>> a = getContentLines(testingByteArr);
        assertEquals(testingByteArr[0], a.get(0).get(0).byteValue());
    }

    // test enkel relevant op mac
    @Test
    public void testLineSeparatorRemoval() {
        String test = "mister"+System.lineSeparator()+"abc";
        byte[] bytes = test.getBytes();
        ArrayList<ArrayList<Byte>> a = getContentLines(bytes);

        assertEquals(6, a.get(0).size());
        assertEquals(3, a.get(1).size());
    }

}