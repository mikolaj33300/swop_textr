package listeners;

public interface EnteredInsertionPointListener {

    /**
     * Listener that is created when an insertion point is entered
     *
     * @param deletedLine the line that the insertion point was entered on
     * @param deletedCol the column that the insertion point was entered on
     */
    void handleEnteredInsertionPoint(int deletedLine, int deletedCol);
}
