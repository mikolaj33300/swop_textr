package listeners;

import files.BufferCursorContext;

public class DeletedInsertionBufferCtxListener implements DeletedInsertionPointListener {

    /**
     * The buffer that the listener is listening to
     */
    private BufferCursorContext listeningBuf;

    /**
     * Constructor for the listener
     *
     * @param bufferCursorContext the buffer that the listener is listening to
     */
    public DeletedInsertionBufferCtxListener(BufferCursorContext bufferCursorContext) {
        this.listeningBuf = bufferCursorContext;
    }

    /**
     * Listener that is created when an insertion point is deleted
     *
     * @param deletedLine the line that the insertion point was deleted on
     * @param deletedCol the column that the insertion point was deleted on
     */
    @Override
    public void handleDeletedInsertionPoint(int deletedLine, int deletedCol) {

    }
}
