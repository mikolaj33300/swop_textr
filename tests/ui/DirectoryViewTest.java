package ui;

import ioadapter.VirtualTestingTermiosAdapter;
import directory.Directory;
import directory.directorytree.FileEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectoryViewTest {


    @TempDir
    Path root, a, b, c, d;

    Directory dir;
    DirectoryView view;

    @BeforeEach
    public void init() throws IOException {

        a = root.resolve("test1.txt");
        Files.write(a, "termios is life ;\ntermios is also very useful for terminal apps".getBytes());
        b = root.resolve("test2.txt");
        Files.write(b, "if kaas is\n not a mister\n ; no one is".getBytes());
        c = root.resolve("test3.txt");
        Files.write(c, "termios".getBytes());
        d = root.resolve("test4.txt");
        Files.write(d, "hallo kaas i am your loyal student\n i use termios daily".getBytes());

        dir = new Directory(new FileEntry(root.toAbsolutePath().toString(), null, null, null));

        view = new DirectoryView(new VirtualTestingTermiosAdapter(100, 5, new ArrayList<>()), dir);

    }

    @Test
    public void calculateBeginning_OffScreen_ReturnsValid() throws IOException {
        view = new DirectoryView(new VirtualTestingTermiosAdapter(100, 5, new ArrayList<>()), dir);
        dir.increaseFocused(); // root 2
        dir.increaseFocused(); // a    4
        dir.increaseFocused(); // b    6
        dir.increaseFocused(); // c    8
        //assertEquals(view.calculateBeginning(), 3);
    }

}
