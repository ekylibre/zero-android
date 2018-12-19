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

    public ItemZoneInventory(String zone) {
        this.zone = zone;
        this.dateInventory = null;
        this.Icon_zone = null;
    }

    @Override
    public String toString() {
        return dateInventory + ',' + zone ;
    }
}


/*

 */