package controller;

import ioadapter.TermiosTerminalAdapter;
import window.*;

import java.io.IOException;

/**
 * This visitor is used so {@link Window} instances can be called by {@link Window#accept(WindowVisitor)}.
 * And the appropriate display can be created without the display actually having to know about the different window types.
 */
public class DisplayFromWindowVisitor implements WindowVisitor {

    /**
     * The new {@link TermiosTerminalAdapter} to be used for the new {@link DisplayFacade}.
     */
    private TermiosTerminalAdapter newAdapter;

    /**
     * The newlt created {@link DisplayFacade}
     */
    private DisplayFacade resultDisplayFacade;

    /**
     * The line separator argument to be used for the new {@link DisplayFacade}.
     */
    private byte[] lineSeparatorArg;

    /**
     * Upon creation of this object, {@link DisplayFacade} gives a {@link TermiosTerminalAdapter} which will be used
     * for the new {@link DisplayFacade}.
     * @param newAdapter the termios adapter for the new display facade
     */
    public DisplayFromWindowVisitor(TermiosTerminalAdapter newAdapter, byte[] lineSeparatorArg) {
        this.newAdapter = newAdapter;
        this.resultDisplayFacade = null;
        this.lineSeparatorArg = lineSeparatorArg;
    }

    /**
     * Creates a new {@link DisplayFacade} object from the focused window where appropriate for the current {@link DisplayFacade} to access.
     * @param fbw the {@link FileBufferWindow} where the 'open new display' command is triggered
     * @throws IOException ?
     */
    @Override
    public void visitFileWindow(FileBufferWindow fbw) throws IOException {
        FileBufferWindow windowToAdd = fbw.duplicate();
        windowToAdd.setTermiosAdapter(newAdapter);
        if (windowToAdd != null) {
            DisplayFacade displayToAdd = new DisplayFacade(windowToAdd, newAdapter, lineSeparatorArg);
            resultDisplayFacade = displayToAdd;
        }
    }

    /**
     * Snake always stays in the same window
     */
    @Override
    public void visitSnakeWindow(SnakeWindow sw) {
        // No putting snake on other window
    }

    /**
     * Directory always stays in the same window
     */
    @Override
    public void visitDirectoryWindow(DirectoryWindow dw) {

    }

    /**
     * The new {@link DisplayFacade} object to be used for the new window.
     * @return the new {@link DisplayFacade} object
     */
    public DisplayFacade getNewFacade(){
        return this.resultDisplayFacade;
    }

}