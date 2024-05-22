package controller;

import ioadapter.SwingTerminalAdapter;
import ioadapter.VirtualTestingTermiosAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisplayFacadeTest {
    @TempDir
    Path path1;

    @BeforeEach
    public void setVariables() throws IOException {
        path1 = path1.resolve("test");
        Files.write(path1, "mister".getBytes());
    }

    @Test
    public void testDisplaysSwing() throws IOException {

        VirtualTestingTermiosAdapter adapter = new VirtualTestingTermiosAdapter(1200, 100, new ArrayList<>());

        SwingTerminalAdapter newSwing = new SwingTerminalAdapter();
        ControllerFacade newController = new ControllerFacade(new String[] {"--lf", path1.toString()}, adapter);
        newController.openNewSwingFromActiveWindow();
        while(true);
        //assertEquals(newSwing.getContentBuffer()[0][0], 'm');
    }

}
