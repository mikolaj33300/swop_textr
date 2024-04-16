package snake.fruits;

import snake.Pos;

public class Fruit {

    private final int growAmount, score;
    private final String character;
    private final Pos pos;

    public Fruit(String character, int growAmount, int score, Pos pos) {
        this.character = character;
        this.growAmount = growAmount;
        this.score = score;
        this.pos = pos;
    }

    /**
     * Returns the score
     * @return an integer higher than 0
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the amount the snake should grow after eating this fruit.
     * @return an integer higher than 0
     */
    public int getGrowAmount() {
        return this.growAmount;
    }

    /**
     * Returns the position of the snake
     * @return {@link Pos} of the snake
     */
    public Pos getPosition() {
        return this.pos.clone();
    }

    /**
     * Returns the visual representation of this fruit.
     * @return a string
     */
    public String getCharacter() {
        return this.character;
    }

    /**
     * Returns the amount of milliseconds are subtracted from the delay.
     * @return
     */
    public int millisecondDecrease() {
        return 10;
    }

    @Override
    public Fruit clone() {
        return new Fruit(new String(this.character), growAmount, score, this.pos.clone());
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Fruit f)
            return f.pos.equals(this.pos) && f.character.equals(this.character) && f.score == this.score && f.growAmount == this.growAmount;
        return false;
    }

}
