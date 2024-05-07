package files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFileHolderTest {

    @TempDir
    Path path1, path2;

    JsonFileHolder holder;
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
                }
                """;
        path1 = path1.resolve("test1.txt");
        Files.write(path1, content1.getBytes());
        content2 = """
                        content1 = ""\"
                                {
                                  "Documents": 
                                    "SWOP": {
                                      "assignment_it2.txt": "This is the assignment for iteration 2.",
                                      "assignment_it3.txt": "This is the assignment for iteration 3."
                                    },
                                    "json_in_string.json": "{\\\\r\\\\n  \\\\"foo\\\\": \\\\"bar\\\\"\\\\r\\\\n}"
                                  }
                                }
                                ""\";
                """;
        path2 = path2.resolve("test2.txt");
        Files.write(path2, content2.getBytes());
    }

    @Test
    public void FileHolder_ParseCorrect() {
        holder = new JsonFileHolder(path1.toString(), null, null);
    }

}
