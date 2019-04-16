package ekylibre.zero.inter.model;

import ekylibre.zero.inter.enums.ParamType;

public class SimpleSelectableItem {

    public String name;
    public String label;
    @ParamType.Type
    public String type;
    public boolean isSelected;

    public SimpleSelectableItem(String name, String label, @ParamType.Type String type) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.isSelected = false;
    }

    public SimpleSelectableItem(String name, String label, @ParamType.Type String type, boolean isSelected) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.isSelected = isSelected;
    }

}
