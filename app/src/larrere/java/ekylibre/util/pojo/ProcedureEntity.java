package ekylibre.util.pojo;

import java.util.ArrayList;
import java.util.List;

public class ProcedureEntity {

    public String name;
    public String categories;
    public List<TargetEntity> target;
    public List<InputEntity> input;
    public List<OutputEntity> output;
    public List<GenericEntity> doer;
    public List<GenericEntity> tool;

    private ProcedureEntity(String name, String categories, List<TargetEntity> target, List<InputEntity> input, List<GenericEntity> doer, List<GenericEntity> tool) {
        this.name = name;
        this.categories = categories;
        this.target = target;
        this.input = input;
        this.doer = doer;
        this.tool = tool;
    }

    public ProcedureEntity() {
        this.target = new ArrayList<>();
        this.doer = new ArrayList<>();
        this.tool = new ArrayList<>();
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
    }
}
