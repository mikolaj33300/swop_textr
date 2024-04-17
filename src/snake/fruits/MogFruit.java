package snake.fruits;

import snake.Pos;

public class MogFruit extends Fruit {

    private static final String CHARACTER = "M";

    /**
     * constructor for MogFruit
     * @param p the position of the fruit
     */
    public MogFruit(Pos p) {
        super(CHARACTER, 30, 6969, p);
    }

}
