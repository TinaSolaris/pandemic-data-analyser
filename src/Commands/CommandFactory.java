package Commands;

import CovidDataEntities.Week;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandFactory {
    private final static Pattern readFilename = Pattern.compile("^\\s*Read\\s+filename\\s+\"(?<filename>[^\"]*)\"\\s*$", Pattern.CASE_INSENSITIVE);
    private final static Pattern showCountryWeek = Pattern.compile("^\\s*Show\\s+country\\s+week\\s+\"(?<country>[^\"]*)\"\\s+(?<year>\\d{4})-(?<week>\\d{1,2})\\s*$", Pattern.CASE_INSENSITIVE);
    private final static Pattern showCountry = Pattern.compile("^\\s*Show\\s+country\\s+\"(?<country>[^\"]*)\"\\s*$", Pattern.CASE_INSENSITIVE);
    private final static Pattern showWeek = Pattern.compile("^\\s*Show\\s+week\\s+(?<year>\\d{4})-(?<week>\\d{1,2})\\s*$", Pattern.CASE_INSENSITIVE);
    private final static Pattern quit = Pattern.compile("^\\s*Quit\\s*$", Pattern.CASE_INSENSITIVE);

    public static Command parseCommand(String value) throws IllegalArgumentException {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("The value should not be null, empty, or consist of white-space characters only");

        Matcher matcher = readFilename.matcher(value);
        if (matcher.find()) {
            String fileName = matcher.group("filename");
            return new ReadFilenameCommand(fileName);
        }

        matcher = showCountryWeek.matcher(value);
        if (matcher.find()) {
            try {
                int year = Integer.parseInt(matcher.group("year"));
                int weekNo = Integer.parseInt(matcher.group("week"));
                return new ShowCountryWeekCommand(matcher.group("country"), new Week(year, weekNo));
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse a valid year and week from the value: " + value);
            }
        }

        matcher = showCountry.matcher(value);
        if (matcher.find())
            return new ShowCountryCommand(matcher.group("country"));

        matcher = showWeek.matcher(value);
        if (matcher.find()) {
            try {
                int year = Integer.parseInt(matcher.group("year"));
                int weekNo = Integer.parseInt(matcher.group("week"));
                return new ShowWeekCommand(new Week(year, weekNo));
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse a valid year and week from the value: " + value);
            }
        }

        matcher = quit.matcher(value);
        if (matcher.find())
            return new QuitCommand();

        throw new IllegalArgumentException("Cannot parse any command from the value: " + value);
    }
}
