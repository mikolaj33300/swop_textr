package snake;

import io.github.btj.termios.Terminal;
import snake.fruits.Fruit;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class SnakeView extends View {

    private SnakeGame game;

    // Note: we pass max width & max height of the view. So Snake works with relative coordinates
    // between [0, width] and [0, height]
    public SnakeView() {
        this.game = new SnakeGame(20, 80-2, 24-2);
    }

    public void tick() {
        SnakeHead.log("Ticking");
        if(game.canContinue())
            this.game.tick();
    }

    public void move(char code) throws IOException {
        switch (code) {
            // Up
            case 'A':
                this.game.move(MoveDirection.UP);
                break;
            // Down
            case 'B':
                this.game.move(MoveDirection.DOWN);
                break;
            // Right
            case 'C':
                this.game.move(MoveDirection.RIGHT);
                break;
            // Left
            case 'D':
                this.game.move(MoveDirection.LEFT);
                break;
        }
    }

    @Override
    public void render() throws IOException {

        super.setCorrectCoords();
        Snake[] segments = game.getSnake().getSegments();
        Snake head = game.getSnake();

        // Print border
        printLine(new Pos(this.startX+width, this.startY), new Pos(this.startX+width, this.startY+height), "I", false);
        printLine(new Pos(this.startX, this.startY+height), new Pos(this.startX+width, this.startY+height), "-", false);

        // Print score
        Terminal.printText(this.startY+height,this.startX + width/2, "Score: " + game.getScore());

        // Determine the skin of the snake
        String a = game.getSnake().getHeadString() + "x";
        a = game.getSnake().getHeadString() + "-hello_mister_i_mogged_snake";

        int skip = 0;
        skip = printLine(head.getEnd(), head.getStart(), Arrays.stream(a.split("")).skip(skip).collect(Collectors.joining()), false);

        for(int i = segments.length-1; i >= 0; i--) {
            String s = Arrays.stream(a.split("")).skip(skip).collect(Collectors.joining());
            if(s.equals("")) s = a.split("")[a.length()-1];
            skip += printLine(segments[i].getEnd(), segments[i].getStart(),
                    s, true);
        }


        List<Fruit> fruits = game.getFruits();
        for(int i = 0; i < fruits.size(); i++)
            Terminal.printText(
                    1+fruits.get(i).getPosition().y()+startY,
                    1+fruits.get(i).getPosition().x()+startX,
                    fruits.get(i).getCharacter());

        // Print lost/won
        if(!this.game.canContinue())
            if(this.game.isWon()) printBox(5, "You won", "Press enter to try again");
            else printBox(5, "You lost", "Press enter to try again");


    }

    @Override
    public void renderCursor() throws IOException {
        return;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    /**
     * Prints a line between the two points. Note, this method asserts that the positions share at least
     * one coordinate.
     * @param start the start position
     * @param end the end position
     * @param isEqual determines if the line may stop when start+i reaches end or before.
     * @return the amount of steps were taken to print this line.
     */
    private int printLine(Pos start, Pos end, String character, boolean isEqual) {
        int dX = -(start.x() - end.x());
        int dY = -(start.y() - end.y());
        int stepX = dX != 0 ? dX / Math.abs(dX) : 0;
        int stepY = dY != 0 ? dY / Math.abs(dY) : 0;

        for(int i = 0;
            i < Math.abs(dX) || (isEqual && i <= Math.abs(dX));
            i++) {
            Terminal.printText(1 + this.startY + start.y(), 1 + this.startX + start.x() + (i*stepX), getCharacter(character, i*stepX));
        }
        for(int i = 0;
            i < Math.abs(dY) || (isEqual && i <= Math.abs(dY));
            i++) {
            Terminal.printText(1 + this.startY + start.y() + (i*stepY), 1 + this.startX + start.x(), getCharacter(character, i*stepY));
        }

        return Math.max(Math.abs(dX), Math.abs(dY));
    }

    /**
     * Gets the character at offset 'step'. Used to render characters on the snake.
     * @param character the string to get offset from
     * @param step the index
     * @return the character string at index i
     */
    String getCharacter(String character, int step) {
        if(character.length() > 1) {
            if(Math.abs(step) >= character.length())
                    return character.split("")[character.length()-1];
                            else
                    return character.split("")[Math.abs(step)];
        } else return character.split("")[0];
    }

    /**
     * Prints a box with given text. Does a lot of low level stuff with coordinates for centering...
     * @param y the y coordinate where the box should start
     * @param text the array of text that should be printed in the box.
     */
    private void printBox(int y, String... text) {
        // Determine the length of the longest string in text
        OptionalInt optInt = Arrays.stream(text).mapToInt((s) -> s.length()).max();
        int maxLength = optInt.isPresent() ? optInt.getAsInt() : 10;

        // Determine the width of the box with that information
        int width = (maxLength) % 2 == 0 ?
                -2+(this.width / 2) - (maxLength / 2) :
                -2+(this.width / 2) - ((maxLength-1)/2);

        // Print the 4 corners of the box
        Terminal.printText(1+this.startY+y, 1+this.startX+this.width-width, "+");
        Terminal.printText(1+this.startY+y, 1+this.startX+width, "+");
        Terminal.printText(1+this.startY+y+text.length+1, 1+this.startX+this.width-width, "+");
        Terminal.printText(1+this.startY+y+text.length+1, 1+this.startX+width, "+");

        // For each line in 'text', we creat the vertical borders
        for(int i = 0; i < text.length; i++) {
            Terminal.printText(1 + this.startY + y + 1 + i, 1 + this.startX + this.width - width, "I");
            Terminal.printText(1 + this.startY + y + 1 + i, 1 + this.startX + width, "I");
        }

        // We also put the horizontal borders, but these are always static..
        printLine(new Pos(width+1, y), new Pos(this.width-width, y), "-", false);
        printLine(new Pos(width+1, y+1+text.length), new Pos(this.width-width, y+2), "-", false);

        // Then we fill the box with our text
        for(int i = 0; i < text.length; i++) {
            width = (text[i].length()) % 2 == 0 ?
                    -2+(this.width / 2) - (text[i].length() / 2) :
                    -2+(this.width / 2) - ((text[i].length()-1)/2);
            Terminal.printText(1 + this.startY + y + 1 + i, 1 + this.startX + width + 2, text[i]);
        }
    }

}
