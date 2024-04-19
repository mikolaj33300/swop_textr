package snake;

import snake.food.Banana;
import snake.food.Apple;
import snake.food.Food;
import snake.food.Mandarin;
import util.MoveDirection;
import util.Pos;
import util.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Tasks:
 * - Control the flow of the game: tick
 * - Enforce game rules: positioning
 * - Hold information of the game: foods, score, delay
 * - Make interaction possible: move the snake
 */
public class SnakeGame {

    /**
     * The head of the snake. All movement & positioning is in this object
     */
    private SnakeHead snake;

    /**
     * The foods on the game board
     */
    List<Food> foods = new ArrayList<>();

    /**
     * Settings of the game.
     */
    final int MAX_FRUITS = 3, STARVE_COUNTER = 20, WIN_LENGTH = 20, MILLISECOND_THRESHOLD = 1000;
    private int score = 0, gameState = 0, starver = 0, maxX, maxY, currentWait = 0;
    private float delay = 0f;

    /**
     * Represents the max X and Y coordinates of the snake game width. Used to generate foods inside map bounds
     */
    public SnakeGame(int length, int maxX, int maxY) {
        this.snake = new SnakeHead(length, maxX/2, maxY/2);
        this.maxX = maxX;
        this.maxY = maxY;
        initializeFruits();
    }

    /**
     * Returns the head of the snake.
     * @return {@link SnakeHead} object
     */
    public SnakeHead getSnake() {
        return this.snake;
    }

    /**
     * Ticks the game map:
     * -> update snake positions
     * -> check for invalid positions
     * -> eat / generate fruit
     */
    public void tick() {

        // 1. Tick the game
        snake.tick();

        // 2. Test if any of the foods' positions match with the snake head.
        if(this.foods.stream().anyMatch((x) -> x.getPosition().equals(snake.getEnd()))) {
            // We can assume that the amount of matches is 1, because the generation restricts two foods on the same position
            Food collidedFood = foods.stream()
                    .filter((fruit) -> fruit.getPosition().equals(snake.getEnd()))
                    .toList()
                    .get(0);
            this.foods.remove(collidedFood);
            this.snake.grow(collidedFood.getGrowAmount());
            this.score += collidedFood.getScore();
            this.delay = delay > 0.9f ? 0.9f : delay + collidedFood.millisecondDecrease();
            this.starver = 0;
            initializeFruits();
        } else {
            this.score += 1;
            this.starver += 1;
        }

        // 3. Starve the snake
        if(this.starver >= this.STARVE_COUNTER) {
            this.starver = 0;
            this.snake.grow(-1);
        }

        // 4. Check for invalid positions
        if(Arrays.stream(this.snake.getSegments()).anyMatch((segment) -> Pos.isBetween1D(segment.getStart(), segment.getEnd(), snake.getEnd()))
           || this.snake.getEnd().x()+1 < 0 || this.snake.getEnd().x() >= maxX-1
           || this.snake.getEnd().y()+1 < 0 || this.snake.getEnd().y() >= maxY-1) {
            // maxX-1 because maxX IS the border of the game and thus invalid
            // snake positions are
            gameState = -1;
        }

        // 5. Checks if the maximum length has been reached.
        if(snake.getLength() >= WIN_LENGTH) gameState = 1;
        else if(snake.getLength() <= 0) {
            gameState = -1;
        }

    }

    /**
     * Determines if the game is won.
     * @return boolean determining if the game is won
     */
    public boolean isWon() {
        return gameState == 1;
    }

    /**
     * Determines if the game can still continue: not lost & not won.
     * @return boolean
     */
    public boolean canContinue() {
        return gameState == 0;
    }

    /**
     * Handles user input
     * @param dir the input direction
     */
    public void move(MoveDirection dir) {
        this.snake.move(dir);
    }

    /**
     * Retrieves the list of positions
     * @return list of the available foods
     */
    public List<Food> getFruits() {
        // Map each Food to its position cloned.
        return this.foods.stream().map(Food::clone).toList();
    }

    /**
     * Returns the amount of delay that has been added by eating food items.
     * @return int
     */
    public float getRemovedDelay() {
        return this.delay;
    }

    /**
     * Returns the score of the game.
     * @return the score of the game
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Modifies the {@link SnakeGame#maxX} and {@link SnakeGame#maxY} fields. And scales
     * all game elements to the new field
     * @param newPlayfield the rectangle of the new playing field
     */
    public void modifyPlayfield(Rectangle newPlayfield) {
        float newMaxX = (float) newPlayfield.width;
        float newMaxY = (float) newPlayfield.height;

        // We scale the previous width to the new width:
        float scaledX = newMaxX / (float) this.maxX;
        float scaledY = newMaxY / (float) this.maxY;

        // We can now multiply the snake positions with these scales
        this.snake.scale(scaledX, scaledY);
        this.maxX = (int) newMaxX;
        this.maxY = (int) newMaxY;
        this.foods.clear();
        initializeFruits();

    }

    /**
     * Given a certain position, determines if that position is valid to spawn a fruit
     * @return boolean determining if a position is valid for usage on the board
     */
    boolean isPosInvalid(Pos pos) {
        // If the position overlaps with one of the segments
        return Arrays.stream(this.snake.getSegments())
                .anyMatch((segment) -> Pos.isBetween1D(segment.getStart(), segment.getEnd(), pos))
                ||
                // Or the position is already occupied by one of the foods
                this.foods.stream().anyMatch((fruit) -> fruit.getPosition().equals(pos))
                ;
    }

    /**
     * Fills the array {@link SnakeGame#foods} with {@link Food} instances
     */
    private void initializeFruits() {
        // Loop as long as the list does not contain the max amount of foods
        while(this.foods.size() < MAX_FRUITS) {
            // We create a random variable to create random foods
            Random rand = new Random();
            int test = rand.nextInt(10);

            // We generate a position & retry each time the generated position already has a fruit
            Pos position = generatePosition();
            while(isPosInvalid(position)) {
                position = generatePosition();
            }

            // We check which fruit to add
            if(test < 3) this.foods.add(new Apple(position));
            else if(test >= 3 && test < 7) this.foods.add(new Mandarin(position));
            else this.foods.add(new Banana(position));
        }

    }

    /**
     * Returns the millisecond threshold between each tick on idle
     * @return millisecond threshold
     */
    public int getMillisecondThreshold() {
        return this.MILLISECOND_THRESHOLD;
    }


    /**
     * Resets the game
     */
    public void reset() {
        this.snake = new SnakeHead(5, maxX/2, maxY/2);
        this.maxX = maxX;
        this.maxY = maxY;
        this.foods.clear();
        initializeFruits();
        this.score = gameState = starver = currentWait = 0;
        delay = 0f;
    }

    /**
     * Generates a new position for the fruit
     * @return a {@link Pos} object
     */
    private Pos generatePosition() {
        Random rand = new Random();
        return new Pos(rand.nextInt(maxX-1), rand.nextInt(maxY-1));
    }
}
