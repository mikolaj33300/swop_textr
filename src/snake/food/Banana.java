package snake.food;

import util.Pos;

public class Banana extends Food {

    private static final String CHARACTER = "B";

    public Banana(Pos pos) {
        super(CHARACTER, 2, 100, pos);
    }

}
