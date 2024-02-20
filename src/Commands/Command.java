package Commands;

public class Command {
    protected CommandType commandType;

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        return "Command{" +
                "commandType=" + commandType +
                '}';
    }
}
