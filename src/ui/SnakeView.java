package ui;

import ioadapter.TermiosTerminalAdapter;
import snake.Snake;
import snake.SnakeGame;
import snake.food.Food;
import util.Coords;
import util.Pos;
import util.Rectangle;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class SnakeView extends View {

    /**
     * The contained {@link SnakeGame}.
     */
    final SnakeGame game;
    /**
     * The relevant coordinates for displaying this view
     */
    private int startX, startY, width, height;

    /**
     * The adapter used that handles the printing
     */
    private TermiosTerminalAdapter termiosTerminalAdapter;

    public SnakeView(SnakeGame game, TermiosTerminalAdapter termiosTerminalAdapter) {
        super(termiosTerminalAdapter);
        this.game = game; this.termiosTerminalAdapter = termiosTerminalAdapter;
    }

    /**
     * update the coordinates of this view out of 
     */
    private void setLocalCoordinates() {
        /*try {*/
            Coords coords = super.uiCoordsReal;
            this.startX = coords.startX;
            this.startY = coords.startY;
            this.width = coords.width;
            this.height = coords.height;
/*        } catch(IOException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * render the snake game
     * @param activeHash the hash of the active window
     */
    @Override
    public void render(int activeHash) throws IOException {
        // Retrieve coordinates from superclass (set by ControllerFacade by going through tree)
        setLocalCoordinates();

        // Retrieve information from the snake game.
        Snake[] segments = game.getSnake().getSegments();
        Snake head = game.getSnake();

        // Print border + plus sign in the corner
        printLine(new Pos(width, 0), new Pos(width, height-1), "|", false);
        printLine(new Pos(0, height-1), new Pos(width-1, height-1), "-", false);
        this.termiosTerminalAdapter.printText(1 + startY + height, 1 + this.startX + width, "+");

        // Print score
        this.termiosTerminalAdapter.printText(startY + height,startX + width/2 -1, "Score: " + game.getScore());

        // Determine the skin of the snake
        String a = game.getSnake().getHeadString() + "x";
        a = game.getSnake().getHeadString() + "abcdef";

        int skip = 0;
        skip = printLine(head.getEnd(), head.getStart(), Arrays.stream(a.split("")).skip(skip).collect(Collectors.joining()), false);

        for(int i = segments.length-1; i >= 0; i--) {
            String s = Arrays.stream(a.split("")).skip(skip).collect(Collectors.joining());
            if(s.equals("")) s = a.split("")[a.length()-1];
            skip += printLine(segments[i].getEnd(), segments[i].getStart(),
                    s, false);
        }


        List<Food> fruits = game.getFruits();
        for(int i = 0; i < fruits.size(); i++)
            this.termiosTerminalAdapter.printText(
                    1+fruits.get(i).getPosition().y()+startY,
                    1+fruits.get(i).getPosition().x()+startX,
                    fruits.get(i).getCharacter());

        // Print lost/won
        if(!this.game.canContinue())
            if(this.game.isWon()) printBox(5, "You won", "Press enter to try again");
            else printBox(5, "You lost", "Press enter to try again");


    }

    /**
     * snake has  no cursor
     */
    @Override
    public void renderCursor() throws IOException {
        return;
    }

    /**
     * compare this object againts object o
     */
    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    protected int getLineLength(int focusedLine) {
        //TODO: whatever makes sense, width of the board? NO! line length is not relevant here!
        return 0;
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
        setLocalCoordinates();

        int dX = -(start.x() - end.x());
        int dY = -(start.y() - end.y());
        int stepX = dX != 0 ? dX / Math.abs(dX) : 0;
        int stepY = dY != 0 ? dY / Math.abs(dY) : 0;

        for(int i = 0;
            i < Math.abs(dX) || (isEqual && i <= Math.abs(dX));
            i++) {
            termiosTerminalAdapter.printText(1 + startY + start.y(), 1 + startX + start.x() + (i*stepX), getCharacter(character, i*stepX));
        }
        for(int i = 0;
            i < Math.abs(dY) || (isEqual && i <= Math.abs(dY));
            i++) {
            if(1 + startY + start.y() + (i*stepY)>0 && 1 + this.startX + start.x()>0)
                termiosTerminalAdapter.printText(1 + startY + start.y() + (i*stepY), 1 + this.startX + start.x(), getCharacter(character, i*stepY));
        }

        return Math.max(Math.abs(dX), Math.abs(dY));
    }

    /**
     * Gets the character at offset 'step'. Used to render skins on the snake.
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
    void printBox(int y, String... text) {
        setLocalCoordinates();
        // Determine the length of the longest string in text
        OptionalInt optInt = Arrays.stream(text).mapToInt((s) -> s.length()).max();
        int maxLength = optInt.isPresent() ? optInt.getAsInt() : 10;

        // Determine the width of the box with that information
        int width = (maxLength) % 2 == 0 ?
                -2 + (this.width / 2) - (maxLength / 2) :
                -2 + (this.width / 2) - ((maxLength - 1) / 2);

        // Print the 4 corners of the box
        termiosTerminalAdapter.printText(1 + this.startY + y, 1 + this.startX + this.width - width, "+");
        termiosTerminalAdapter.printText(1 + this.startY + y, 1 + this.startX + width, "+");
        termiosTerminalAdapter.printText(1 + this.startY + y + text.length + 1, 1 + this.startX + this.width - width, "+");
        termiosTerminalAdapter.printText(1 + this.startY + y + text.length + 1, 1 + this.startX + width, "+");

        // For each line in 'text', we creat the vertical borders
        for (int i = 0; i < text.length; i++) {
            termiosTerminalAdapter.printText(1 + this.startY + y + 1 + i, 1 + this.startX + this.width - width, "I");
            termiosTerminalAdapter.printText(1 + this.startY + y + 1 + i, 1 + this.startX + width, "I");
        }

        // We also put the horizontal borders, but these are always static..
        printLine(new Pos(width + 1, y), new Pos(this.width - width, y), "-", false);
        printLine(new Pos(width + 1, y + 1 + text.length), new Pos(this.width - width, y + 2), "-", false);

        // Then we fill the box with our text
        for (int i = 0; i < text.length; i++) {
            width = (text[i].length()) % 2 == 0 ?
                    -2 + (this.width / 2) - (text[i].length() / 2) :
                    -2 + (this.width / 2) - ((text[i].length() - 1) / 2);
            termiosTerminalAdapter.printText(1 + this.startY + y + 1 + i, 1 + this.startX + width + 2, text[i]);
        }
    }

    @Override
    public int getFocusedCol() {
        //TODO: column snake is in?
        return 0;
    }

    @Override
    public int getFocusedLine() {
        //TODO: line snake is in?
        return 0;
    }

    @Override
    public int getTotalContentHeight() {
        /*TODO: the logical thing to make snake scrollable would be to return the
           height of the game here?
           In the long run we might have to think about if scrolling and resizing the snake game
           do not exclude each other though and where to put that logic*/
        return 0;
    }

    /**
     * This also scales the playing field in {@link SnakeGame} itself. Its bounds are given to this view upon creation.
     * @throws IOException when the coords cannot be retrieved
     */
    void scaleGame() throws IOException {
        Coords ui = super.uiCoordsReal;
        Rectangle rct = new Rectangle(ui.startX, ui.startY, ui.width, ui.height);
        this.game.modifyPlayfield(rct);
    }

}
