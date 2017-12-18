package ekylibre.zero.reception;

import android.graphics.Picture;

import java.io.Serializable;

/**
 * Created by Asus on 18/12/2017.
 */

public class ArticlePictures implements Serializable {
    private byte[] picture;
    private int fk_article;
    private int id;
    private int ek_id;


    public ArticlePictures(byte[] picture) {
        this.picture = picture;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(String nature) {
        this.picture = picture;
    }
}