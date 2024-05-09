package files;

import util.json.JsonUtil;
import util.json.TextLocation;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class JsonFileHolder extends FileHolder {

    /**
     * The path to a certain entry in the json structure
     */
    private final String entryPath;

    /**
     * The referenced file buffer
     */
    private final FileBuffer buffer;

    /**
     * The constructor for JsonFileHolder.
     * @param buffer the buffer where the contents should be saved
     * @param entryPath the path of an entry in the json structure
     */
    public JsonFileHolder(FileBuffer buffer, String entryPath) {
        super(buffer.getPath(), buffer.getLineSeparator());
        this.entryPath = entryPath;
        this.buffer = buffer;
    }

    @Override
    public byte[] getContent() throws IOException, RuntimeException {
        byte[] fileContent = readSpecifiedLine();
        fileContent = FileAnalyserUtil.spliceArray(
                FileAnalyserUtil.toArray(fileContent),
                0,
                fileContent.length - getLastBytes().length + 1
        );
        checkInvalidCharacters(fileContent);
        checkLineSeparator(fileContent);
        return fileContent;
    }

    @Override
    int save(byte[] fileContent) {

        TextLocation location = getJsonContentLocation();
        ArrayList<ArrayList<Byte>> allBytes = buffer.getLines();

        // 1. In the specified line, we get the first part which is static: '"file.txt": "'
        byte[] firstLine = FileAnalyserUtil.spliceArray(allBytes.get(location.line()), 0, location.column()+1);

        // 2. Then we add the rest: content
        byte[] lastBytes = getLastBytes();
        byte[] lineAndContent = FileAnalyserUtil.copyArray(firstLine, fileContent);
        byte[] fullLine = FileAnalyserUtil.copyArray(lineAndContent, lastBytes);

        // 3. we replace the entry with result
        ArrayList<ArrayList<Byte>> replacedLines = new ArrayList<>();
        for(int i = 0; i < allBytes.size(); i++) {
            if(i == location.line())
                replacedLines.add(FileAnalyserUtil.toArray(fullLine));
            else
                replacedLines.add(allBytes.get(i));
        }

        // We save in the FileBuffer
        buffer.setLinesArrayList(replacedLines);

        return 1;
    }

    /**
     * Reads the content between the quotation marks in the json file using the {@link JsonFileHolder#getJsonContentLocation()}.
     * @return the bytes in the file starting from the given location
     */
    byte[] readSpecifiedLine() {

        TextLocation location = getJsonContentLocation();
        ArrayList<ArrayList<Byte>> allBytes = buffer.getLines();

        ArrayList<Byte> specifiedLine = allBytes.get(location.line());

        // Column + 1 because the location.column() is the location before the "
        // size() - 1 because we don't want to read the " at the end
        int startRead = location.column()+1;
        int endRead = specifiedLine.size()-1;

        return FileAnalyserUtil.spliceArray(specifiedLine, startRead, endRead);

    }

    /**
     * This method decides how many bytes we have at the end:
     * "tag": "hello", OR "tag": "hello"
     * @return the byte[] which represents a " or ",
     */
    byte[] getLastBytes() {

        TextLocation location = getJsonContentLocation();

        // We get the line this holder represents
        ArrayList<Byte> specifiedLine = buffer.getLines().get(location.line());

        // Then we get the last bytes
        // 1. there is only " at the end
        if(FileHolder.areContentsEqual(
                new byte[] {specifiedLine.get(specifiedLine.size()-1)},
                "\"".getBytes()
        ))
            return "\"".getBytes();

        else return "\",".getBytes();

    }

    /**
     * Retrieves the location of the content for this file.
     * Purpose is to catch exceptions here
     * @return a {@link TextLocation} object specifying the location of the content for a given {@link JsonFileHolder#entryPath}
     */
    TextLocation getJsonContentLocation() {
        return JsonUtil.getTextLocationFor(this.buffer, this.entryPath);
    }

}
