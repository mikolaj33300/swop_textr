package observer;

import files.FileBuffer;
import java.io.IOException;

public interface FileBufferListener {
    public void render(FileBuffer containedFileBuffer, int hashCode, boolean active) throws IOException;// TODO: exchange containedFileBuffer for filebuffer contents, length, everything we need
}
