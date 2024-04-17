package snake;

import snake.fruits.Banana;
import snake.fruits.Apple;
import snake.fruits.Fruit;
import snake.fruits.MogFruit;
import ui.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
    final int MAX_FRUITS = 3, STARVE_COUNTER = 20, WIN_LENGTH = 100, MILLISECOND_THRESHOLD = 1000;
    private int score = 0, delay = 0, gameState = 0, starver = 0, maxX, maxY, currentWait = 0;

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
     * Idles the game
     */
    public void idle() {
        currentWait++;
        if(currentWait + delay >= MILLISECOND_THRESHOLD) {
            tick();
            currentWait = 0;
        }
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

        // 2. Test if any of the fruits' positions match with the snake head.
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
            SnakeHead.log("Position : " + snake.getEnd().getPrint() + " invaliddssdf...?");
            gameState = -1;
        }

        // 5. Checks if the maximum length has been reached.
        if(snake.getLength() == WIN_LENGTH) gameState = 1;
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
     */
    public void move(MoveDirection dir) {
        this.snake.move(dir);
    }

    /**
     * Retrieves the list of positions
     * @return list of the available fruits
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
        int newMaxX = (int) newPlayfield.width;
        int newMaxY = (int) newPlayfield.height;
        int startX = (int) newPlayfield.startX;
        int startY = (int) newPlayfield.startY;

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
            while(isPosInvalid(position)) {
                position = generatePosition();
            }

            // We check which fruit to add
            if(test < 3) this.fruits.add(new Apple(position));
            else if(test >= 3 && test < 7) this.fruits.add(new MogFruit(position));
            else this.fruits.add(new Banana(position));
        }

    }

    /**
     * Generates a new position for the fruit
     * @return a {@link Pos} object
     */
    private Pos generatePosition() {
        Random rand = new Random();
        return new Pos(rand.nextInt(maxX), rand.nextInt(maxY));
    }

    @Override
    public SnakeGame clone() {
        SnakeGame game = new SnakeGame(5, this.maxX, this.maxY);
        game.snake = this.snake.clone();
        game.fruits = this.fruits.stream().map((fruit) -> fruit.clone()).toList();
        game.score = this.score;
        game.starver = this.starver;
        game.delay = this.delay;
        game.gameState = this.gameState;
        return game;
    }

}
