package CovidDataEntities;

public class TreeData {
    private Integer cases;
    private Integer deaths;
    private String continent;

    public TreeData(Integer cases, Integer deaths, String continent) {
        if (cases < 0)
            throw new IllegalArgumentException("The number of cases should be non-negative.");
        if (deaths < 0)
            throw new IllegalArgumentException("The number of deaths should be non-negative.");
        if (continent == null || continent.isBlank())
            throw new IllegalArgumentException("The continent should not be null, empty, or consist of white-space characters only");

        this.cases = cases;
        this.deaths = deaths;
        this.continent = continent;
    }

    public Integer getCases() {
        return cases;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public String getContinent() {
        return continent;
    }

    @Override
    public String toString() {
        return "CovidDataEntities.TreeData{" +
                "cases=" + cases +
                ", deaths=" + deaths +
                ", continent='" + continent + '\'' +
                '}';
    }
}
