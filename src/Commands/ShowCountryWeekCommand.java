package Commands;

import CovidDataEntities.Week;

public class ShowCountryWeekCommand extends Command {
    private String country;
    private Week week;

    public ShowCountryWeekCommand(String country, Week week) {
        super(CommandType.SHOW_COUNTRY_WEEK);

        if (week == null)
            throw new IllegalArgumentException("The week should not be null");
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        this.country = country;
        this.week = week;
    }

    public String getCountry() {
        return country;
    }

    public Week getWeek() {
        return week;
    }

    @Override
    public String toString() {
        return "ShowCountryWeekCommand{" +
                "country='" + country + '\'' +
                ", week=" + week +
                '}';
    }
}
