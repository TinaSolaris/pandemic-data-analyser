package CovidDataEntities;

import java.util.Objects;

public class Week implements Comparable {
    private Integer year;
    private Integer week;

    public Week(Integer year, Integer week) {
        if (year < 2020 || year > 2021)
            throw new IllegalArgumentException("The year should be from 2020 to 2021.");
        if (week < 1 || week > 53)
            throw new IllegalArgumentException("The week should be from 1 to 53.");

        this.year = year;
        this.week = week;
    }

    public Week(WeeklyDataProper data) {
        if (data == null)
            throw new IllegalArgumentException("The data should not be null.");
        if (data.getYear() < 2020 || data.getYear() > 2021)
            throw new IllegalArgumentException("The year should be from 2020 to 2021.");
        if (data.getWeek() < 1 || data.getWeek() > 53)
            throw new IllegalArgumentException("The week should be from 1 to 53.");

        this.year = data.getYear();
        this.week = data.getWeek();
    }

    public Integer getYear() {
        return year;
    }

    public Integer getWeek() {
        return week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Week))
            return false;

        Week that = (Week)o;

        return  Objects.equals(getYear(), that.getYear()) &&
                Objects.equals(getWeek(), that.getWeek());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getYear(), getWeek());
    }

    @Override
    public String toString() {
        return "Week{" +
                "year='" + year + '\'' +
                ", week=" + week +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (this == o)
            return 0;
        if (!(o instanceof Week))
            return -1;

        Week that = (Week)o;

        int result;
        if (this.getYear().equals(that.getYear()))
            result = this.getWeek().compareTo(that.getWeek());
        else
            result = this.getYear().compareTo(that.getYear());

        return result;
    }
}