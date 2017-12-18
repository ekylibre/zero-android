package ekylibre.zero.reception;

import android.graphics.Picture;

import java.io.Serializable;

/**
 * Created by Asus on 18/12/2017.
 */

public class ArticlePicturesDataModel implements Serializable {
    private byte[] picture;
    private int fk_article;
    private int id;
    private int ek_id;


    public ArticlePicturesDataModel(byte[] picture, int id, ArticleDataModel article) {
        this.picture = picture;
        this.id = id;
        this.fk_article = article.getId();

    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(String nature) {
        this.picture = picture;
    }
}