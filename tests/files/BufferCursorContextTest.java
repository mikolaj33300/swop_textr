package files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.Debug;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static files.FileAnalyserUtil.getContentLines;
import static org.junit.jupiter.api.Assertions.*;

public class BufferCursorContextTest {

    @TempDir
    Path path1, path2, path3;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test1.txt");
        path2 = path2.resolve("test.txt");
        path3 = path3.resolve("test2.txt");
        Files.write(path1, "hallo".getBytes());
    }

    @Test
    public void testConstructorWithPath() throws IOException {
        BufferCursorContext newCtx = new BufferCursorContext(path1.toString(), System.lineSeparator().getBytes());

        assertTrue(newCtx.getFileBuffer().equals(new FileBuffer(path1.toString(), System.lineSeparator().getBytes())));
    }

    @Test
    public void testDeleteCharacterChangesBufBasic() throws IOException {

      
        String textToWrite = "hallo kaas i am your loyal student i use termios daily";
        Debug.write(path3.toString(), textToWrite);

        BufferCursorContext buffer = new BufferCursorContext(path3.toString(), System.lineSeparator().getBytes());
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

        String textToWrite = System.lineSeparator() + "i use termios daily";
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());

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
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);

        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorDown();

        assertEquals(buffer.getInsertionPointLine(), 1);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorDownOutOfBounds() throws IOException {
        String firstLine = "this is a very long line, longer than the second";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);

        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        for (int i = 0; i < firstLine.length(); i++) {
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
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorDown();
        for (int i = 0; i < secondLine.length(); i++) {
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
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorDown();

        buffer.moveCursorUp();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorUpOutOfBounds() throws IOException {
        String firstLine = "this is a short line";
        String secondLine = "this is a very long line, longer than the first";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorDown();
        for (int i = 0; i < secondLine.length(); i++) {
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
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());

        buffer.moveCursorUp();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorLeftBasic() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorRight();

        buffer.moveCursorLeft();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorLeftAcross() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorDown();

        buffer.moveCursorLeft();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), firstLine.length());
    }

    @Test
    public void testMoveCursorLeftBeginning() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorLeft();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorRightBasic() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());

        buffer.moveCursorRight();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), 1);
    }

    @Test
    public void testMoveCursorRightAcross() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        for (int i = 0; i < firstLine.length(); i++) {
            buffer.moveCursorRight();
        }

        buffer.moveCursorRight();

        assertEquals(buffer.getInsertionPointLine(), 1);
        assertEquals(buffer.getInsertionPointCol(), 0);
    }

    @Test
    public void testMoveCursorRightEnd() throws IOException {
        String firstLine = "hello everyone";
        Debug.write(path2.toString(), firstLine);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        for (int i = 0; i < firstLine.length(); i++) {
            buffer.moveCursorRight();
        }
        buffer.moveCursorRight();

        assertEquals(buffer.getInsertionPointLine(), 0);
        assertEquals(buffer.getInsertionPointCol(), firstLine.length());
    }

    @Test
    public void testClosingClean() throws IOException {
        String firstLine = "hello everyone";
        Debug.write(path2.toString(), firstLine);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());

        int saveStatus = buffer.close();

        assertEquals(saveStatus, 0);
    }

    @Test
    public void testClosingDirty() throws IOException {
        String firstLine = "hello everyone";
        Debug.write(path2.toString(), firstLine);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.write("a".getBytes()[0]);
        int saveStatus = buffer.close();

        assertEquals(saveStatus, 1);
    }

    @Test
    public void testSaveChangedFile() throws IOException {
        String firstLine = "hello everyone";
        Debug.write(path2.toString(), firstLine);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.write("a".getBytes()[0]);

        buffer.save();

        FileHolder fh = new FileHolder(path2.toString(), System.lineSeparator().getBytes());
        assertArrayEquals(fh.getContent(), "ahello everyone".getBytes());
    }

    @Test
    public void testWriteByte() throws IOException {
        String firstLine = "hello everyone";
        Debug.write(path2.toString(), firstLine);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorRight();
        buffer.write("a".getBytes()[0]);

        assertArrayEquals(FileAnalyserUtil.toArray(buffer.getByteContent()), "haello everyone".getBytes());
    }

    @Test
    public void testDirtyAfterChange() throws IOException {
        String firstLine = "hello everyone";
        Debug.write(path2.toString(), firstLine);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.write("a".getBytes()[0]);

        boolean dirtyStatus = buffer.getDirty();

        assertTrue(dirtyStatus);
    }

    @Test
    public void testDirtyClean() throws IOException {
        String firstLine = "hello everyone";
        Debug.write(path2.toString(), firstLine);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());

        boolean dirtyStatus = buffer.getDirty();

        assertFalse(dirtyStatus);
    }

    @Test
    public void testGetLines() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);

        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());

        ArrayList<ArrayList<Byte>> lines = buffer.getLines();

        assertEquals(lines, getContentLines(textToWrite.getBytes(), System.lineSeparator().getBytes()));
    }

    @Test
    public void testCharDeletionSynchronization() throws IOException {
        String firstLine = "hello";
        Debug.write(path2.toString(), firstLine);
        BufferCursorContext bufctx = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        BufferCursorContext bufctx2 = new BufferCursorContext(bufctx);
        for(int i = 0; i<firstLine.length(); i++){
            bufctx.moveCursorRight();
            bufctx2.moveCursorRight();
        }

        bufctx.deleteCharacter();
        bufctx.deleteCharacter();
        bufctx.deleteCharacter();

        assertEquals(firstLine.length()-3, bufctx2.getInsertionPointCol());
    }

    @Test
    public void testDeletionInsertionSynchronization() throws IOException {
        String firstLine = "aa";
        String secondLine = "bbb";
        String textToWrite = firstLine + System.lineSeparator()+ secondLine;
        Debug.write(path2.toString(), textToWrite);

        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorDown();
        buffer.moveCursorRight();
        buffer.moveCursorRight();
        buffer.moveCursorRight();

        BufferCursorContext buffer2 = new BufferCursorContext(buffer);
        buffer2.moveCursorLeft();
        buffer2.moveCursorLeft();
        buffer2.moveCursorLeft();
        buffer2.deleteCharacter();

        //When the line you are on gets merged with the previous one, your cursor jumps to the end of the new merged line.
        assertEquals(5, buffer.getInsertionPointCol());
        assertEquals(0, buffer.getInsertionPointLine());
    }

    @Test
    public void testEnterInsertionSynchronization() throws IOException {
        String firstLine = "aaabbb";
        String secondLine = "ccc";
        Debug.write(path2.toString(), firstLine + System.lineSeparator() + secondLine);
        BufferCursorContext bufctx = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        BufferCursorContext bufctx2 = new BufferCursorContext(bufctx);
        for(int i = 0; i<3; i++){
            bufctx.moveCursorRight();
            bufctx2.moveCursorRight();
        }
        bufctx2.moveCursorDown();

        bufctx.enterSeparator();

        assertEquals(2, bufctx2.getInsertionPointLine());
    }

    @Test
    public void testShallowCopyConstructor() throws IOException {
        String firstLine = "hello everyone";
        String secondLine = "i use termios daily";
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
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
        String textToWrite = firstLine + System.lineSeparator() + secondLine;
        Debug.write(path2.toString(), textToWrite);
        BufferCursorContext buffer = new BufferCursorContext(path2.toString(), System.lineSeparator().getBytes());
        buffer.moveCursorRight();

        BufferCursorContext buffer2 = buffer.deepClone();

        assertEquals(buffer.getFileBuffer(), buffer2.getFileBuffer());
        assertEquals(buffer.getInsertionPointCol(), buffer2.getInsertionPointCol());
        assertEquals(buffer.getInsertionPointLine(), buffer2.getInsertionPointLine());
    }
}
