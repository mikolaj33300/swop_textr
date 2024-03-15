package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Debug {

    public static void print(String s) {
        System.out.println(s);
    }

    public static class Print<T> {

        private T[] type;

        public Print(T[] type) {
            this.type = type;
        }

        public void print() {
            String s = "[";
            for(int i = 0; i < type.length; i++) {
                s += type[i];
                if(i < type.length-1) s += ", ";
            }
            s += "]";
            Debug.print(s);
        }

    }

    /**
     * Helper method that writes given text into the file at given path
     */
    public static void write(String path, String text) {
        try {
            // Overwrite file test.txt
            FileWriter writer = new FileWriter(new File(path));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
