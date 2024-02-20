package Commands;

import CovidDataEntities.Week;

public class ShowWeekCommand extends Command {
    private Week week;

    public ShowWeekCommand(Week week) {
        super(CommandType.SHOW_WEEK);

        if (week == null)
            throw new IllegalArgumentException("The week should not be null");

        this.week = week;
    }

    public Week getWeek() {
        return week;
    }

    @Override
    public String toString() {
        return "ShowWeekCommand{" +
                "week=" + week +
                '}';
    }
}
