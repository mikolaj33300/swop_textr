package snake;

import snake.fruits.Banana;
import snake.fruits.Apple;
import snake.fruits.Fruit;
import snake.fruits.MogFruit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Tasks:
 * - Control the flow of the game: tick
 * - Enforce game rules: positioning
 * - Hold information of the game: fruits, score, delay
 * - Make interaction possible: move the snake
 */
public class SnakeGame {

    /**
     * The head of the snake. All movement & positioning is in this object
     */
    private SnakeHead snake;

    /**
     * The fruits on the game board
     */
    List<Fruit> fruits = new ArrayList<>();

    /**
     * Settings of the game.
     */
    final int MAX_FRUITS = 3, maxX, maxY;
    private int score = 0, delay = 0, gameState = 0;

    /**
     * Represents the max X and Y coordinates of the snake game width. Used to generate fruits inside map bounds
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

        snake.tick();

        // Test if any of the fruits' positions match with the snake head.
        if(this.fruits.stream().anyMatch((x) -> x.getPosition().equals(snake.getEnd()))) {
            // We can assume that the amount of matches is 1, because the generation restricts two fruits on the same position
            Fruit collidedFruit = fruits.stream()
                    .filter((fruit) -> fruit.getPosition().equals(snake.getEnd()))
                    .toList()
                    .get(0);
            this.fruits.remove(collidedFruit);
            this.snake.grow(collidedFruit.getGrowAmount());
            this.score += collidedFruit.getScore();
            this.delay += collidedFruit.millisecondDecrease();
            initializeFruits();
        } else this.score += 1;

        // Checks for invalid positions
        if(Arrays.stream(this.snake.getSegments()).anyMatch((segment) -> Pos.isBetween1D(segment.getStart(), segment.getEnd(), snake.getEnd()))
           || this.snake.getEnd().x() <= 0 || this.snake.getEnd().x() >= maxX
           || this.snake.getEnd().y() <= 0 || this.snake.getEnd().y() >= maxY) {
            gameState = -1;
            SnakeHead.log(">>>>>>Hello: " + Arrays.stream(this.snake.getSegments()).anyMatch((segment) -> Pos.isBetween1D(segment.getStart(), segment.getEnd(), snake.getEnd())));
        }

        // Checks if the maximum length has been reached.
        if(snake.getLength() == maxX * maxY) gameState = 1;

    }

    /**
     * Determines if the game is won.
     * @return
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
     */
    public void move(MoveDirection dir) {
        this.snake.move(dir);
    }

    /**
     * Retrieves the list of positions
     * @return
     */
    public List<Fruit> getFruits() {
        // Map each Fruit to its position cloned.
        return this.fruits.stream().map(Fruit::clone).toList();
    }

    /**
     * Returns the amount of delay that has been added by eating food items.
     */
    public int getRemovedDelay() {
        return this.delay;
    }

    /**
     * Fills the array {@link SnakeGame#fruits} with {@link Fruit} instances
     */
    private void initializeFruits() {
        // Loop as long as the list does not contain the max amount of fruits
        while(this.fruits.size() < MAX_FRUITS) {
            // We create a random variable to create random fruits
            Random rand = new Random();
            int test = rand.nextInt(10);

            // We generate a position & retry each time the generated position already has a fruit
            Pos position = generatePosition();
            int i = 100;
            while(isPosInvalid(position)) {
                if(i < 0) SnakeHead.log("--Looping pos");
                position = generatePosition();
                i--;
            }

            // We check which fruit to add
            if(test < 3) this.fruits.add(new Apple(position));
            else if(test >= 3 && test < 7) this.fruits.add(new MogFruit(position));
            else this.fruits.add(new Banana(position));
        }

        SnakeHead.log("Fruit at " +         fruits.stream()
                .map((x) -> x.getPosition().getPrint())
                .collect(Collectors.joining()));
    }

    /**
     * Generates a new position for the fruit
     * @return a {@link Pos} object
     */
    private Pos generatePosition() {
        Random rand = new Random();
        return new Pos(rand.nextInt(maxX), rand.nextInt(maxY));
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
        // Or the position is already occupied by one of the fruits
                this.fruits.stream().anyMatch((fruit) -> fruit.getPosition().equals(pos))
                ;
    }

    /**
     * Returns the score of the game.
     * @return
     */
    int getScore() {
        return this.score;
    }

}
