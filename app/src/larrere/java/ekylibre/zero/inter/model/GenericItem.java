package ekylibre.zero.inter.model;

import java.math.BigDecimal;
import java.util.HashMap;

public class GenericItem {

    public int id;
    public String name;
    public String number;
    public String workNumber;
    public String variety;
    public String[] abilities;
    public BigDecimal quantity;
    public String unit;
//    public List<String> referenceName;
    public HashMap<String, String> referenceName;
    public String population;
    public String containerName;
    public String netSurfaceArea;

    public GenericItem() {
        this.quantity = null;
        this.referenceName = new HashMap<>();
    }
}
