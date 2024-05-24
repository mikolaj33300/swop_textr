package files;

public interface DisplayRequestForFileBufferListener {

    /**
     * Listener to notify a request to a FileBuffer
     * @param fb the file buffer
     */
    public void notifyRequestOpening(EditableFileBuffer fb);
}
