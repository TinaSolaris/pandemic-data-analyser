import CommandProcessors.*;

public class Main {
    /** Test Commands:
     read filename "F:\Path\ecdc_covid19_cases.csv"
     show country "Afghanistan"
     show country "Albania"
     show country week "Afghanistan" 2020-10
     show week 2020-10
     quit */

    public static void main(String[] args) {
        try {
            // Change to a desired type of CommandProcessor when needed
            CommandProcessor commandProcessor = new CommandProcessorAL();
            commandProcessor.executeCommands();
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
