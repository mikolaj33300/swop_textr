package listeners;

public interface DeletedCharListener {

    /**
     * Listener that is created when a char is deleted
     *
     * @param deletedLine the line that the char was deleted on
     * @param deletedCol the column that the char was deleted on
     */
    void handleDeletedChar(int deletedLine, int deletedCol);
}
