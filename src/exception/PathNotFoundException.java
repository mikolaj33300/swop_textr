package exception;

public class PathNotFoundException extends RuntimeException {

    /**
     * Creates a new PathNotFoundException
     * Used when a path is not found
     */
    public PathNotFoundException() {
        super("Path not found.");
    }
}