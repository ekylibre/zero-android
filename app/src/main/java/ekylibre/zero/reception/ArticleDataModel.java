package ekylibre.zero.reception;

import java.io.Serializable;

/**
 * Created by Asus on 18/12/2017.
 */

public class ArticleDataModel implements Serializable {
    private String nature;
    private String unity;
    private String name;
    private int id;
    private int ek_id;


    public ArticleDataModel(String nature, String unity, String name, int id) {
        this.nature = nature;
        this.unity = unity;
        this.name = name;
        this.id = id;

    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
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


}