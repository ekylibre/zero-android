package ekylibre.zero.inter.model;

import ekylibre.zero.inter.enums.ParamType;

public class ItemWithQuantity {

    public int id;
    public String name;
    @ParamType.Type
    public String type;
    public Float quantity;
    public String unit;

    public ItemWithQuantity(int id, String name, @ParamType.Type String type, Float quantity, String unit) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.unit = unit;
    }

}
