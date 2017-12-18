package ekylibre.zero.reception;

import java.io.Serializable;

/**
 * Created by Asus on 18/12/2017.
 */

public class ArticleCodesDataModel implements Serializable {
    private String code;
    private int fk_article;
    private int id;
    private int ek_id;


    public ArticleCodesDataModel(String code,ArticleDataModel article, int id) {
        this.code = code;
        this.fk_article = article.getId();
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String nature) {
        this.code = code;
    }

    public int getId() {
        return id;
    }


}