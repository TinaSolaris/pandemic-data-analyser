package CovidDataEntities;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CovidDataStore {
    private boolean ignoreFailedRecords;
    private boolean isDataInitialized;

    private ArrayList<InvalidWeeklyData> invalidRecords;
    private ArrayList<WeeklyDataProper> arrayListObjects = new ArrayList<>();
    private HashSet<WeeklyDataProper> hashSetObjects = new HashSet<>();
    private TreeMap<String, TreeData> treeMapObjects = new TreeMap<>();
    private TreeMap<TreeDataKey, TreeData> treeMapObjectsOpt = new TreeMap<>();

    public CovidDataStore(boolean ignoreFailedRecords) {
        this.ignoreFailedRecords = ignoreFailedRecords;
        this.isDataInitialized = false;
    }

    public void readFromFile(String filePath) throws FileFormatException, FileDataFormatException {
        if (filePath == null || filePath.isBlank())
            throw new IllegalArgumentException("The filePath should not be null, empty, or consist of white-space characters only");

        this.isDataInitialized = false;

        ScanFile scanFile;
        try {
            scanFile = new ScanFile(filePath);
            scanFile.scan();
        }
        catch (Exception ex) {
            throw new FileFormatException(filePath, 0, ex.getMessage());
        }

        arrayListObjects = scanFile.getValidItems();

        hashSetObjects = new HashSet<>();
        hashSetObjects.addAll(arrayListObjects);

        treeMapObjects = createTreeMapObjects(arrayListObjects);
        treeMapObjectsOpt = createTreeMapObjectsOpt(arrayListObjects);

        invalidRecords = scanFile.getInvalidItems();

        // the lines with errors should be rejected and suitable message should appear on standard error
        if (!ignoreFailedRecords && scanFile.getInvalidItems().size() > 0) {
            throw new FileDataFormatException(filePath, scanFile.getInvalidItems());
        }

        this.isDataInitialized = true;
    }

    public boolean isDataInitialized() {
        return isDataInitialized;
    }

    public boolean isWeekForCountryAL(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");
        if (week == null)
            throw new IllegalArgumentException("The week should not be null.");

        if (arrayListObjects.stream().anyMatch(n -> n.getCountry().equalsIgnoreCase(country) && new Week(n).equals(week)))
            return true;

        return false;
    }

    public boolean isWeekForCountryHS(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (week == null)
            throw new IllegalArgumentException("The week should not be null.");

        if (hashSetObjects.isEmpty())
            return false;

        for (WeeklyDataProper item : hashSetObjects) {
            Week itemWeek = new Week(item);
            if (item.getCountry().equalsIgnoreCase(country) && itemWeek.equals(week))
                return true;
        }

        return false;
    }

    public boolean isCountryInFileAL(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (arrayListObjects.isEmpty())
            return false;

        for (WeeklyDataProper item : arrayListObjects) {
            if (item.getCountry().equalsIgnoreCase(country))
                return true;
        }

        return false;
    }

    public boolean isCountryInFileHS(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (hashSetObjects.isEmpty())
            return false;

        for (WeeklyDataProper item : hashSetObjects) {
            if (item.getCountry().equalsIgnoreCase(country))
                return true;
        }

        return false;
    }

    public boolean isCountryInFileTM(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (treeMapObjects.isEmpty())
            return false;

        for (String key : treeMapObjects.keySet()) {
            String parsedCountry = parseCountry(key);
            if (parsedCountry.equalsIgnoreCase(country))
                return true;
        }

        return false;
    }

    public boolean isCountryInFileTMOpt(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (treeMapObjectsOpt.isEmpty())
            return false;

        for (TreeDataKey key : treeMapObjectsOpt.keySet()) {
            if (key.getCountry().equalsIgnoreCase(country))
                return true;
        }

        return false;
    }

    public boolean isWeekForCountryTM(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");
        if (week == null)
            throw new IllegalArgumentException("The week should not be null.");

        String key = createKey(country, week);

        return treeMapObjects.containsKey(key);
    }

    public boolean isWeekForCountryTMOpt(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");
        if (week == null)
            throw new IllegalArgumentException("The week should not be null.");

        return treeMapObjectsOpt.containsKey(new TreeDataKey(country, week));
    }

    // The year is not included as it is not mentioned in the requirements.
    // But it should be there because it is a part of the key.
    private String createKey(String country, Week week) {
        return country + "|" + week.getYear() + "|" + (week.getWeek() < 10 ? "0" + week.getWeek() : week.getWeek());
    }

    private final static Pattern keyExtract = Pattern.compile("^(?<country>[^|]*)|(?<year>\\d*)|(?<week>\\d*)$");

    private String parseCountry(String key) {
        Matcher matcher = keyExtract.matcher(key);
        if (!matcher.find())
            return "";

        return matcher.group("country");
    }

    private Week parseWeek(String key) {
        Matcher matcher = keyExtract.matcher(key);
        if (!matcher.find())
            return null;

        int year;
        try {
            year = Integer.parseInt(matcher.group("year"));
        } catch (Exception ex) {
            year = 0;
        }

        int weekNo;
        try {
            weekNo = Integer.parseInt(matcher.group("week"));
        } catch (Exception ex) {
            weekNo = 0;
        }

        if (year > 0 && weekNo > 0) {
            try {
                return new Week(year, weekNo);
            } catch (Exception ex) {
                return null;
            }
        }

        return null;
    }

    private TreeMap<String, TreeData> createTreeMapObjects(ArrayList<WeeklyDataProper> source) {
        var result = new TreeMap<String, TreeData>();
        for (WeeklyDataProper item: source) {
            String key = createKey(item.getCountry(), new Week(item.getYear(), item.getWeek()));
            if (!result.containsKey(key))
                result.put(key, new TreeData(item.getCases(), item.getDeaths(), item.getContinent()));
            else
                System.out.println("The key: " + key + " exists already");
        }
        return result;
    }

    private TreeMap<TreeDataKey, TreeData> createTreeMapObjectsOpt(ArrayList<WeeklyDataProper> source) {
        var result = new TreeMap<TreeDataKey, TreeData>();
        for (WeeklyDataProper item: source) {
            TreeDataKey key = new TreeDataKey(item.getCountry(), new Week(item.getYear(), item.getWeek()));
            if (!result.containsKey(key))
                result.put(key, new TreeData(item.getCases(), item.getDeaths(), item.getContinent()));
            else
                System.out.println("The key: " + key + " exists already");
        }
        return result;
    }

    public void printHS(int maxItems) {
        if (maxItems <= 0)
            throw new IllegalArgumentException("The maxItems should be a positive number.");

        System.out.println();
        System.out.println("printHS(" + maxItems + ")");

        if (hashSetObjects.isEmpty())
            return;

        int index = 0;
        var iterator = hashSetObjects.iterator();

        while (index < maxItems && iterator.hasNext()) {
            System.out.println("Index: " + index + ". Item: " + iterator.next());
            index++;
        }
    }

    public void printTM(int maxItems) {
        if (maxItems <= 0)
            throw new IllegalArgumentException("The maxItems should be a positive number.");

        System.out.println();
        System.out.println("printTM(" + maxItems + ")");

        if (treeMapObjects.isEmpty())
            return;

        int index = 0;
        for (String key: treeMapObjects.keySet()) {
            System.out.println("Index: " + index + ". Key: " + key + ". Item: " + treeMapObjects.get(key));
            index++;
            if (index >= maxItems)
                break;
        }
    }

    public int getCasesAL(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");
        if (week == null)
            throw new IllegalArgumentException("The week should not be null");

        if (arrayListObjects.isEmpty())
            return 0;

        int total = 0;

        for (WeeklyDataProper item : arrayListObjects) {
            if (week.equals(new Week(item)) && item.getCountry().equalsIgnoreCase(country))
                total += item.getCases();
        }

        return total;
    }

    public int getCasesHS(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");
        if (week == null)
            throw new IllegalArgumentException("The week should not be null");

        if (hashSetObjects.isEmpty())
            return 0;

        int total = 0;

        for (WeeklyDataProper item : hashSetObjects) {
            if (week.equals(new Week(item)) && item.getCountry().equalsIgnoreCase(country))
                total += item.getCases();
        }

        return total;
    }

    public int getCasesTM(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");
        if (week == null)
            throw new IllegalArgumentException("The week should not be null");

        if (treeMapObjects.isEmpty())
            return 0;

        String sourceKey = createKey(country, week);

        int total = 0;
        for (String key : treeMapObjects.keySet()) {
            if (sourceKey.equalsIgnoreCase(key)) {
                var item = treeMapObjects.get(key);
                total += item.getCases();
            }
        }

        return total;
    }

    public int getCasesTMOpt(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");
        if (week == null)
            throw new IllegalArgumentException("The week should not be null");

        if (treeMapObjectsOpt.isEmpty())
            return 0;

        int total = 0;
        for (TreeDataKey key : treeMapObjectsOpt.keySet()) {
            if (key.getCountry().equalsIgnoreCase(country) && key.getWeek().equals(week)) {
                var item = treeMapObjectsOpt.get(key);
                total += item.getCases();
            }
        }

        return total;
    }

    public int getCasesAL(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (arrayListObjects.isEmpty())
            return 0;

        int total = 0;

        for (WeeklyDataProper item : arrayListObjects) {
            if (item.getCountry().equalsIgnoreCase(country))
                total += item.getCases();
        }

        return total;
    }

    public int getCasesHS(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (hashSetObjects.isEmpty())
            return 0;

        int total = 0;

        for (WeeklyDataProper items : hashSetObjects) {
            if (items.getCountry().equals(country))
                total += items.getCases();
        }

        return total;
    }

    public int getCasesTM(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only.");

        if (treeMapObjects.isEmpty())
            return 0;

        int total = 0;
        for (String key: treeMapObjects.keySet()) {
            String parsedCountry = parseCountry(key);
            if (parsedCountry.equalsIgnoreCase(country))
            {
                var item = treeMapObjects.get(key);
                total += item.getCases();
            }
        }

        return total;
    }

    public List<String> getAllCountriesAL() {
        if (arrayListObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<String> countries = arrayListObjects.stream()
                .map(n -> n.getCountry())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(countries, (String a, String b) -> a.compareToIgnoreCase(b));

        return countries;
    }

    public List<String> getAllCountriesHS() {
        if (hashSetObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<String> countries = hashSetObjects.stream()
                .map(n -> n.getCountry())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(countries, (String a, String b) -> a.compareToIgnoreCase(b));

        return countries;
    }

    public List<String> getAllCountriesTM() {
        if (treeMapObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<String> countries = treeMapObjects.keySet().stream()
                .map(n -> parseCountry(n))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(countries, (String a, String b) -> a.compareToIgnoreCase(b));

        return countries;
    }

    public List<String> getAllCountriesTMOpt() {
        if (treeMapObjectsOpt.isEmpty())
            return new ArrayList<>();

        ArrayList<String> countries = treeMapObjectsOpt.keySet().stream()
                .map(n -> n.getCountry())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(countries, (String a, String b) -> a.compareToIgnoreCase(b));

        return countries;
    }

    public List<String> getCountriesAL(Week week) {
        if (week == null)
            throw new IllegalArgumentException("The week should not be null");

        if (arrayListObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<String> countries = arrayListObjects.stream()
                .filter(n -> week.equals(new Week(n)))
                .map(n -> n.getCountry())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(countries, (String a, String b) -> a.compareToIgnoreCase(b));

        return countries;
    }

    public List<String> getCountriesHS(Week week) {
        if (week == null)
            throw new IllegalArgumentException("The week should not be null");

        if (hashSetObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<String> countries = hashSetObjects.stream()
                .filter(n -> week.equals(new Week(n)))
                .map(n -> n.getCountry())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(countries, (String a, String b) -> a.compareToIgnoreCase(b));

        return countries;
    }

    public List<String> getCountriesTM(Week week) {
        if (week == null)
            throw new IllegalArgumentException("The week should not be null");

        if (treeMapObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<String> countries = treeMapObjects.keySet().stream()
                .filter(n -> week.equals(parseWeek(n)))
                .map(n -> parseCountry(n))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(countries, (String a, String b) -> a.compareToIgnoreCase(b));

        return countries;
    }

    public List<Integer> getWeeksAL(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (arrayListObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<Integer> weeks = arrayListObjects.stream()
                .filter(n -> n.getCountry().equalsIgnoreCase(country))
                .map(n -> n.getWeek())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(weeks, (a, b) -> a - b);

        return weeks;
    }

    public List<Integer> getWeeksHS(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (hashSetObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<Integer> weeks = hashSetObjects.stream()
                .filter(n -> n.getCountry().equalsIgnoreCase(country))
                .map(n -> n.getWeek())
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(weeks, (a, b) -> a - b);

        return weeks;
    }

    public List<Week> getWeeksTM(String country) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        if (treeMapObjects.isEmpty())
            return new ArrayList<>();

        ArrayList<Week> weeks = treeMapObjects.keySet().stream()
                .filter(n -> parseCountry(n).equalsIgnoreCase(country))
                .map(n -> parseWeek(n))
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.sort(weeks, (a, b) -> a.compareTo(b));

        return weeks;
    }

    @Override
    public String toString() {
        return "CovidDataStore{" +
                "ignoreFailedRecords=" + ignoreFailedRecords +
                ", isDataInitialized=" + isDataInitialized +
                ", invalidRecords=" + invalidRecords +
                ", arrayListObjects=" + arrayListObjects +
                ", hashSetObjects=" + hashSetObjects +
                ", treeMapObjects=" + treeMapObjects +
                '}';
    }
}