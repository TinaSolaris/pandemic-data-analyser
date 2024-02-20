package CovidDataEntities;

public class InvalidWeeklyData {
    protected int lineNo;
    protected String errorMessage;
    protected String source;

    public InvalidWeeklyData(int lineNo, String errorMessage, String source) {
        this.errorMessage = errorMessage;
        this.lineNo = lineNo;
        this.source = source;
    }

    public int getLineNo( ) {
        return lineNo;
    }
    public String getErrorMessage() {
        return errorMessage;
    }
    public String getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "[CovidDataEntities.InvalidWeeklyData]: " +
                "lineNo=" + lineNo +
                ", errorMessage='" + errorMessage + '\'' +
                ", source='" + source + '\'';
    }
}
