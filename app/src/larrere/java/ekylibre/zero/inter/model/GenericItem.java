package ekylibre.zero.inter.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GenericItem {

    public int id;
    public String name;
    public String number;
    public String workNumber;
    public String variety;
    public String[] abilities;
    public BigDecimal quantity;
    public String unit;
    public boolean isSelected;
    public List<String> referenceName;
    public BigDecimal population;

    public GenericItem(String variety) {
        this.variety = variety;
        this.referenceName = new ArrayList<>();
    }

    public GenericItem(int id, String name, String number, String workNumber, String variety, String[] abilities, String unit, BigDecimal population) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.workNumber = workNumber;
        this.variety = variety;
        this.abilities = abilities;
        this.quantity = null;
        this.unit = unit;
        this.isSelected = false;
        this.referenceName = new ArrayList<>();
        this.population = population;
    }

    public GenericItem(int id, String name, String number, String workNumber, String variety, String[] abilities, String unit) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.workNumber = workNumber;
        this.variety = variety;
        this.abilities = abilities;
        this.quantity = null;
        this.unit = unit;
        this.isSelected = false;
        this.referenceName = new ArrayList<>();
        this.population = null;
    }
}
