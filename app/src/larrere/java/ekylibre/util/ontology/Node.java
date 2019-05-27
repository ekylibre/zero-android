package ekylibre.util.ontology;

import java.util.ArrayList;
import java.util.List;


public class Node<String> {

    private String name;
    private List<Node<String>> children = new ArrayList<>();
    private Node<String> parent = null;

    public Node(String name) {
        this.name = name;
    }

    public void addChild(Node<String> child) {
        child.setParent(this);
        this.children.add(child);
//        return child;
    }

    public void addChildren(List<Node<String>> children) {
        for (Node<String> each : children)
            each.setParent(this);
        this.children.addAll(children);
    }

    public List<Node<String>> getChildren() {
        return children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setParent(Node<String> parent) {
        this.parent = parent;
    }

    public Node<String> getParent() {
        return parent;
    }

}
