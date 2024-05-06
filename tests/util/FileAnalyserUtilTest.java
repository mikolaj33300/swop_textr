package util;

import files.FileAnalyserUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static files.FileAnalyserUtil.getContentLines;
import static org.junit.jupiter.api.Assertions.*;

public class FileAnalyserUtilTest {
    @Test
    public void testGetContentLinesBasic() {
        byte[] testingByteArr = {65, 65, 65};
        ArrayList<ArrayList<Byte>> a = getContentLines(testingByteArr, System.lineSeparator().getBytes());
        assertEquals(testingByteArr[0], a.get(0).get(0).byteValue());
    }

    @Test
    public void testGetContentLinesComplex() {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;

        ArrayList<ArrayList<Byte>> a = getContentLines(textToWrite.getBytes(), System.lineSeparator().getBytes());

        ArrayList<ArrayList<Byte>> toCompare = new ArrayList<>();
        ArrayList<Byte> toCompareFirst = FileAnalyserUtil.createByteWrapArrayList(firstLine.getBytes());
        ArrayList<Byte> toCompareSecond = FileAnalyserUtil.createByteWrapArrayList(secondLine.getBytes());
        toCompare.add(toCompareFirst);
        toCompare.add(toCompareSecond);

        assertEquals(a, toCompare);
    }

    // test enkel relevant op mac
    @Test
    public void testLineSeparatorRemoval() {
        String test = "mister"+System.lineSeparator()+"abc";
        byte[] bytes = test.getBytes();
        ArrayList<ArrayList<Byte>> a = getContentLines(bytes, System.lineSeparator().getBytes());

        assertEquals(6, a.get(0).size());
        assertEquals(3, a.get(1).size());
    }

}