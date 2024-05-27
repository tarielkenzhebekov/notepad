import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.StandardOpenOption;

import java.nio.file.FileAlreadyExistsException;

public class FileHandler {
    
    private static final String DEFAULT_FILE_NAME = "Untitled";

    private static final String FILE_CHANGED_SYMBOL = "*";

    private static final int BUFFER_SIZE = DataStorageUnit.KB * 8;

    private static final int FILE_MAX_SIZE = DataStorageUnit.MB * 100;

    public static final class Status {

        public static final int SUCCESS = 0;

        public static final int FILECHANNEL_IS_NULL = (1 << 0);

        public static final int FILE_IS_TOO_LARGE = (1 << 1);

        public static final int PATH_IS_NULL = (1 << 2);

        public static final int FILE_ALREADY_EXIST = (1 << 3);

        private Status() {}
        
    }

    private ByteBuffer buffer;
    
    private Path path;

    private volatile boolean savedFlag;

    public FileHandler() {
        buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);
        newFile(null);
    }

    public boolean isSaved() {
        return savedFlag;
    }

    public void setSavedFlag(boolean newValue) {
        this.savedFlag = newValue;
    }

    public Path getPath() {
        return path;
    }

    public void newFile(Path path) {
        this.path = path;
        savedFlag = true;
    }

    public String getFileName() {
        String fileName = DEFAULT_FILE_NAME;

        if (path != null) {
            fileName = path.getFileName().toString();
        }

        if (!savedFlag) {
            fileName = fileName + FILE_CHANGED_SYMBOL;
        }
            
        return fileName;
    }
        
    public Result<String, Integer> readContentFromFile() {
        StringBuilder builder = new StringBuilder("");

        try {
            FileChannel in = openFileChannel();
    
            if (in == null) {
                Integer status = Status.FILECHANNEL_IS_NULL;
                Result<String, Integer> result = Result.fail(status);
    
                return result;
            }
    
            buffer.limit(buffer.capacity());
    
            while (in.position() < in.size()) {
                readFromChannel(in);
    
                if ((builder.length() + buffer.limit()) >= FILE_MAX_SIZE) {
                    Integer status = Status.FILE_IS_TOO_LARGE;
                    Result<String, Integer> result = Result.fail(status);
    
                    return result;
                }
    
                builder.append(Charset.forName("UTF-8").decode(buffer)); // possible couse of unknown symbols when
                                                                         // saving-opening cirillic text
                buffer.flip();
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
            
            return null;
        }

        savedFlag = true;

        String content = builder.toString();
        builder = null;

        Result<String, Integer> result = Result.success(content);

        return result;
    }

    public int writeByteArrayToFile(byte[] byteArray) {
        try {
            FileChannel out = openFileChannel();
            
            if (out == null) {
                return Status.FILECHANNEL_IS_NULL;
            }
            
            buffer.limit(buffer.capacity());
            out.truncate(0);
            
            int iterations = byteArray.length / buffer.capacity();
            int i = 0;
            int offcet = 0;

            while (i < iterations) {
                buffer.put(byteArray, offcet, buffer.limit());

                buffer.flip();

                writeToChannel(out);

                offcet += buffer.limit();
                i++;
            }

            buffer.put(byteArray, offcet, byteArray.length - offcet);
            buffer.flip();

            writeToChannel(out);
        } catch (IOException ioe) {
            System.out.println(ioe);
            
            return -1;
        }

        savedFlag = true;

        return Status.SUCCESS;
    }

    private FileChannel openFileChannel() throws IOException,
                                                 NullPointerException {
        FileChannel channel = null;

        if (path == null) {
            throw new NullPointerException("Path is null!");
        }

        if (!Files.exists(path)) {
            channel = createFileChannel();

            return channel;
        }

        channel = FileChannel.open(path,
                              StandardOpenOption.READ,
                              StandardOpenOption.WRITE);
        
        return channel;
    }
    
    private FileChannel createFileChannel() throws IOException, NullPointerException {
        FileChannel channel;

        if (path == null) {
            throw new NullPointerException("Path is null!");
        }

        if (Files.exists(path)) {
            throw new FileAlreadyExistsException(path.getFileName().toString() + " is already exist!");
        }

        channel = FileChannel.open(path,
                                   StandardOpenOption.READ,
                                   StandardOpenOption.WRITE,
                                   StandardOpenOption.CREATE_NEW);

        return channel;
    }

    private void readFromChannel(FileChannel channel) throws IOException {
        do {
            int bytes = channel.read(buffer);
            
            if (bytes <= 0) {
                break;
            }
        } while (buffer.hasRemaining());

        buffer.flip();
    }

    private void writeToChannel(FileChannel channel) throws IOException {
        do {
            int bytes = channel.write(buffer);
        
            if (bytes <= 0) {
                break;
            }
        } while (buffer.hasRemaining());

        buffer.flip();
    }
}