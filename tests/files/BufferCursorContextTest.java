import files.BufferCursorContext;
import files.FileBuffer;
import files.FileHolder;
import org.junit.jupiter.api.Test;
import util.Debug;

import java.io.IOException;
import java.util.ArrayList;

import static files.FileAnalyserUtil.getContentLines;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

}