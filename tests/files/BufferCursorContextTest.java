import files.BufferCursorContext;
import files.FileAnalyserUtil;
import files.FileBuffer;
import files.FileHolder;
import org.junit.jupiter.api.Test;
import util.Debug;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

import static files.FileAnalyserUtil.getContentLines;
import static org.junit.jupiter.api.Assertions.*;

public class BufferCursorContextTest {
    @Test
    public void testConstructorWithPath() throws IOException {
        String path = "testresources/test.txt";
        Debug.write(path, "");
        BufferCursorContext newCtx = new BufferCursorContext(path, System.lineSeparator().getBytes());

        assertTrue(newCtx.getFileBuffer().equals(new FileBuffer(path, System.lineSeparator().getBytes())));
    }

    @Test
    public void testDeleteCharacterChangesBufBasic() throws IOException {

            String textToWrite = "hallo kaas i am your loyal student i use termios daily";

            Debug.write("testresources/test.txt", textToWrite);
            BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
            buffer.moveCursorRight();
            buffer.deleteCharacter();

            assertTrue(
                    FileHolder.areContentsEqual(
                            buffer.getFileBuffer().getBytes(),
                            "allo kaas i am your loyal student i use termios daily".getBytes()
                    )
            );
    }

    @Test
    public void testDeleteCharacterChangesBufInPoint() throws IOException {

        String textToWrite = System.lineSeparator()+ "i use termios daily";
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());

        buffer.moveCursorRight();
        buffer.deleteCharacter();

        assertTrue(
                FileHolder.areContentsEqual(
                        buffer.getFileBuffer().getBytes(),
                        "i use termios daily".getBytes()
                )
        );

    }

    @Test
    public void testMoveCursorDownBasic() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorDown();
        
        assertEquals(buffer.getInsertionPointLine(), 1);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorDownOutOfBounds() throws IOException {
        String firstLine = "this is a very long line, longer than the second";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);

        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        for(int i = 0; i< firstLine.length(); i++){
            buffer.moveCursorRight();
        }
        buffer.moveCursorDown();

        assertEquals(buffer.getInsertionPointLine(), 1);
        assertEquals(buffer.getInsertionPointCol(), secondLine.length());
    }

    @Test
    public void testMoveCursorDownBottom() throws IOException {
        String firstLine = "this is a very long line, longer than the second";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorDown();
        for(int i = 0; i< secondLine.length(); i++){
            buffer.moveCursorRight();
        }

        buffer.moveCursorDown();

        assertEquals(buffer.getInsertionPointLine(), 1);
        assertEquals(buffer.getInsertionPointCol(), secondLine.length());
    }

    @Test
    public void testMoveCursorUpBasic() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorDown();

        buffer.moveCursorUp();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorUpOutOfBounds() throws IOException {
        String firstLine = "this is a short line";
        String secondLine = "this is a very long line, longer than the first";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorDown();
        for(int i = 0; i< secondLine.length(); i++){
            buffer.moveCursorRight();
        }

        buffer.moveCursorUp();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), firstLine.length());
    }

    @Test
    public void testMoveCursorUpTop() throws IOException {
        String firstLine = "this is a very long line, longer than the second";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());

        buffer.moveCursorUp();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorLeftBasic() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorRight();

        buffer.moveCursorLeft();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorLeftAcross() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorDown();

        buffer.moveCursorLeft();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), firstLine.length());
    }

    @Test
    public void testMoveCursorLeftBeginning() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorLeft();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorRightBasic() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());

        buffer.moveCursorRight();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 1);
    }

    @Test
    public void testMoveCursorRightAcross() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        for(int i = 0; i<firstLine.length(); i++){
            buffer.moveCursorRight();
        }

        buffer.moveCursorRight();

        assertEquals(buffer.getInsertionPointLine(), 1);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorRightEnd() throws IOException {
        String firstLine = "hello everyone";
        Debug.write("testresources/test.txt", firstLine);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        for(int i = 0; i<firstLine.length(); i++){
            buffer.moveCursorRight();
        }
        buffer.moveCursorRight();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), firstLine.length());
    }

    @Test
    public void testClosingClean() throws IOException {
        String firstLine = "hello everyone";
        Debug.write("testresources/test.txt", firstLine);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());

        int saveStatus = buffer.close();

        assertEquals(saveStatus, 0);
    }

    @Test
    public void testClosingDirty() throws IOException {
        String firstLine = "hello everyone";
        Debug.write("testresources/test.txt", firstLine);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.write("a".getBytes()[0]);
        int saveStatus = buffer.close();

        assertEquals(saveStatus, 1);
    }

    @Test
    public void testSaveChangedFile() throws IOException {
        String firstLine = "hello everyone";
        Debug.write("testresources/test.txt", firstLine);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.write("a".getBytes()[0]);

        buffer.save();

        FileHolder fh = new FileHolder("testresources/test.txt", System.lineSeparator().getBytes());
        assertArrayEquals(fh.getContent(), "ahello everyone".getBytes());
    }

    @Test
    public void testWriteByte() throws IOException {
        String firstLine = "hello everyone";
        Debug.write("testresources/test.txt", firstLine);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorRight();
        buffer.write("a".getBytes()[0]);

        assertArrayEquals(FileAnalyserUtil.toArray(buffer.getByteContent()), "haello everyone".getBytes());
    }

    @Test
    public void testDirtyAfterChange() throws IOException {
        String firstLine = "hello everyone";
        Debug.write("testresources/test.txt", firstLine);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.write("a".getBytes()[0]);

        boolean dirtyStatus = buffer.getDirty();

        assertTrue(dirtyStatus);
    }

    @Test
    public void testDirtyClean() throws IOException {
        String firstLine = "hello everyone";
        Debug.write("testresources/test.txt", firstLine);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());

        boolean dirtyStatus = buffer.getDirty();

        assertFalse(dirtyStatus);
    }

    @Test
    public void testGetLines() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);

        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());

        ArrayList<ArrayList<Byte>> lines = buffer.getLines();

        assertEquals(lines, getContentLines(textToWrite.getBytes(), System.lineSeparator().getBytes()));
    }

    @Test
    public void testShallowCopyConstructor() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorRight();

        BufferCursorContext buffer2 = new BufferCursorContext(buffer);

        assertEquals(buffer.getFileBuffer(), buffer2.getFileBuffer());
        assertEquals(buffer.getInsertionPointCol(), buffer2.getInsertionPointCol());
        assertEquals(buffer.getInsertionPointLine(), buffer2.getInsertionPointLine());
    }

    @Test
    public void testDeepClone() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write("testresources/test.txt", textToWrite);
        BufferCursorContext buffer = new BufferCursorContext("testresources/test.txt", System.lineSeparator().getBytes());
        buffer.moveCursorRight();

        BufferCursorContext buffer2 = buffer.deepClone();

        assertEquals(buffer.getFileBuffer(), buffer2.getFileBuffer());
        assertEquals(buffer.getInsertionPointCol(), buffer2.getInsertionPointCol());
        assertEquals(buffer.getInsertionPointLine(), buffer2.getInsertionPointLine());
    }




}