package snake.fruits;

import snake.Pos;

public class Apple extends Fruit {

    private static final String CHARACTER = "a";

    public Apple(Pos pos) {
        super(CHARACTER, 1, 100, pos);
    }

}