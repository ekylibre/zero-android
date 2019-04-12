package ekylibre.zero.inter.model;

public class SimpleSelectableItem {

    public String name;
    public String label;
    public boolean isSelected;

    public SimpleSelectableItem(String name, String label) {
        this.name = name;
        this.label = label;
        this.isSelected = false;
    }

    public SimpleSelectableItem(String name, String label, boolean isSelected) {
        this.name = name;
        this.label = label;
        this.isSelected = isSelected;
    }

}
