package snake.fruits;

import snake.Pos;

public class Banana extends Fruit {

    private static final String CHARACTER = "b";

    /**
     * constructor for banana fruit
     * @param pos Fruit position
     */
    public Banana(Pos pos) {
        super(CHARACTER, 2, 50, pos);
    }

}
