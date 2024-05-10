package util.json;

import directory.directorytree.JsonEntry;
import files.FileAnalyserUtil;
import files.FileBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class TestJsonUtil {

    @TempDir
    Path path1, path2, path3;

    String content1, content2, content3;
    FileBuffer buffer1, buffer2, buffer3;


    @BeforeEach
    public void init() throws IOException {
        content1 = """
				{
				  "Documents": {
				    "SWOP": {
				      "assignment_it2.txt": "This is the assignment for iteration 2.",
				      "assignment_it3.txt": "This is the assignment for iteration 3."
				    },
				    "json_in_string.json": "{\\r\\n  \\"foo\\": \\"bar\\"\\r\\n}"
				  }
				}""";
        content2 = """
				{
				  "Mister Tee": "i love optee and i time it for interrupt for attestation",
				  "Dnny Hghs": "i am the creator of mister tee, my assistant is tve"
				}""";
        content3 = """
				{
				  "Mister Tee: "i love optee and i time it for interrupt for attestation",
				  "Dnny Hghs": "i am the creator of mister tee, my assistant is tve"
				}""";
        path1 = path1.resolve("test1.txt");
        Files.write(path1, content1.getBytes());
        path2 = path2.resolve("test2.txt");
        Files.write(path2, content2.getBytes());
        path3 = path3.resolve("test3.txt");
        Files.write(path3, content3.getBytes());

        buffer1 = new FileBuffer(path1.toString(), FileAnalyserUtil.getLineSeparator(content1.getBytes()));
        buffer2 = new FileBuffer(path2.toString(), FileAnalyserUtil.getLineSeparator(content1.getBytes()));
        buffer3 = new FileBuffer(path3.toString(), FileAnalyserUtil.getLineSeparator(content1.getBytes()));
    }

    @Test
    public void testFindNonNestedLocation() {
        assertEquals(JsonUtil.getTextLocationFor(buffer2, "Mister Tee"), new TextLocation(1,16));
        assertEquals(JsonUtil.getTextLocationFor(buffer2, "Dnny Hghs"), new TextLocation(2,15));
    }

    @Test
    public void testFindNestedLocation() {
        assertEquals(JsonUtil.getTextLocationFor(buffer1, "assignment_it2.txt"), new TextLocation(3,28));
        assertEquals(JsonUtil.getTextLocationFor(buffer1, "json_in_string.json"), new TextLocation(6,27));
    }

    @Test
    public void testFindDirectoryLocation_IsNull() {
        assertNull(JsonUtil.getTextLocationFor(buffer1, "Documents"));
    }

    @Test
    public void testFindLocation_NonExisting() {
        assertNull(JsonUtil.getTextLocationFor(buffer1, "mister tee is revolutionary"));
    }

    @Test
    public void testErrorLocation() {
        assertEquals(JsonUtil.getErrorLocation(content3), new TextLocation(1,16));
    }

    @Test
    public void testParseInvalidFile_ResultsNull() {
        assertNull(JsonUtil.parseDirectory(buffer3));
    }

    @Test
    public void testParseValidFile_ResultsNonNull() {
        assertNotNull(JsonUtil.parseDirectory(buffer1));
    }

}
