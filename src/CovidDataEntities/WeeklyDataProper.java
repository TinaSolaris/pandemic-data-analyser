package CovidDataEntities;

import java.util.Objects;

public class WeeklyDataProper extends WeeklyData {
    public WeeklyDataProper(String line) throws Exception {
        super(line);
    }

    @Override
    public boolean equals(Object ob) {
        if (this == ob)
            return true;

        if (!(ob instanceof WeeklyDataProper))
            return false;

        WeeklyDataProper that = (WeeklyDataProper)ob;
        if (this.hashCode() != that.hashCode())
            return false;

        // The year is not included as it is not mentioned in the requirements.
        // But it should be there because it is a part of the key.
        return  Objects.equals(getYear(), that.getYear()) &&
                Objects.equals(getWeek(), that.getWeek()) &&
                Objects.equals(getCountry(), that.getCountry());
    }

    @Override
    public int hashCode() {
        // The year is not included as it is not mentioned in the requirements.
        // But it should be there because it is a part of the key.
        return Objects.hash(getCountry(), getYear(), getWeek());
    }

    @Override
    public String toString() {
        return super.toString() +
                ", hashCode=" + hashCode();
    }
}
