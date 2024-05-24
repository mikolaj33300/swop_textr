package files;

public interface OpenFileOnPathRequestListener {
    /**
     * Listener to notify a request to open a file
     * @param pathToOpen the path to open
     */
    public void notifyRequestToOpenFile(String pathToOpen);
}
