package ekylibre.util.pojo;

import androidx.annotation.NonNull;

import ekylibre.util.Helper;

public class SpinnerItem {

    private String label;
    private String value;

    public SpinnerItem(String value) {
        this.value = value;
        this.label = Helper.getTranslation(value);
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    //to display object as a string in spinner
    @NonNull
    @Override
    public String toString() {
        return label;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpinnerItem) {
            SpinnerItem item = (SpinnerItem) obj;
            return item.getValue().equals(value) && item.getLabel().equals(label);
        }
        return false;
    }
}
