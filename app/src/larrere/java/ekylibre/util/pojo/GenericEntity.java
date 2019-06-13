package ekylibre.util.pojo;

import java.util.ArrayList;
import java.util.List;

public class GenericEntity {

    public String name;
    public String filter;
    public String cardinality;
    public String group;
    public List<HandlerEntity> handler;

    public GenericEntity() {
        group = null;
        handler = new ArrayList<>();
    }

}
