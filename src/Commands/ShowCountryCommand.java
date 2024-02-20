package Commands;

public class ShowCountryCommand extends Command {
    private String country;

    public ShowCountryCommand(String country) {
        super(CommandType.SHOW_COUNTRY);

        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");

        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "ShowCountryCommand{" +
                "country='" + country + '\'' +
                '}';
    }
}
