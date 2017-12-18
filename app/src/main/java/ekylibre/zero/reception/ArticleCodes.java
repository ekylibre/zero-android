package ekylibre.zero.reception;

import java.io.Serializable;

/**
 * Created by Asus on 18/12/2017.
 */

public class ArticleCodes implements Serializable {
    private String code;
    private int fk_article;
    private int id;
    private int ek_id;


    public ArticleCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String nature) {
        this.code = code;
    }
}