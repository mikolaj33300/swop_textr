package controller;

public interface ASCIIKeyEventListener {
    public void notifyNormalKey(int byteInt);

    public void notifySurrogateKeys(int first, int second);

}
