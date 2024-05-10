package directory.directorytree;

import files.FileBuffer;
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

    JsonEntry root;

    FileBuffer buffer1;

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
        buffer1 = new FileBuffer(path1.toString(), System.lineSeparator().getBytes());
        root = (JsonEntry) JsonUtil.parseDirectory(buffer1);
    }

    @Test
    public void AmountChildrenParser_FileSystemEntryRoot_Same() {
        assertEquals(
                SimpleJsonParser.parseObject(content1).getChildren().size(),
                JsonUtil.parseDirectory(buffer1).getChildren().size()
        );
    }

    @Test
    public void RootHasParentNull() {
        assertNull(root.getParent());
    }

    @Test
    public void NonRootHasNonNullParent_True() {
        assertNotNull(root.getEntries().getFirst().getParent());
    }

    @Test
    public void ChildrenListedCorrectly() {
        assertEquals(root.getDirectChildrenNames().getFirst(), "Documents");
    }

    @Test
    public void getLocation_Dir_Null() {
        assertNull(root.getEntries().getFirst().getLocation());
    }

    @Test
    public void getLocation_File_NotNull() {
        assertNull(root.getEntries().getFirst().getEntries().getFirst().getLocation());
    }

    @Test
    public void IsRootDirectory_True() {
        assertTrue(root.isDirectory());
    }

    @Test
    public void isChildDirectory_False() {
        assertFalse(root.getEntries().get(0).getEntries().get(1).isDirectory());
    }

    @Test
    public void isSubDirectory_Directory_True() {
        assertTrue(root.getEntries().get(0).isDirectory());
    }

    @Test
    public void DirectoryDoesntGenerateFileholder_True() {
        assertNull(root.getEntries().get(0).createFile(new FileCreator()));
    }

    @Test
    public void FileEntryGenerates_FileHolder_True() {
        System.out.println("Child:" + root.getEntries().get(0).getEntries().get(1).createFile(new FileCreator()));
        assertNotNull(root.getEntries().get(0).getEntries().get(1).createFile(new FileCreator()));
    }


}