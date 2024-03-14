package core;

import org.junit.jupiter.api.*;

public class InspectBufferTest {

    Controller c;
    private final String root = "testresources/";
    private final String path1 = root + "test.txt";
    private final String path2 = root + "test2.txt";
    private final String path3 = root + "test3.txt";


    @Test
    public void testInspectBuffer(){
        Controller c1 = new Controller(new String[]{"testresources/test.txt", "testresources/test2.txt"});
    }

}
