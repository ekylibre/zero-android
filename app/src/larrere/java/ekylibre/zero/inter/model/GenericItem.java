package ekylibre.zero.inter.model;

import ekylibre.zero.inter.enums.ParamType;

public class GenericItem {

    public int id;
    public String name;
    public String number;
    @ParamType.Type
    public String type;
    public String[] abilities;
    public String unit;
    public boolean isSelected;

    public GenericItem(int id, String name, String number, @ParamType.Type String type, String[] abilities, String unit) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.type = type;
        this.abilities = abilities;
        this.unit = unit;
        this.isSelected = false;
    }
}
