package ekylibre.util.pojo;

import java.util.List;

public class InputEntity {

    public String name;
    public String filter;
    public String displayStatus;
    public String cardinality;
    public List<HandlerEntity> handler;

    private InputEntity(String name, String filter, String displayStatus, String cardinality, List<HandlerEntity> handler) {
        this.name = name;
        this.filter = filter;
        this.displayStatus = displayStatus;
        this.cardinality = cardinality;
        this.handler = handler;
    }

}
