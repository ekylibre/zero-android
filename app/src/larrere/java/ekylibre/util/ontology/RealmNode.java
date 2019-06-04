package ekylibre.util.ontology;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmNode extends RealmObject {

    @PrimaryKey
    public String name;

    public RealmNode parent;

//    public RealmList<RealmNode> children;

}
