package layouttree;

import files.FileBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LayoutTest {

    String path1;
    String path2;
    String path3;
    String path4;
    FileBuffer fb1;
    FileBuffer fb2;
    FileBuffer fb3;
    FileBuffer fb4;
    LayoutLeaf l1;
    LayoutLeaf l1a;
    LayoutLeaf l1p;
    LayoutLeaf l2;
    LayoutLeaf l2a;
    LayoutLeaf l2p;
    LayoutLeaf l3;
    LayoutLeaf l4;

    @BeforeEach
    void setUp() throws IOException {
        path1 = "testresources/test.txt";
        path2 = "testresources/test2.txt";
        path3 = "testresources/test3.txt";
        path4 = "testresources/test4.txt";
    }
}


















