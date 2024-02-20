package Commands;

public class QuitCommand extends Command {
    public QuitCommand() {
        super(CommandType.QUIT);
    }

    @Override
    public String toString() {
        return "QuitCommand{}";
    }
}