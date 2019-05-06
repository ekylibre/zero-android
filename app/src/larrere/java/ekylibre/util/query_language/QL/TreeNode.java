package ekylibre.util.query_language.QL;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TreeNode implements Iterable<TreeNode> {
    public String text;
    public int offset;
    public List<TreeNode> elements;

    Map<Label, TreeNode> labelled;

    public TreeNode() {
        this("", -1, new ArrayList<TreeNode>(0));
    }

    public TreeNode(String text, int offset) {
        this(text, offset, new ArrayList<TreeNode>(0));
    }

    public TreeNode(String text, int offset, List<TreeNode> elements) {
        this.text = text;
        this.offset = offset;
        this.elements = elements;
        this.labelled = new EnumMap<Label, TreeNode>(Label.class);
    }

    public TreeNode get(Label key) {
        return labelled.get(key);
    }

    public Iterator<TreeNode> iterator() {
        return elements.iterator();
    }
}

class TreeNode1 extends TreeNode {
    TreeNode1(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.head, elements.get(0));
        labelled.put(Label.test, elements.get(0));
        labelled.put(Label.spacer, elements.get(3));
        labelled.put(Label.operand, elements.get(4));
        labelled.put(Label.conjonctive, elements.get(4));
    }
}

class TreeNode2 extends TreeNode {
    TreeNode2(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.head, elements.get(0));
        labelled.put(Label.conjonctive, elements.get(0));
        labelled.put(Label.spacer, elements.get(3));
        labelled.put(Label.operand, elements.get(4));
        labelled.put(Label.disjunctive, elements.get(4));
    }
}

class TreeNode3 extends TreeNode {
    TreeNode3(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.boolean_expression, elements.get(1));
    }
}

class TreeNode4 extends TreeNode {
    TreeNode4(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(1));
        labelled.put(Label.variety_name, elements.get(2));
    }
}

class TreeNode5 extends TreeNode {
    TreeNode5(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(1));
        labelled.put(Label.variety_name, elements.get(2));
    }
}

class TreeNode6 extends TreeNode {
    TreeNode6(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(3));
        labelled.put(Label.variety_name, elements.get(4));
    }
}

class TreeNode7 extends TreeNode {
    TreeNode7(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(5));
        labelled.put(Label.variety_name, elements.get(6));
    }
}

class TreeNode8 extends TreeNode {
    TreeNode8(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(1));
        labelled.put(Label.variety_name, elements.get(2));
    }
}

class TreeNode9 extends TreeNode {
    TreeNode9(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.indicator_filter, elements.get(1));
        labelled.put(Label.spacer, elements.get(4));
        labelled.put(Label.indicator_name, elements.get(5));
    }
}

class TreeNode10 extends TreeNode {
    TreeNode10(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(0));
        labelled.put(Label.mode, elements.get(1));
    }
}

class TreeNode11 extends TreeNode {
    TreeNode11(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(1));
        labelled.put(Label.ability, elements.get(2));
    }
}

class TreeNode12 extends TreeNode {
    TreeNode12(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.ability_name, elements.get(0));
        labelled.put(Label.ability_parameters, elements.get(1));
    }
}

class TreeNode13 extends TreeNode {
    TreeNode13(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.parameters, elements.get(2));
    }
}

class TreeNode14 extends TreeNode {
    TreeNode14(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.first_parameter, elements.get(0));
        labelled.put(Label.ability_argument, elements.get(0));
        labelled.put(Label.other_parameters, elements.get(1));
    }
}

class TreeNode15 extends TreeNode {
    TreeNode15(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.parameter, elements.get(3));
        labelled.put(Label.ability_argument, elements.get(3));
    }
}

class TreeNode16 extends TreeNode {
    TreeNode16(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.first_ability, elements.get(0));
        labelled.put(Label.ability, elements.get(0));
        labelled.put(Label.other_abilities, elements.get(1));
    }
}

class TreeNode17 extends TreeNode {
    TreeNode17(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.ability, elements.get(3));
    }
}

class TreeNode18 extends TreeNode {
    TreeNode18(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.parameters, elements.get(2));
    }
}

class TreeNode19 extends TreeNode {
    TreeNode19(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.first_parameter, elements.get(0));
        labelled.put(Label.ability_argument, elements.get(0));
        labelled.put(Label.other_parameters, elements.get(1));
    }
}

class TreeNode20 extends TreeNode {
    TreeNode20(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.parameter, elements.get(3));
        labelled.put(Label.ability_argument, elements.get(3));
    }
}

class TreeNode21 extends TreeNode {
    TreeNode21(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.negated_test, elements.get(1));
    }
}

class TreeNode22 extends TreeNode {
    TreeNode22(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(0));
        labelled.put(Label.ability, elements.get(1));
    }
}

class TreeNode23 extends TreeNode {
    TreeNode23(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(0));
        labelled.put(Label.essence, elements.get(1));
    }
}

class TreeNode24 extends TreeNode {
    TreeNode24(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.spacer, elements.get(0));
        labelled.put(Label.derivative, elements.get(1));
    }
}

class TreeNode25 extends TreeNode {
    TreeNode25(String text, int offset, List<TreeNode> elements) {
        super(text, offset, elements);
        labelled.put(Label.boolean_expression, elements.get(1));
    }
}
