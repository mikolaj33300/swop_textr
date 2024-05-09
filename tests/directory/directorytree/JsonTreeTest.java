package directory.directorytree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.json.JsonUtil;
import util.json.SimpleJsonParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class JsonTreeTest {

    @TempDir
    Path path1, path2;

    String content1, content2;

    FileSystemEntry root;

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
        path1 = path1.resolve("test1.txt");
        Files.write(path1, content1.getBytes());
        root = JsonUtil.parseDirectory(content1, path1.toString());
    }

    @Test
    public void AmountChildrenParser_FileSystemEntryRoot_Same() {
        assertEquals(
                SimpleJsonParser.parseObject(content1).getChildren().size(),
                JsonUtil.parseDirectory(content1, path1.toString()).getChildrenPaths().size()
        );
    }

    @Test
    public void RootHasParentNull() {
        assertNull(root.getParent());
    }

    @Test
    public void ChildrenListedCorrectly() {
        assertEquals(root.getDirectChildrenNames().getFirst(), "Documents");
    }

}
