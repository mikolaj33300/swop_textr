package files;

import directory.directorytree.FileCreator;
import directory.directorytree.JsonEntry;
import inputhandler.FileBufferInputHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.json.JsonUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class JsonFileHolderTest {

    @TempDir
    Path path1, path2;

    String content1, content2;
    FileBuffer buffer1, buffer2;
    JsonEntry entry1, entry2, entryDocuments, entryJsonInSring, ass2, ass3;

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
        path1 = path1.resolve("test1.txt");
        Files.write(path1, content1.getBytes());
        content2 = """
                {
                  "antijsonfile":
                    "SWOP": {
                      "assignment_it2.txt": "This is the assignment for iteration 2.",
                      "assignment_it3.txt": "This is the assignment for iteration 3."
                    },
                    "json_in_string.json": "{\\r\\n  \\"foo\\": \\"bar\\"\\r\\n}"
                  }
                }
                """;
        path2 = path2.resolve("test2.txt");
        Files.write(path2, content2.getBytes());

        buffer1 = new FileBuffer(path1.toString(), "\n".getBytes());
        buffer2 = new FileBuffer(path2.toString(), "\n".getBytes());

        entry1 = (JsonEntry) JsonUtil.parseDirectory(buffer1, null, null);
        entry2 = (JsonEntry) JsonUtil.parseDirectory(buffer2, null, null);

        entryDocuments = entry1.getEntries().get(0);
        entryJsonInSring = entryDocuments.getEntries().get(1);
        ass2 = entryDocuments.getEntries().getFirst().getEntries().get(0);
        ass3 = entryDocuments.getEntries().getFirst().getEntries().get(1);
    }

    @Test
    public void CreateJsonFileHolder_FromFileEntry_CorrectContents() throws IOException {
        assertTrue(
            FileHolder.areContentsEqual(
                    new JsonFileHolder(buffer1, entryJsonInSring.getName()).getContent()
                    ,
                    "{\\r\\n  \\\"foo\\\": \\\"bar\\\"\\r\\n}".getBytes()
            )
        );
    }

    //TODO: IDK remove this test? i only did this quickly to check a bug with the constructor
    @Test
    public void CreateJsonFileHolder_FromFileEntry_CorrectContents2() throws IOException {
        assertTrue(
                FileHolder.areContentsEqual(
                        new JsonFileHolder(buffer1, ass2.getName()).getContent()
                        ,
                        "This is the assignment for iteration 2.".getBytes()
                )
        );
        FileBufferInputHandler bufToTest = new FileBufferInputHandler(new BufferCursorContext(new EditableFileBuffer(new JsonFileHolder(buffer1, ass2.getName())), 0, 0));
        assertTrue(
                new FileBuffer(new JsonFileHolder(buffer1, ass2.getName())).getLines().size() ==1
        );
    }

    @Test
    public void CreateJsonFileHolder_FromDirectoryEntry_NullContents() {
        assertNotNull(
          entryDocuments.selectEntry()
        );
    }

    @Test
    public void LastBytesMiddleEntry_EqualsColumnsAndComma() {
        assertTrue(
                FileHolder.areContentsEqual(
                    (new JsonFileHolder(buffer1, ass2.getName())).getLastBytes(),
                    "\",".getBytes()
                )
        );
    }

    @Test
    public void LastBytesLastEntry_EqualsColumnsOnly() {
        assertTrue(
                FileHolder.areContentsEqual(
                    (new JsonFileHolder(buffer1, ass3.getName())).getLastBytes(),
                    "\"".getBytes()
                )
        );
    }

    @Test
    public void SaveOnFileHolder_OverwritesCorrectPart() throws IOException {
        JsonFileHolder ass2Holder = ((new JsonFileHolder(buffer1, ass2.getName())));
        // save writes to buffer
        ass2Holder.save("i like optee, im optee beginner ; borys is optee expert ; tom is optee god "
                        .getBytes()
                );
        // getContent reads from buffer.getLines()
        assertTrue(
            FileHolder.areContentsEqual(
                    ass2Holder.getContent()
                    ,
                    "i like optee, im optee beginner ; borys is optee expert ; tom is optee god ".getBytes()
            )
        );
    }

}
