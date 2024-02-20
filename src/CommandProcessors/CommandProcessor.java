package CommandProcessors;

import Commands.*;
import CovidDataEntities.*;
import java.util.Scanner;

public abstract class CommandProcessor {
    private CovidDataCollectionType collectionType;
    protected CovidDataStore completeData;
    private static Scanner scanner = new Scanner(System.in);

    protected CommandProcessor(CovidDataCollectionType collectionType) {
        this.collectionType = collectionType;
    }

    protected Command readCommand() {
        System.out.println();
        System.out.println("Enter a command, end the program by using \"Quit\":");

        while (scanner.hasNext()) {
            String commandText = scanner.nextLine();

            try {
                return CommandFactory.parseCommand(commandText);
            } catch (IllegalArgumentException ex) {
                System.out.println("Please specify a correct command. The current value causes: " + ex);
            }
        }

        return null;
    }

    public void executeCommands() {
        System.out.println("Guidelines: Enter the following commands:");
        System.out.println("read filename \"<file-name>\"");
        System.out.println("show country \"<country>\"");
        System.out.println("show country week \"<country>\" <year>-<week-number>");
        System.out.println("show week <year>-<week-number>");
        System.out.println("quit");

        Command command = readCommand();

        while (command != null) {
            long startMillis = System.currentTimeMillis();
            System.out.println("Parsed Command: " + command);

            var type = command.getCommandType();

            if (type != CommandType.QUIT && type != CommandType.READ_FILENAME) {
                if (!this.completeData.isDataInitialized()) {
                    System.out.println("The Covid data is not initialized. Please use the Read Filename command at first.");
                    break;
                }
                printCasesHeaderRow();
            }

            executeCommand(command);

            long endMillis = System.currentTimeMillis();
            System.out.println("Elapsed Time (milliseconds): " + (endMillis - startMillis));

            if (type == CommandType.QUIT)
                break;

            command = readCommand();
        }
    }

    protected abstract void executeCommand(Command command);

    protected void readFile(ReadFilenameCommand command) {
        ReadFilenameCommand readFilenameCommand = command;
        this.completeData = new CovidDataStore(true);
        try {
            this.completeData.readFromFile(readFilenameCommand.getFileName());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    protected void printCasesHeaderRow() {
        System.out.println("Country; Year; Week; Number of Cases");
    }

    protected abstract void printCasesRow(String country, Week week);
}
