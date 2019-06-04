package ekylibre.util.ontology;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class Ontology {

    public static Node<String> tree;

    private static Realm realmInstance;

//    public static void build() {
//        try {
//
//            InputStream inputStream = Zero.getContext().getAssets().open("db.xml");
//            new XMLReader(inputStream).execute();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static Node<String> getTree() {
        return tree;
    }

    public static void setTree(Node<String> newTree) {
        tree = newTree;
    }

//    /**
//     * Entry point method to search a node and all parents
//     * @param search he string used for matching
//     */
//    public static List<String> findParents(String search) {
//
//        // Init variables
//        currentPath = new ArrayList<>();
//        searchedNode = null;
//
//        // Run logic
//        walkOnLevelDeep(tree, search);
//
//        return currentPath;
//    }

    /**
     * Entry point method to search a node and all parents
     * @param search he string used for matching
     */
    public static List<String> findParentsInRealm(String search) {

        // Initialize & add leaf node
        List<String> currentPath = new ArrayList<>();
        currentPath.add(search);

        String parent = search;

        if (realmInstance == null)
            realmInstance = Realm.getDefaultInstance();

        while (!parent.equals("product")) {
            RealmNode realmNode = realmInstance.where(RealmNode.class).equalTo("name", parent).findFirst();
            if (realmNode != null) {
                parent = realmNode.parent.name;
                currentPath.add(parent);
            }
        }

        return currentPath;
    }

//    /**
//     * Return on child node if match the name
//     * @param node the tree structure root node
//     * @param search the string used for matching
//     */
//    private static void walkOnLevelDeep(Node<String> node, String search) {
//
//        if (searchedNode == null) {
//
//            // Save the entering node path name
//            currentPath.add(node.getName());
//
//            if (node.getName().equals(search))
//                searchedNode = node;
//            else
//                for (Node<String> child : node.getChildren())
//                    walkOnLevelDeep(child, search);
//
//            if (searchedNode == null)
//                currentPath.remove(currentPath.size() - 1);
//        }
//    }
}

