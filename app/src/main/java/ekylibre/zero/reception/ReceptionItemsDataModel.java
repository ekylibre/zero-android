package ekylibre.zero.reception;

import java.io.Serializable;

/**
 * Created by Pierre on 18/12/2017.
 */

public class ReceptionItemsDataModel implements Serializable {
    private int id;
    private int fk_reception ;
    private int fk_article;
    private double quantity ;
    private int ek_id;

    public ReceptionItemsDataModel(int id, ReceptionDataModel reception, ArticleDataModel article, double quantity, int ek_id) {
        this.id = id;
        this.fk_reception = reception.getId() ;
        this.fk_article = article.getId();
        this.quantity = quantity ;
        this.ek_id = ek_id;
    }

    public int getId() {

        return id;
    }

    public int getFk_reception() {
        return fk_reception;
    }



    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getEk_id() {
        return ek_id;
    }


}
