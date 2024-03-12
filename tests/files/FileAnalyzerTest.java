package files;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static files.FileAnalyserUtil.getContentLines;
import static org.junit.jupiter.api.Assertions.*;

public class FileAnalyzerTest {
    @Test
    public void testGetContentLines() {
        byte[] testingByteArr = {65, 65, 65};
        ArrayList<ArrayList<Byte>> a = getContentLines(testingByteArr);
        assertEquals(testingByteArr[0], a.get(0).get(0).byteValue());
    }
}