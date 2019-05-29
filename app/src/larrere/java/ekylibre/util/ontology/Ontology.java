package ekylibre.util.ontology;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Ontology {

    private static final String TAG = "Ontology";
    private List<String> currentPath;
    private Node<String> searchedNode = null;

    /**
     * Entry point method to search a node and all parents
     * @param rootNode the tree
     * @param search he string used for matching
     */
    public List<String> findInTree(Node<String> rootNode, String search) {

        currentPath = new ArrayList<>();
        walkOnLevelDeep(rootNode, search);

        Log.e(TAG, "CurrentPath after --> " + currentPath);

        return currentPath;
    }

    /**
     * Return on child node if match the name
     * @param node the tree structure root node
     * @param search the string used for matching
     */
    private void walkOnLevelDeep(Node<String> node, String search) {

        if (searchedNode == null) {
            // Save the entering node path name
            currentPath.add(node.getName());
//            Log.e(TAG, "Current path = " + currentPath);

            if (node.getName().equals(search))
                searchedNode = node;
            else
                for (Node<String> child : node.getChildren())
                    walkOnLevelDeep(child, search);

            if (searchedNode == null)
                currentPath.remove(currentPath.size() - 1);
        }
    }

//    public static void displayTree(Node<String> node) {
//        Log.i("Ontology", node.getName() + "(" + node.getChildren().size() + ")");
//        for (Node<String> child : node.getChildren())
//            displayTree(child);
//    }
//
//    public static void getLowerAbilities(@NonNull String string) {
//
//        Pattern pattern = Pattern.compile("(.*)");
//        Matcher matcher = pattern.matcher(string);
//
//        if (matcher.matches()) {
//            String group = matcher.group();
//            Log.e("Ontology", "group = " + group);
//        }
//    }
}

