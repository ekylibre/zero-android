package ekylibre.zero.inventory;


import android.widget.Switch;

public class ItemCategoryInventory {

    public String category;
    String Icon_category;
    boolean is_selected;


    public ItemCategoryInventory(String category, String icon_category) {
        this.category = category;
        Icon_category=icon_category;


    }

    public ItemCategoryInventory(String category) {
        this.category = category;
        this.Icon_category=null;


    }

    @Override
    public String toString() {
        return category ;
    }
}


/*

 */