package files;

import util.json.TextLocation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class JsonFileHolder extends FileHolder {

    private final TextLocation location;

    public JsonFileHolder(String path, byte[] lineSeparator, TextLocation location) {
        super(path, lineSeparator);
        this.location = location;
    }

    @Override
    public byte[] getContent() throws IOException, RuntimeException {
        byte[] fileContent = readSpecifiedLine();
        checkInvalidCharacters(fileContent);
        checkLineSeparator(fileContent);
        return fileContent;
    }

    @Override
    int save(byte[] fileContent) {

        ArrayList<ArrayList<Byte>> allBytes = readAllFileContent();

        // 1. In the specified line, we get the first part which is static: '"file.txt": "'
        byte[] firstLine = FileAnalyserUtil.spliceArray(allBytes.get(location.line()), 0, location.column());

        // 2. Then we add the rest: content
        byte[] lastBytes = getLastBytes();
        byte[] result = new byte[firstLine.length + fileContent.length + lastBytes.length];

        System.arraycopy(firstLine, 0, result, 0, firstLine.length);
        System.arraycopy(fileContent, firstLine.length, result, firstLine.length, firstLine.length + fileContent.length - firstLine.length);
        if (firstLine.length + fileContent.length + lastBytes.length - (firstLine.length + fileContent.length) >= 0)
            System.arraycopy(lastBytes, firstLine.length + fileContent.length, result, firstLine.length + fileContent.length, firstLine.length + fileContent.length + lastBytes.length - (firstLine.length + fileContent.length));

        // 3. we replace the entry with result
        ArrayList<ArrayList<Byte>> replacedLines = new ArrayList<>();
        for(int i = 0; i < allBytes.size(); i++) {
            if(i == location.line())
                FileAnalyserUtil.createByteWrapArrayList(result);
            else
                replacedLines.add(allBytes.get(i));
        }

        try {
            Files.write(Path.of(this.getPath()), flatten(replacedLines));
        } catch(IOException e) {

        }

        return 1;
    }

    /**
     * Reads the content between the quotation marks in the json file using the {@link JsonFileHolder#location}.
     * @return the bytes in the file starting from the given location
     * @throws IOException when reading the file failed
     */
    byte[] readSpecifiedLine() throws IOException {

        ArrayList<ArrayList<Byte>> allBytes = FileAnalyserUtil.getContentLines(
                Files.readAllBytes(Path.of(this.getPath())), getLineSeparator()
        );

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

        // We get the line this holder represents
        ArrayList<Byte> specifiedLine = readAllFileContent().get(location.line());

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
     * Reads the content of the file. We need this method because the JSON file has
     * to read the full file for returning the specified content and saving new content
     * at the right place
     * @return a {@link ArrayList} in a {@link ArrayList} representing each 'line' of {@link Byte} values seperated by the {@link FileHolder#getLineSeparator()}
     */
    ArrayList<ArrayList<Byte>> readAllFileContent() {
        ArrayList<ArrayList<Byte>> allBytes = null;

        try {
            allBytes = FileAnalyserUtil.getContentLines(
                    Files.readAllBytes(Path.of(this.getPath())), getLineSeparator()
            );
        } catch(IOException io) {
            System.out.println("If this triggers, we are in big trouble");
            io.notify();
        }

        return allBytes;
    }

    /**
     * Mends two byte[] together
     * @return the mended list
     */
    byte[] flatten(ArrayList<ArrayList<Byte>> sdfsldfkjsdfjsjj) {

        int length = 0;
        for(ArrayList<Byte> list : sdfsldfkjsdfjsjj)
            length += list.size();

        byte[] sdfsdfeefeef = new byte[length];
        int currentI = 0;

        for(ArrayList<Byte> list : sdfsldfkjsdfjsjj) {
            for(int i = 0; i < list.size(); i++) {
                sdfsdfeefeef[i+currentI] = list.get(i);
                currentI++;
            }
        }

        return sdfsdfeefeef;

    }

}
