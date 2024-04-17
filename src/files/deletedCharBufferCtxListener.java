package files;

public class deletedCharBufferCtxListener implements deletedCharListener{
    private BufferCursorContext listeningBuf;
    @Override
    public void handleDeletedChar(int deletedLine, int deletedCol) {
        if(listeningBuf.getInsertionPointLine()==deletedLine && listeningBuf.getInsertionPointCol()==deletedCol
        && deletedCol > listeningBuf.containedFileBuffer.getLines().get(deletedLine).size()){
            listeningBuf.moveCursorLeft();
        }
    }
}
