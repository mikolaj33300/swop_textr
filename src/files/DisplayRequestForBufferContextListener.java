package files;

public interface DisplayRequestForBufferContextListener {

    /**
     * Listener to notify a request to a BufferCursorContext
     * @param ctx the buffer cursor context
     */
    public void notifyRequest(BufferCursorContext ctx);
}
