package ekylibre.zero.reception;

/**
 * Created by Asus on 18/12/2017.
 */

public class SupplierDataModel {
    private String name;
    private int id;
    private int ek_id;

    public SupplierDataModel( String name, int id) {

        this.name = name;
        this.id = id;

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
