package snake.fruits;

import snake.Pos;

public class Banana extends Fruit {

    private static final String CHARACTER = "b";

    public Banana(Pos pos) {
        super(CHARACTER, 2, 50, pos);
    }

}