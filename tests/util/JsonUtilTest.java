package util;

import files.JsonFileHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.json.JsonUtil;
import util.json.SimpleJsonObject;
import util.json.SimpleJsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class JsonUtilTest {

    @TempDir
    Path path1, path2;

    String content1, content2;

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
				  "Documents":
				    "SWOP": {
				      "assignment_it2.txt": "This is the assignment for iteration 2.",
				      "assignment_it3.txt": "This is the assignment for iteration 3."
				    },
				    "json_in_string.json": "{\\r\\n  \\"foo\\": \\"bar\\"\\r\\n}"
				  }
				}""";
    }

    @Test
    public void TestParse_Successful_ChildrenDetected() {
        SimpleJsonObject o = SimpleJsonParser.parseObject(content1);
        assertEquals(o.getChildren().size(), 1);
    }

    @Test
    public void TestParse_Successful_ErrorLocationNull() {
        assertNull(SimpleJsonParser.getErrorLocation(content1));
    }

    @Test
    public void TestParse_Failed() {
        assertNotNull(SimpleJsonParser.getErrorLocation(content2));
    }

}
