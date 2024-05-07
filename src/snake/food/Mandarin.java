package snake.food;

import util.Pos;

public class Mandarin extends Food {

    /**
     * Constructor for Mandarin
     * Mandarin is visualised with the character M, has growAmount 30, and gives score 300
     * @param pos Banana position, the reference to this objet will be lost
     */
    public Mandarin(Pos pos) {super("M", 30, 300, pos);
    }

}
