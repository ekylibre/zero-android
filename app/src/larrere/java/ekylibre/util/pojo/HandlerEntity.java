package ekylibre.util.pojo;

public class HandlerEntity {

    public String name;
    public String indicator;
    public String unit;

    private HandlerEntity(String name, String indicator, String unit) {
        this.name = name;
        this.indicator = indicator;
        this.unit = unit;
    }

}
