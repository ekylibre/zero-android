package ekylibre.zero.inventory;


public class ItemZoneInventory {
    public String dateInventory;
    public String zone;
    byte [] Icon_zone;

    public ItemZoneInventory(String dateInventory, String zone, byte[] icon_zone) {
        this.dateInventory = dateInventory;
        this.zone = zone;
        Icon_zone = icon_zone;
    }

    @Override
    public String toString() {
        return dateInventory + ',' + zone ;
    }
}
