package inputhandler;

public interface DisplayRequestForInputHandlersListener {

    /**
     * using this we can open a new inputhandler
     */
    public void notifyRequestOpening(InputHandlingElement handler);
}
