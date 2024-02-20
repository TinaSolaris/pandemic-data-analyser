package Commands;

public class ReadFilenameCommand extends Command {
    private String fileName;

    public ReadFilenameCommand(String fileName) {
        super(CommandType.READ_FILENAME);

        if (fileName == null || fileName.isBlank())
            throw new IllegalArgumentException("The fileName should not be null, empty, or consist of white-space characters only");

        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "ReadFilenameCommand{" +
                "fileName='" + fileName + '\'' +
                '}';
    }
}
