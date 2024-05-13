package controller.adapter;

import ui.TerminalPanel;
import util.Coords;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SwingTermiosTerminalAdapter implements TermiosTerminalAdapter {
    private TerminalPanel panelToRenderOn;

    //TODO: should this class create panel? Or should other classes use it as well?
    public SwingTermiosTerminalAdapter(TerminalPanel panelToRenderOn){
        this.panelToRenderOn = panelToRenderOn;
    }
    @Override
    public void clearScreen() {
        panelToRenderOn.clearBuffer();
    }

    @Override
    public void enterRawInputMode() {

    }

    @Override
    public void leaveRawInputMode() {

    }

    @Override
    public void moveCursor(int row, int column) {
        //TODO: in the char array use | on the place of the cursor
        panelToRenderOn.repaint();
    }

    @Override
    public void printText(int row, int column, String text) {
        //TODO: change the char[][]
        panelToRenderOn.repaint();
    }

    @Override
    public int readByte() throws IOException {
        return 0;
    }

    @Override
    public int readByte(long deadline) throws IOException, TimeoutException {
        return 0;
    }

    @Override
    public Coords getTextAreaSize() throws IOException {
        return panelToRenderOn.getCoords();
    }
}
