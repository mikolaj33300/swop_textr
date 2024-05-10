package snake.food;

import util.Pos;

public class Banana extends Food {

    /**
     * Constructor for Banana
     * Banana is visualised with the character B, has growAmount 2, and gives score 100
     * @param pos Banana position, the reference to this objet will be lost
     */
    public Banana(Pos pos) {super("B", 2, 100, pos);}
}
