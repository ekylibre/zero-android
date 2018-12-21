package ekylibre.zero.inventory;


import java.util.Date;

public class ItemProductInventory {
    public String productName;
    public String productVariant;
    public Date productDate;
    public float productQuantity;
    public String productComment;
    public byte[] productImage;

    public ItemProductInventory(String productName, String productVariant, Date productDate,
                                float productQuantity, String productComment, byte[] productImage) {
        this.productName = productName;
        this.productVariant = productVariant;
        this.productDate = productDate;
        this.productQuantity = productQuantity;
        this.productComment = productComment;
        this.productImage = productImage;
    }

    /*
    public ItemProductInventory(String zone) {
        this.zone = zone;
        this.dateInventory = null;
        this.Icon_zone = null;
    }
*/

    @Override
    public String toString() {
        return productName;
    }

}
