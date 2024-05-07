package snake.food;

import util.Pos;

public class Apple extends Food {

    /**
     * Constructor for Apple
     * Apple is visualised with the character A, has growAmount 1, and gives score 150
     * @param pos Banana position, the reference to this objet will be lost
     */
    public Apple(Pos pos) {
        super("A", 1, 150, pos);
    }
}
