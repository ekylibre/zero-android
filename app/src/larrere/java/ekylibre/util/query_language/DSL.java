package ekylibre.util.query_language;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ekylibre.util.query_language.QL.ParseError;
import ekylibre.util.query_language.QL.QL;
import ekylibre.util.query_language.QL.TreeNode;

public class DSL {

    private static final String TAG = "DSL parser";
    private static List<String> response;


//    public static List<String> computeAbilities(String phrase) {
//
//        response = new ArrayList<>();
//
//        try { TreeNode tree = QL.computeAbilities(phrase);
//
//            for (TreeNode node : tree.elements) {
//                if (!node.text.equals(" ")) {
//                    if (node.elements.size() == 0) {
//                        response.add(node.text);
//                    } else {
//                        response.addAll(getSubElements(node.elements));
//                    }
//                }
//            }
//        } catch (ParseError parseError) {
//            parseError.printStackTrace();
//        }
//
//        Log.i(TAG, "Response -> " + response);
//        return response;
//    }

    private static void getElements(List<TreeNode> elements) {

        for (TreeNode node : elements) {
            String text = node.text;
            if (!text.equals(" ")) {
                if (node.elements.size() == 1) {
                    response.add(text);
                } else if (node.elements.size() == 3) {
                    Log.i(TAG, text + "("+node.elements.size()+")");
                    response.add(text.contains("can ") ? text.replace("can ", "") : text);
                } else {
                    Log.i(TAG, "Multi node text = " + text + "("+node.elements.size()+")");
                    if (node.elements.size() != 0)
                        getElements(node.elements);
                }
            }
        }


//        StringBuilder sb = new StringBuilder();
//        for (TreeNode subnode : elements) {
//            if (!subnode.text.equals(" ")) {
//                sb.append(subnode.text);
//                if (elements.indexOf(subnode) < elements.size() - 1)
//                    sb.append(", ");
//            }
//        }
//        return sb.toString();
    }


    public static List<String> parse(String phrase) {

        response = new ArrayList<>();

        try { TreeNode tree = QL.parse(phrase);
            Log.i(TAG, "Full phrase -> " + tree.text);
            getElements(tree.elements);

//            for (TreeNode node : tree.elements) {
//
//                response.add(node.text);


//                StringBuilder sb = new StringBuilder();
//                sb.append(node.text);
//                if (!node.text.equals(" ")
//                        && !node.text.equals("and")
//                        && !node.text.equals("can")
//                        && !node.text.equals("is")
//                ) {
//                    // Some horrible hacks
//                    for (String str : natures)
//                        if (str.equals(node.text))
//                            node.text = "is " + str;
//                    elements.add(node.text.replace("can ", "").replace("matter", "equipment"));
//                }

//            }
        } catch (ParseError parseError) {
            parseError.printStackTrace();
        }

        return response;
    }
}
