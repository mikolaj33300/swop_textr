package snake.food;

import util.Pos;

public class Food {

    /**
     * The amount of SnakeSegments the Snake gains upon eating this Food
     */
    private final int growAmount;

    /**
     * The amount of score given to the SnakeGame upon eating this Food
     */
    private final int score;

    /**
     * The character that represents this Food in the SnakeGame
     */
    private final String character;

    /**
     * The position of this Food in the SnakeGame
     */
    private final Pos pos;

    /**
     * Constructor for Food
     * @param pos Food position, the reference to this object will be lost
     * @param growAmount the amount the snake grows when eating the Food
     * @param character the character representing the Food
     */
    public Food(String character, int growAmount, int score, Pos pos) {
        this.character = character;
        this.growAmount = growAmount;
        this.score = score;
        this.pos = pos.clone();
    }

    /**
     * Returns the score of this Food
     * @return score, an int
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the amount the snake should grow after eating this fruit.
     * @return growAmount, an int
     */
    public int getGrowAmount() {
        return this.growAmount;
    }

    /**
     * Returns the position of this Food
     * @return {@link Pos} of the Food
     */
    public Pos getPosition() {
        return this.pos.clone();
    }

    /**
     * Returns the visual representation of this Food.
     * @return character, an int
     */
    public String getCharacter() {
        return this.character;
    }

    /**
     * Returns the amount of milliseconds are subtracted from the delay.
     * @return a float
     */
    public float millisecondDecrease() {
        return 0.01f;
    }

    /**
     * Clones this Food to remove the reference to the originial object
     * The reference to the pos field will also be lost
     * @return Food, a clone of this object
     */

    @Override
    public Food clone() {
        return new Food(this.character, this.growAmount, this.score, this.pos.clone());
    }

    /**
     * Checks if an object is equal to this Food
     * @param o the object to compare this to
     * @return boolean if the object o equals this
     * The objects are equals if they have the same pos, character, score, and growAmount content-wise
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof Food f)
            return f.pos.equals(this.pos) && f.character.equals(this.character) && f.score == this.score && f.growAmount == this.growAmount;
        return false;
    }

}
