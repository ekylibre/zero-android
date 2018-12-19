package ekylibre.zero.inventory;


import java.util.Date;

public class ItemZoneInventory {
    public String dateInventory;
    public String zone;
    String Icon_zone;

    public ItemZoneInventory(String dateInventory, String zone, String icon_zone) {
        this.dateInventory = dateInventory;
        this.zone = zone;
        Icon_zone = icon_zone;
    }

    @Override
    public String toString() {
        return dateInventory + ',' + zone ;
    }
}


/*

 */