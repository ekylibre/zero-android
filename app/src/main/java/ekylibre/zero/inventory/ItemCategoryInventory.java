package ekylibre.zero.inventory;


import android.widget.Switch;

public class ItemCategoryInventory {

    public String category;
    public int Id;
    public boolean is_selected;


    public ItemCategoryInventory(String category, int id) {
        this.category = category;
        this.Id=id;
        this.is_selected=false;
    }

    public ItemCategoryInventory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return category ;
    }
}


/*

 */