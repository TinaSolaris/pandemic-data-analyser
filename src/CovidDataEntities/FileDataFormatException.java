package CovidDataEntities;

import java.util.ArrayList;

public class FileDataFormatException extends Exception {
    protected String filePath;
    protected ArrayList<InvalidWeeklyData> invalidRecords; // error desc. and not proper data

    public FileDataFormatException(String filePath, ArrayList<InvalidWeeklyData> invalidRecords) {
        super("Cannot proceed the file: " + filePath);

        this.filePath = filePath;
        this.invalidRecords = invalidRecords;
    }

    public String getFilePath() {
        return filePath;
    }

    public ArrayList<InvalidWeeklyData> getInvalidRecords() {
        return invalidRecords;
    }
}
