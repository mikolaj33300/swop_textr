package util;

public class Pair<A,B> {

    /**
     * The first element in the pair
     */
    public final A a;

    /**
     * The second element in the pair
     */
    public final B b;

    /**
     * Constructor for a Pair of any type
     * @param a the first element of the pair
     * @param b the second element of the pair
     */
    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
};