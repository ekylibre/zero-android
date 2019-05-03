package ekylibre.zero.inter.model;

import ekylibre.zero.inter.enums.ParamType;

public class SimpleSelectableItem {

    public int id;
    public String name;
    @ParamType.Type
    public String type;
    public boolean isSelected;

    public SimpleSelectableItem(int id, String name, @ParamType.Type String type) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isSelected = false;
    }

    public SimpleSelectableItem(int id, String name, @ParamType.Type String type, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isSelected = isSelected;
    }

}
