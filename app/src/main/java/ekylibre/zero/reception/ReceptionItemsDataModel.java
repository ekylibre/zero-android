package ekylibre.zero.reception;

/**
 * Created by Pierre on 18/12/2017.
 */

public class ReceptionItemsDataModel {
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

    public int getFk_article() {
        return fk_article;
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

    public void setEk_id(int ek_id) {
        this.ek_id = ek_id;
    }
}
