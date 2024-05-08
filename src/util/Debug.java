package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Debug {

  /**
   * print a string
   * @param s string to be printed
   */
    public static void print(String s) {
        System.out.println(s);
    }

    public static class Print<T> {

        private T[] type;

        public Print(T[] type) {
            this.type = type;
        }

	/**
	 * print an array
	 */
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

}
