package listeners;

public interface DeletedInsertionPointListener {

    /**
     * Listener that is created when an insertion point is deleted
     *
     * @param deletedLine the line that the insertion point was deleted on
     * @param deletedCol the column that the insertion point was deleted on
     */
    void handleDeletedInsertionPoint(int deletedLine, int deletedCol);
}
