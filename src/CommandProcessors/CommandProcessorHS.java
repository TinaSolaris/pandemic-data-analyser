package CommandProcessors;

import Commands.*;
import CovidDataEntities.Week;

public class CommandProcessorHS extends CommandProcessor {
    public CommandProcessorHS() {
        super(CovidDataCollectionType.HASH_SET);
    }

    @Override
    protected void executeCommand(Command command) {
        switch (command.getCommandType()) {
            case READ_FILENAME -> readFile((ReadFilenameCommand)command);
            case SHOW_COUNTRY_WEEK -> {
                ShowCountryWeekCommand showCountryWeekCommand = (ShowCountryWeekCommand)command;
                var country = showCountryWeekCommand.getCountry();
                if (!this.completeData.isCountryInFileHS(country))
                    System.out.println("There is no such country in the data file: " + country);
                else
                    printCasesRow(showCountryWeekCommand.getCountry(), showCountryWeekCommand.getWeek());
            }
            case SHOW_COUNTRY -> {
                ShowCountryCommand showCountryCommand = (ShowCountryCommand)command;
                var country = showCountryCommand.getCountry();
                if (!this.completeData.isCountryInFileHS(country)) {
                    System.out.println("There is no such country in the data file: " + country);
                    break;
                }

                for (int year = 2020; year <= 2021; year++) {
                    for (int week = 1; week <= 53; week++) {
                        printCasesRow(country, new Week(year, week));
                    }
                }
            }
            case SHOW_WEEK -> {
                ShowWeekCommand showWeekCommand = (ShowWeekCommand)command;
                for (String country : completeData.getAllCountriesHS()) {
                    printCasesRow(country, showWeekCommand.getWeek());
                }
            }
        }
        System.out.println("Command with HashSet data executed.");
    }

    protected void printCasesRow(String country, Week week) {
        if (this.completeData.isWeekForCountryHS(country, week))
            System.out.println(country + "; " + week.getYear() + "; " + week.getWeek() + "; " + this.completeData.getCasesHS(country, week));
        else
            System.out.println(country + "; " + week.getYear() + "; " + week.getWeek() + "; N/A");
        // Print N/A (Not Available) if there is no information about this week in exactly this country in the file
    }
}
