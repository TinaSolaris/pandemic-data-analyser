package CovidDataEntities;

public class FileFormatException extends Exception {
    private static final long serialVersionUID = 1L;
    protected int lineNo;
    protected String info; // error desc. and not proper data
    protected String filePath;

    public FileFormatException(String filePath, int lineNo, String info) {
        super("Cannot proceed the file: '" + filePath + "'; Line No: " + lineNo + "; Details: " + info);

        this.filePath = filePath;
        this.lineNo = lineNo;
        this.info = info;
    }

    public int getLineNo() {
        return lineNo;
    }

    public String getInfo() {
        return info;
    }

    public String getFilePath() {
        return filePath;
    }
}
