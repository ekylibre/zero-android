package ekylibre.util.query_language;

import java.util.ArrayList;
import java.util.List;

import ekylibre.util.query_language.QL.ParseError;
import ekylibre.util.query_language.QL.QL;
import ekylibre.util.query_language.QL.TreeNode;

public class DSL {

    public static List<String> getElements(String phrase) {

        List<String> elements = new ArrayList<>();

        try {
            TreeNode tree = QL.parse(phrase);
            for (TreeNode node : tree.elements)
                if (node.text != null)
                    elements.add(node.text);
        } catch (ParseError parseError) {
            parseError.printStackTrace();
        }

        return elements;
    }
}
