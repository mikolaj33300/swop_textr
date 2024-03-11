package util;

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

}
