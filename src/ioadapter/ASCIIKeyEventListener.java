package ioadapter;

public interface ASCIIKeyEventListener {

    /**
     * Notifies the listener of a normal key press: a character in TextR that consists of a single byte.
     * @param byteInt The byte value of the key press.
     */
    public void notifyNormalKey(int byteInt);

    /**
     * Notifies the listener of a surrogate key press: a character in TextR that consists of two bytes.
     * @param first The first byte value of the key press.
     * @param second The second byte value of the key press.
     */
    public void notifySurrogateKeys(int first, int second);

}
