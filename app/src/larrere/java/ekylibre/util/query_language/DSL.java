package ekylibre.util.query_language;

import java.util.ArrayList;
import java.util.List;

import ekylibre.util.query_language.QL.ParseError;
import ekylibre.util.query_language.QL.QL;
import ekylibre.util.query_language.QL.TreeNode;

public class DSL {

    private static final String TAG = "DSL parser";

    public static List<String> getElements(String phrase) {

        List<String> elements = new ArrayList<>();

        try {
            TreeNode tree = QL.parse(phrase);
            for (TreeNode node : tree.elements) {
                String str = node.text;
                if (!str.equals(" ")
                        && !str.equals("and")
                        && !str.equals("can")
                        && !str.equals("is"))
                    elements.add(str.replace("can ", ""));

            }
        } catch (ParseError parseError) {
            parseError.printStackTrace();
        }

        return elements;
    }
}
