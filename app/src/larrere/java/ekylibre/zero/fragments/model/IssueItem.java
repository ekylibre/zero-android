package ekylibre.zero.fragments.model;

public class IssueItem {
    public String category;
    public String label;
    public String nature;
    public boolean is_selected;

    public IssueItem(String category, String label, String nature) {
        this.category = category;
        this.label = label;
        this.nature = nature;
        this.is_selected = false;
    }

    public IssueItem(String category, String label, String nature, boolean is_selected) {
        this.category = category;
        this.label = label;
        this.nature = nature;
        this.is_selected = is_selected;
    }
}