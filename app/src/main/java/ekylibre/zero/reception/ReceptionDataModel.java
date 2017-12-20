package ekylibre.zero.reception;

import java.io.Serializable;

/**
 * Created by Asus on 18/12/2017.
 */

public class ReceptionDataModel implements Serializable {
    private int id;
    private String received_at;
    private int fk_supplier;
    private String reception_number;
    private int ek_id;


    public ReceptionDataModel(String received_at, String reception_number, SupplierDataModel supplier, int id) {
        this.received_at = received_at;
        this.reception_number = reception_number;
        this.fk_supplier = supplier.getId();
    }

    public ReceptionDataModel(String received_at) {
        this.received_at = received_at;
        this.reception_number = "34564";
        this.fk_supplier = 3;
    }

    public ReceptionDataModel(String received_at, String reception_number, int supplierID) {
        this.received_at = received_at;
        this.reception_number = reception_number;
        this.fk_supplier = supplierID;
    }

    public ReceptionDataModel(String received_at, String reception_number, SupplierDataModel supplier) {
        this.received_at = received_at;
        this.reception_number = reception_number;
        this.fk_supplier = supplier.getId();
    }

    public String getReceived_at() {
        return received_at;
    }

    public void setReceived_at(String received_at) {
        this.received_at = received_at;
    }

    public String getReception_number() {
        return reception_number;
    }

    public void setReception_number(String reception_number) {
        this.reception_number = reception_number;
    }

    public int getId() {
        return id;
    }

    public int getFk_supplier() {
        return fk_supplier;
    }
}
