package ekylibre.zero.fragments.model;

public class ActivityItem {
    public int id;
    public String name;
    public String details;
    public boolean is_selected;

    public ActivityItem(int id, String name, String details) {
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