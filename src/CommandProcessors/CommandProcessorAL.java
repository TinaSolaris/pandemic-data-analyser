package CommandProcessors;

import Commands.*;
import CovidDataEntities.Week;

public class CommandProcessorAL extends CommandProcessor {
    public CommandProcessorAL() {
        super(CovidDataCollectionType.ARRAY_LIST);
    }

    @Override
    protected void executeCommand(Command command) {
        switch (command.getCommandType()) {
            case READ_FILENAME -> {
                readFile((ReadFilenameCommand)command);
                break;
            }
            case SHOW_COUNTRY_WEEK -> {
                ShowCountryWeekCommand showCountryWeekCommand = (ShowCountryWeekCommand)command;
                var country = showCountryWeekCommand.getCountry();
                if (!this.completeData.isCountryInFileAL(country))
                    System.out.println("There is no such country in the data file: " + country);
                else
                    printCasesRow(showCountryWeekCommand.getCountry(), showCountryWeekCommand.getWeek());
                break;
            }
            case SHOW_COUNTRY -> {
                ShowCountryCommand showCountryCommand = (ShowCountryCommand)command;
                var country = showCountryCommand.getCountry();
                if (!this.completeData.isCountryInFileAL(country)) {
                    System.out.println("There is no such country in the data file: " + country);
                    break;
                }

                for (int year = 2020; year <= 2021; year++) {
                    for (int week = 1; week <= 53; week++) {
                        printCasesRow(country, new Week(year, week));
                    }
                }
                break;
            }
            case SHOW_WEEK -> {
                ShowWeekCommand showWeekCommand = (ShowWeekCommand)command;
                for (String country : completeData.getAllCountriesAL()) {
                    printCasesRow(country, showWeekCommand.getWeek());
                }
                break;
            }
            // Default is used just in case, to be sure that all enum values are covered
            case default, QUIT -> {
                break;
            }
        }
        System.out.println("Command with ArrayList data executed.");
    }

    protected void printCasesRow(String country, Week week) {
        if (this.completeData.isWeekForCountryAL(country, week))
            System.out.println(country + "; " + week.getYear() + "; " + week.getWeek() + "; " + this.completeData.getCasesAL(country, week));
        else
            System.out.println(country + "; " + week.getYear() + "; " + week.getWeek() + "; N/A");
        // Print N/A (Not Available) if there is no information about this week in exactly this country in the file
    }
}
