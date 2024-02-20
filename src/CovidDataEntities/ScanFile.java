package CovidDataEntities;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ScanFile {
    private final String filePath;
    private ArrayList<WeeklyDataProper> validItems;
    private ArrayList<InvalidWeeklyData> invalidItems;

    public ScanFile(String filePath) throws Exception {
        if (filePath == null || filePath.isBlank())
            throw new Exception("The filePath should not be null, empty, or consist of white-space characters only");

        this.filePath = filePath;
    }

    public void scan() throws Exception {
        this.validItems = new ArrayList<>();
        this.invalidItems = new ArrayList<>();

        Scanner fileScan = null;

        try {
            fileScan = new Scanner(new File(filePath));

            int lineNo = 0;
            while (fileScan.hasNextLine()) {
                String line = fileScan.nextLine();
                readLine(lineNo, line);
                lineNo++;
            }
        }
        finally {
            if (fileScan != null)
                fileScan.close();
        }

        System.out.println("Scan completed for filePath: '" + filePath + "'; Valid Items Count: " + validItems.size() + "; Invalid Items Count: " + invalidItems.size());
    }

    private void readLine(int lineNo, String line) {
        if (lineNo == 0 && line.startsWith("dateRep"))
            return;

        try {
            WeeklyDataProper item = new WeeklyDataProper(line);
            validItems.add(item);
        }
        catch (Exception ex) {
            invalidItems.add(new InvalidWeeklyData(lineNo, ex.getMessage(), line));
        }
    }

    public ArrayList<WeeklyDataProper> getValidItems() {
        return validItems;
    }

    public ArrayList<InvalidWeeklyData> getInvalidItems() {
        return invalidItems;
    }
}