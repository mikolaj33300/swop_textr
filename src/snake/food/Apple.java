package snake.food;

import util.Pos;

public class Apple extends Food {

    private static final String CHARACTER = "A";

    public Apple(Pos pos) {
        super(CHARACTER, 1, 150, pos);
    }

}
