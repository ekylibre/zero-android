package ekylibre.util.ontology;

import android.util.Log;

public class Ontology {

    public static void displayTree(Node<String> node) {
        Log.i("Ontology", node.getName() + "(" + node.getChildren().size() + ")");
        for (Node<String> child : node.getChildren())
            displayTree(child);
    }

//    private List<Node<String>> currentRoute;

//    public List<String> getChildren(Node<String> ontology, String searchString) {
//
//        List<String> response = new ArrayList<>();
//
//        // Level search
//        for (Node<String> node : ontology.getChildren()) {
//
//            if (node.getName().equals(searchString)) {
//                Log.e("OntologyTools", node.getName() + " is a descendent of " + searchString);
//                response.add(searchString);
//                break;
//
//            } else {
//                for (Node<String> child :  node.getChildren())
//                    getChildren(ontology, searchString);
//            }
//        }
//
//        return response;
//    }

//    public List<String> getParents(Node<String> root, String searchString) {
//
//        currentRoute = new ArrayList<>();
//        currentRoute.add(root);
//
//        // Browse all root children horizontally
//        for (Node<String> node : root.getChildren()) {
//            if (node.getName().equals(searchString)) {
//                // Add current node to route history
//                currentRoute.add(node);
//            }
//        }
//
//
//        return parents;
//    }
//
//    private String getParent() {
//
//
//
//
//        return name;
//
//    }

//    private void findChild(String name, String parent, Node<String> baseNode) {
//
//        if (parent.equals(baseNode.getName()))
//            baseNode.addChild(new Node<>(name));
//        else
//            for (Node<String> node :  baseNode.getChildren())
//                findParent(name, parent, node);
//    }
}
