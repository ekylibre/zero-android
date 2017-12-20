package ekylibre.zero.reception;

import java.io.Serializable;

/**
 * Created by Asus on 18/12/2017.
 */

public class SupplierDataModel implements Serializable {
    private String name;
    private int id;
    private int ek_id;

    public SupplierDataModel( String name, int ek_id) {

        this.name = name;
        this.ek_id = ek_id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {

        return id;
    }



    public int getEk_id() {
        return ek_id;
    }


}
