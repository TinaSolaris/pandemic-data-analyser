package CommandProcessors;

import Commands.*;
import CovidDataEntities.Week;

public class CommandProcessorTM extends CommandProcessor {
    public CommandProcessorTM() {
        super(CovidDataCollectionType.TREE_MAP);
    }

    @Override
    protected void executeCommand(Command command) {
        switch (command.getCommandType()) {
            case READ_FILENAME -> readFile((ReadFilenameCommand)command);
            case SHOW_COUNTRY_WEEK -> {
                ShowCountryWeekCommand showCountryWeekCommand = (ShowCountryWeekCommand)command;
                var country = showCountryWeekCommand.getCountry();
                if (!this.completeData.isCountryInFileTM(country))
                    System.out.println("There is no such country in the data file: " + country);
                else
                    printCasesRow(country, showCountryWeekCommand.getWeek());
            }
            case SHOW_COUNTRY -> {
                ShowCountryCommand showCountryCommand = (ShowCountryCommand)command;
                var country = showCountryCommand.getCountry();
                if (!this.completeData.isCountryInFileTM(country)) {
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
                for (String country : completeData.getAllCountriesTM()) {
                    printCasesRow(country, showWeekCommand.getWeek());
                }
            }
        }
        System.out.println("Command with TreeMap data executed.");
    }

    protected void printCasesRow(String country, Week week) {
        if (this.completeData.isWeekForCountryTM(country, week))
            System.out.println(country + "; " + week.getYear() + "; " + week.getWeek() + "; " + this.completeData.getCasesTM(country, week));
        else
            System.out.println(country + "; " + week.getYear() + "; " + week.getWeek() + "; N/A");
        // Print N/A (Not Available) if there is no information about this week in exactly this country in the file
    }
}