package files;

public class DeletedInsertionBufferCtxListener implements DeletedInsertionPointListener {
    private BufferCursorContext listeningBuf;

    public DeletedInsertionBufferCtxListener(BufferCursorContext bufferCursorContext) {
        this.listeningBuf = bufferCursorContext;
    }

    @Override
    public void handleDeletedInsertionPoint(int deletedLine, int deletedCol) {

    }
}
