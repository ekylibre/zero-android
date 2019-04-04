package ekylibre.util.pojo;

import android.content.Context;

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

    public ProcedureEntity() {
        this.target = new ArrayList<>();
        this.doer = new ArrayList<>();
        this.tool = new ArrayList<>();
        this.input = new ArrayList<>();
        this.output = new ArrayList<>();
    }

    public int getTranslationId(Context context) {
        return context.getResources().getIdentifier(this.name, "string", context.getPackageName());
    }
}
