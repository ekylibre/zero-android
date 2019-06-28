package ekylibre.util.pojo;

import androidx.annotation.NonNull;

import java.util.List;

import ekylibre.util.Helper;

public class SpinnerItem {

    private String label;
    private String value;
    private String indicator;

    public SpinnerItem(String value, String indicator) {
        this.value = value;
        this.label = Helper.getTranslation(value);
        this.indicator = indicator;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public String getIndicator() {
        return indicator;
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

    public static int getIndex(List<SpinnerItem> list, String indicator) {
        for (SpinnerItem item : list) {
            if (item.indicator.equals(indicator))
                return list.indexOf(item);
        }
        return 0;
    }
}
