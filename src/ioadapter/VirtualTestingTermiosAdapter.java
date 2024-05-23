package ioadapter;

import util.Coords;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class VirtualTestingTermiosAdapter implements TermiosTerminalAdapter{
    private ArrayList<Integer> inputStream;

    //TODO we could use a proxy swing timer object that gets returned by the adapter (that TextR could work with)
    // and have some better timer management,
    // where we could make the virtual timer tick from a unit test and that could trigger the actionlisteners
    // when appropriate. But this should do for the current implementation.
    ActionListener timerActionListener;

    private ArrayList<ArrayList<Character>> virtualScreen;
    int screenHeight;
    int screenWidth;

    int cursorX;
    int cursorY;

    private Runnable runnableForTerminalInput = null;

    public ArrayList<char[]> getVirtualScreen(){
        ArrayList<char[]> clonedVirtualScreen = new ArrayList<>();
        for (int i = 0; i < virtualScreen.size(); i++) {
            char[] clonedLine = new char[screenWidth];
            for(int j = 0; j<screenWidth; j++){
                clonedLine[j] = virtualScreen.get(i).get(j);
            }
            clonedVirtualScreen.add(clonedLine);
        }
        return clonedVirtualScreen;
    }

    /**
     * Initial stream is a stream of values that read byte must return followed by INTEGER.MIN_VALUE that terminates the list
     * and the loop, or -1 in the case of a delay that needs to elapse. -1 is never passed into TextR, but the terminating
     * value is since we need a way to stop the loop without using threads as per assignment.
     * @param screenWidth
     * @param screenHeight
     * @param initialInputStream
     */
    public VirtualTestingTermiosAdapter(int screenWidth, int screenHeight, ArrayList<Integer> initialInputStream){
        virtualScreen = new ArrayList<>();
        for(int i = 0; i<screenHeight; i++){
            virtualScreen.add(createInitialLine(screenWidth));
        }
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        this.inputStream = new ArrayList<>(initialInputStream.size());
        inputStream.addAll(initialInputStream);
    }

    private ArrayList<Character> createInitialLine(int length){
        ArrayList<Character> toRetArrList = new ArrayList<Character>();
        for(int i = 0; i<length; i++){
            toRetArrList.add(' ');
        }
        return toRetArrList;
    }

    @Override
    public void clearScreen() {
        for(int i = 0; i<screenHeight; i++){
            virtualScreen.add(createInitialLine(screenWidth));
        }
    }

    @Override
    public void enterRawInputMode() {
        //We assume that classes to be tested already are in raw input mode. It would give an equivalent result to what
        //we already do here.
    }

    @Override
    public void leaveRawInputMode() {
        //See above
    }

    /**
     * Inherits doc of original termios, but also throws exception when we go outside of screen (easier for testing)
     * @param row
     * @param column
     */
    @Override
    public void moveCursor(int row, int column) {
        if(row<1 || column < 1 ||row>screenHeight || column>screenWidth){
            throw new IllegalArgumentException();
        }
        cursorX = column;
        cursorY = row;
    }


    /**
     * Inherits doc of original termios, but also throws exception when text goes past a line
     * @param row
     * @param column
     * @param text
     */
    @Override
    public void printText(int row, int column, String text) {
        if(row<1 ||column<1 || column+text.length()-1 > screenWidth){
            throw new IllegalArgumentException();
        }

        char[] charArray = text.toCharArray();
        int i = 0;
        for(char c : charArray){
            virtualScreen.get(row-1).set(column-1+i, c);
            i++;
        }


    }

    /**
     * Pops first element from arraylist. If no elements left, returns 0
     * @return
     * @throws IOException
     */
    @Override
    public int readByte() throws IOException {
        if(inputStream.size() != 0){
            int toReturn = inputStream.get(0);
            inputStream.remove(0);
            return toReturn;
        } else {
            return Integer.MIN_VALUE; //loop is over, this is a byte to signal to kill the loop
        }

    }

    /**
     * @deprecated
     * Inherits doc from real termios, but does not actually wait until the deadline to trigger a delay.
     * It relies on the initial stream passed to let a user of this class signal when the deadline is reached in the
     * sequence of bytes/events. Also see constructor doc.
     * @param deadline
     * @return
     * @throws TimeoutException
     */
    @Override
    @Deprecated
    public int readByte(long deadline) throws TimeoutException {
/*        if(inputStream.size() != 0){
            int toReturn = inputStream.get(0);
            *//*-1 can be used here to signal to wait the delay, as waiting the actual time is not that interesting
            * for a quick unit test, and is also discouraged as the assignment forbids use of other timing classes*//*
            inputStream.remove(0);
            if(toReturn == -1){
                throw new TimeoutException();
            }
            return toReturn;
        } else {
            return Integer.MIN_VALUE; //loop is over, this is a byte to signal to kill the loop
        }*/
        return 0;
    }

    /**
     * returns the dimensions of the virtual screen
     * @return
     * @throws IOException
     */
    @Override
    public Coords getTextAreaSize() throws IOException {
        return new Coords(0, 0, screenWidth, screenHeight);
    }

    @Override
    public void subscribeToResizeTextArea(ResizeListener l) {
        //TODO: We can put resize events as something triggerable in the virtual? But we can test most events already. This is not a priority right now
    }

    @Override
    public void setInputListenerOnAWTEventQueue(Runnable runnable) {
        if(runnableForTerminalInput != null){
            throw new RuntimeException("Already a stdin listener attached!");
        } else {
            this.runnableForTerminalInput = runnable;
        }
    }

    @Override
    public void clearInputListener() {

    }

    @Override
    public void subscribeToKeyPresses(ASCIIKeyEventListener newAsciiListener) {
        //TODO: We can put Keypress events as something triggerable in the virtual? But we can test most events. This is not a priority right now

    }

    @Override
    public void unsubscribeFromKeyPresses(ASCIIKeyEventListener listenerToRemove) {

    }

    @Override
    public void unsubscribeFromResizeTextArea(ResizeListener l) {

    }

    @Override
    public void addAndStartTimerListener(int delay, ActionListener actionListener) {
        if(this.timerActionListener != null){
            throw new RuntimeException("Timer action already registered, are we calling this correctly?");
        }
        this.timerActionListener = actionListener;
    }

    public void triggerTimerAction(){
        this.timerActionListener.actionPerformed(null);
    }

    /**
     * Adds an integer to the inputstream
     * @param i the integer for the input stream.
     * -2 halts the loop, MAX INTEGER delimits events, other normal integers are integers readable from the input
     *
     * stream when the stdin event fires.
     */
    public void putByte(int i) {
        this.inputStream.add(i);
    }

    /**
     * For testing purposes: gets the cursor X position
     * @return
     */
    public int getCursorX() {
        return cursorX;
    }

    /**
     * For testing purposes: gets the cursor Y position
     * @return
     */
    public int getCursorY() {
        return cursorY;
    }

    public void runStdinListener(){
        Runnable currentListener = this.runnableForTerminalInput;
        this.runnableForTerminalInput = null;
        currentListener.run();
    }
}
