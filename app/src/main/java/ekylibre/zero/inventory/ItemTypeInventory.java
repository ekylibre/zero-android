package ekylibre.zero.inventory;


public class ItemTypeInventory {

    public String type;
    public int Id;
    public boolean is_selected;


    public ItemTypeInventory(String type, int id) {
        this.type = type;
        this.Id=id;
        this.is_selected=false;
    }

    public ItemTypeInventory(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type ;
    }
}


/*

 */