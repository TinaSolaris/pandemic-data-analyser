package CovidDataEntities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeeklyData {
    private final static String DATE_REGEX_DEFINITION = "^(([0-2][0-9]|3[0-1])/(0[0-9]|1[0-2])/(202[0-1]))$";
    private final static Pattern dateExtract = Pattern.compile(DATE_REGEX_DEFINITION);
    private final static DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("dd/MM/uuuu", Locale.ENGLISH)
            .withResolverStyle(ResolverStyle.STRICT); // resolving year-month and day-of-month in the ISO calendar system
    // using strict mode will ensure that the day-of-month is valid for the year-month, rejecting invalid values.

    private final static String NON_NEGATIVE_INT_REGEX_DEFINITION = "^(\\d+)$";
    private final static Pattern nonNegativeIntegerExtract = Pattern.compile(NON_NEGATIVE_INT_REGEX_DEFINITION);

    private final static String WEEK_REGEX_DEFINITION = "^202[0-1]-(\\d{2})$";
    private final static Pattern weekExtract = Pattern.compile(WEEK_REGEX_DEFINITION);

    // With whitespace at the end
    private final static String RATE_REGEX_DEFINITION = "^(0$|^\\d{1,4}.\\d{1,2})\\s*$";
    // Without whitespace at the end
    // private final static String RATE_REGEX_DEFINITION = "^(0$|^\\d{1,4}.\\d{1,2})$";
    private final static Pattern rateExtract = Pattern.compile(RATE_REGEX_DEFINITION);

    // Country Name Examples: https://www.worldometers.info/geography/alphabetical-list-of-countries/
    // Allowed characters: Letters, -, (, ), _, ç (Curaçao), comma, whitespace; starting from an upper case letter
    private final static String GEO_NAME_REGEX_DEFINITION = "^\"*([A-Z][a-zA-Z\\-()_ç, ]+)\"*$";
    private final static Pattern geoNameExtract = Pattern.compile(GEO_NAME_REGEX_DEFINITION);

    // A pair of double quotes with anything except double quotes between them,
    // or a series of characters that does not include a comma
    private final static String CSV_DEFINITION = "\"[^\"]*\"|[^,]+";
    private final static Pattern csvExtract = Pattern.compile(CSV_DEFINITION);

    // Class fields
    private final Integer year;
    private final Integer week;
    private final Integer cases;
    private final Integer deaths;
    private final String country;
    private final String continent;
    private final Double rate;

    // Constructor
    public WeeklyData(String line) throws Exception {
        if (line == null || line.isBlank())
            throw new Exception("The line should not be null, empty, or consist of white-space characters only");

        Matcher matcher = csvExtract.matcher(line);
        String[] values = new String[10];
        int index = 0;
        while (matcher.find()) {
            values[index] = matcher.group(0);
            index++;

            if (index > 10)
                throw new Exception("The line should contain not more than 10 comma-separated values");
        }

        if (index < 10)
            throw new Exception("The line should contain not less than 10 comma-separated values");

        this.year = parseYear(values[0]);
        this.week = parseWeek(values[1]);
        this.cases = parseNonNegativeInteger(values[2]);
        this.deaths = parseNonNegativeInteger(values[3]);
        this.country = parseGeoName(values[4]);
        this.continent = parseGeoName(values[8]);
        this.rate = parseRate(values[9]);
    }

    private Integer parseYear(String value) throws Exception {
        Matcher dateMatcher = dateExtract.matcher(value);
        if (!dateMatcher.find())
            throw new Exception("The value: '" + value + "' does not match the date format: dd/MM/yyyy");

        try {
            LocalDate date = LocalDate.parse(value, dateTimeFormatter);
            return date.getYear();
        }
        catch (Exception ex) {
            throw new Exception("The value: '" + value + "' cannot be parsed as a valid date: dd/MM/yyyy");
        }
    }

    private Integer parseWeek(String value) throws Exception {
        Matcher matcher = weekExtract.matcher(value);
        if (!matcher.find())
            throw new Exception("The value: '" + value + "' does not match the week format: " + WEEK_REGEX_DEFINITION);

        int week;

        try {
            var weekString = matcher.group(1);
            week = Integer.parseInt(weekString);
        }
        catch (Exception ex) {
            throw new Exception("A week cannot be parsed from the value: '" + value + "'");
        }

        if (week > 53)
            throw new Exception("A week number: " + week + " exceeds the maximal week limit per year");

        return week;
    }

    private Integer parseNonNegativeInteger(String value) throws Exception {
        Matcher matcher = nonNegativeIntegerExtract.matcher(value);
        if (!matcher.find())
            throw new Exception("The value: '" + value + "' does not match the non-negative integer number format: " + NON_NEGATIVE_INT_REGEX_DEFINITION);

        try {
            return Integer.parseInt(matcher.group(0));
        }
        catch (Exception ex) {
            throw new Exception("The value: '" + value + "' cannot be parsed as a valid integer number");
        }
    }

    private Double parseRate(String value) throws Exception {
        Matcher matcher = rateExtract.matcher(value);
        if (!matcher.find())
            throw new Exception("The value: '" + value + "' does not match the rate number format: " + RATE_REGEX_DEFINITION);

        try {
            return Double.parseDouble(matcher.group(0));
        }
        catch (Exception ex) {
            throw new Exception("The value: " + value + " cannot be parsed as a valid double number");
        }
    }

    private String parseGeoName(String value) throws Exception {
        Matcher matcher = geoNameExtract.matcher(value);
        if (!matcher.find())
            throw new Exception("The value: '" + value + "' does not match the geo name format: " + GEO_NAME_REGEX_DEFINITION);

        // to count groups because of semicolons
        if (matcher.groupCount() < 1)
            throw new Exception("The value: '" + value + "' does not match the geo name format: " + GEO_NAME_REGEX_DEFINITION);

        return matcher.group(1);
    }

    public Integer getYear() {
        return this.year;
    }

    public Integer getWeek() {
        return this.week;
    }

    public Integer getCases() {
        return this.cases;
    }

    public Integer getDeaths() {
        return this.deaths;
    }

    public String getCountry() {
        return this.country;
    }

    public String getContinent() {
        return this.continent;
    }

    public Double getRate() {
        return this.rate;
    }

    @Override
    public boolean equals(Object ob) {
        if (this == ob)
            return true;

        if (!(ob instanceof WeeklyData))
            return false;

        WeeklyData that = (WeeklyData)ob;
        if (this.hashCode() != that.hashCode())
            return false;

        return Objects.equals(getYear(), that.getYear()) && Objects.equals(getWeek(), that.getWeek()) &&
                Objects.equals(getCases(), that.getCases()) && Objects.equals(getDeaths(), that.getDeaths()) &&
                Objects.equals(getCountry(), that.getCountry()) && Objects.equals(getContinent(), that.getContinent()) &&
                Objects.equals(getRate(), that.getRate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getWeek(), getCases(), getDeaths(), getCountry(), getContinent(), getRate());
    }

    @Override
    public String toString() {
        return "[CovidDataEntities.WeeklyData]: " +
                "year=" + year +
                ", week=" + week +
                ", cases=" + cases +
                ", deaths=" + deaths +
                ", country='" + country + '\'' +
                ", continent='" + continent + '\'' +
                ", rate=" + rate;
    }
}
