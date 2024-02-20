package CovidDataEntities;

import java.util.Objects;

public class TreeDataKey implements Comparable {
    private String country;
    private Week week;

    public TreeDataKey(String country, Week week) {
        if (country == null || country.isBlank())
            throw new IllegalArgumentException("The country should not be null, empty, or consist of white-space characters only");
        if (week == null)
            throw new IllegalArgumentException("The week should not be null.");

        this.country = country;
        this.week = week;
    }

    public String getCountry() {
        return country;
    }

    public Week getWeek() {
        return week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TreeDataKey))
            return false;

        TreeDataKey that = (TreeDataKey)o;

        return  Objects.equals(this.getCountry(), that.getCountry()) &&
                Objects.equals(this.getWeek(), that.getWeek());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getWeek());
    }

    @Override
    public String toString() {
        return "TreeDataKey{" +
                "country='" + country + '\'' +
                ", week=" + week +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (this == o)
            return 0;
        if (!(o instanceof TreeDataKey))
            return -1;

        TreeDataKey that = (TreeDataKey)o;

        int result;
        if (this.getCountry().equals(that.getCountry()))
            result = this.getWeek().compareTo(that.getWeek());
        else
            result = this.getCountry().compareTo(that.getCountry());

        return result;
    }
}
