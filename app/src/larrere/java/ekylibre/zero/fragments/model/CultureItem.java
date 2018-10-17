package ekylibre.zero.fragments.model;

public class CultureItem {
    public int id;
    public String name;
    public String details;
    public boolean is_selected;

    public CultureItem(int id, String name, String details) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.is_selected = false;
    }

    @Override
    public String toString() {
        return name;
    }
}