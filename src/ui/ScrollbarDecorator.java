package ui;

import ioadapter.TermiosTerminalAdapter;
import util.Coords;
import util.Rectangle;

import java.io.IOException;

//This decorator does not extend an abstract ViewDecorator because we need to extend all the methods anyways
public class ScrollbarDecorator extends View {
  /**
   * the view rendering the underlying stuff
   */
    View wrappedView;

    /**
     * create a new instance of this object with the View v
     */
    public ScrollbarDecorator(View v) {
        super(v.getTermiosTerminalAdapter());
        this.wrappedView = v;
    }

    /**
     * render everything using the render logic of the view
     * @param activeHash the hash of the active window used to see if we are the active window
     */
    //TODO: these operations could be done better using some special library for vector/matrix operations? Lets only do this if enough time
    @Override
    public void render(int activeHash) throws IOException {
        Coords thisRealDimensions;
        /*try{*/
            thisRealDimensions = super.uiCoordsReal;
/*        } catch (IOException e){
            System.out.println(e);
            return;
        }*/
        updateWrappeeDimensions();
        wrappedView.render(activeHash);

        int focusedLine = wrappedView.getFocusedLine();
        int contentHeight = wrappedView.getTotalContentHeight();
        this.renderScrollbar(thisRealDimensions.height-1, thisRealDimensions.startX+ thisRealDimensions.width-1, thisRealDimensions.startY, focusedLine, contentHeight);

        int focusedCol = wrappedView.getFocusedCol();
        renderHorzScrollbar(thisRealDimensions.width-1, thisRealDimensions.startX, thisRealDimensions.startY+thisRealDimensions.height-1,
                focusedCol,
                wrappedView.getLineLength(focusedLine));
    }

    /**
     * render the cursor on the right position
     */
    @Override
    public void renderCursor() throws IOException {
        updateWrappeeDimensions();
        wrappedView.renderCursor();
    }

    /**
     * check if this equals o
     * @return true if o is equals this false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof ScrollbarDecorator s) {
            return s.wrappedView.equals(this.wrappedView);
        }
        return false;
    }

    /**
     * get the length of the focussed line
     * @return int
     */
    @Override
    protected int getLineLength(int focusedLine) {
        return wrappedView.getLineLength(focusedLine);
    }

    /**
     * get the current cursor column
     * @return int: column
     */
    @Override
    public int getFocusedCol() {
        return wrappedView.getFocusedCol();
    }

    /**
     * get the current cursor line
     * @return int: line
     */
    @Override
    public int getFocusedLine() {
        return wrappedView.getFocusedLine();
    }

    /**
     * get the height of the complete content
     * @return the number of lines possibly printed
     */
    @Override
    public int getTotalContentHeight() {
        return wrappedView.getTotalContentHeight();
    }

    /**
     * @param height the height of our view
     * @param scrollStartX the x coordinate for the scrollbar
     * @param focusedLine the number of the focussed line
     * @param contentTotalHeight the amount of lines of the filebuffer
     */
    private void renderScrollbar(int height, int scrollStartX, int startY, int focusedLine, int contentTotalHeight){
        contentTotalHeight = contentTotalHeight == 0 ? 1 : contentTotalHeight;
        int visibleStartPercent = (focusedLine*height)/contentTotalHeight;
        int visibleEndPercent = ((1+focusedLine)*height)/contentTotalHeight;

/*
        for (int i = 0; i < visibleStartPercent; i++)
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "|");
        for (int i = visibleStartPercent; i < visibleEndPercent; i++)
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "+");
        for (int i = visibleEndPercent; i < height-startY-2; i++)
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "|");
*/

        for(int i = 0; i < height; i++){
            if(i<visibleStartPercent){
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "|");
            } else if (i<visibleEndPercent){
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "+");
            } else {
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "|");
            }
        }

        /*
        for(int i = 0; i<(height-1); i++){
            if((i*1.0/(height-1)>=visibleStartPercent) && (i*1.0/height <= visibleEndPercent)){
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "+");
            } else {
                termiosTerminalAdapter.printText(1+startY+i, 1+scrollStartX, "|");
            }
        }
        */
    }

    /**
     * render a horizontal renderHorzScrollbar
     * @param width the window width
     * @param startX the x coordinate to draw the scrollbar on
     * @param startY the Y coordinate to draw the scrollbar on
     * @param focusedCol the focussed column
     * @param contentTotalWidth the
     */
    private void renderHorzScrollbar(int width, int startX, int startY, int focusedCol, int contentTotalWidth){
        contentTotalWidth = contentTotalWidth == 0 ? 1 : contentTotalWidth;
        int visibleStartPercent = (focusedCol*width)/contentTotalWidth;
        int visibleEndPercent = ((1+focusedCol)*width)/contentTotalWidth;

/*        //TODO: Rewrite this for loop to not get out of bounds error like in vertical bar
        //TODO: Test this

        for (int i = 0; i < visibleStartPercent; i++)
            termiosTerminalAdapter.printText(1+startY, 1+startX+i, "-");
        for (int i = visibleStartPercent; i < visibleEndPercent; i++)
            termiosTerminalAdapter.printText(1+startY, 1+startX+i, "+");
        for (int i  = visibleEndPercent; i < width-1; i++)
            termiosTerminalAdapter.printText(1+startY, 1+startX+i, "-");*/

        for(int i = 0; i < width; i++){
            if(i<visibleStartPercent){
                termiosTerminalAdapter.printText(1+startY, 1+startX+i, "-");
            } else if (i<visibleEndPercent){
                termiosTerminalAdapter.printText(1+startY, 1+startX+i, "+");
            } else {
                termiosTerminalAdapter.printText(1+startY, 1+startX+i, "-");
            }
        }
    }

    /**
     * update the Dimensions of the wrapped view
     */
    private void updateWrappeeDimensions() throws IOException {
        Coords thisRealDimensions = super.uiCoordsReal;

        Rectangle wrappeeRealDimensions = new Rectangle(thisRealDimensions.startX, thisRealDimensions.startY, thisRealDimensions.width-1, thisRealDimensions.height-1);
        wrappedView.setRealCoords(wrappeeRealDimensions);
    }

    /**
     * set the termiosTerminalAdapter of the wrapped view
     * @param adapter the adapter
     */
    public void setTermiosTerminalAdapter(TermiosTerminalAdapter adapter){
        this.termiosTerminalAdapter = adapter;
        wrappedView.setTermiosTerminalAdapter(adapter);
    };
}

