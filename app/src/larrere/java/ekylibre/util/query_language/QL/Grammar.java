package ekylibre.util.query_language.QL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

abstract class Grammar {
    static TreeNode FAILURE = new TreeNode();

    int inputSize, offset, failure;
    String input;
    List<String> expected;
    Map<Label, Map<Integer, CacheRecord>> cache;
    Actions actions;

    private static Pattern REGEX_1 = Pattern.compile("\\A[a-z]");
    private static Pattern REGEX_2 = Pattern.compile("\\A[a-z0-9]");
    private static Pattern REGEX_3 = Pattern.compile("\\A[a-z0-9]");
    private static Pattern REGEX_4 = Pattern.compile("\\A[a-z]");
    private static Pattern REGEX_5 = Pattern.compile("\\A[a-z0-9]");
    private static Pattern REGEX_6 = Pattern.compile("\\A[a-z0-9]");
    private static Pattern REGEX_7 = Pattern.compile("\\A[a-z]");
    private static Pattern REGEX_8 = Pattern.compile("\\A[a-z0-9]");
    private static Pattern REGEX_9 = Pattern.compile("\\A[a-z0-9]");
    private static Pattern REGEX_10 = Pattern.compile("\\A[a-z]");
    private static Pattern REGEX_11 = Pattern.compile("\\A[a-z0-9]");
    private static Pattern REGEX_12 = Pattern.compile("\\A[a-z0-9]");
    private static Pattern REGEX_13 = Pattern.compile("\\A[\\s\\n]");

    TreeNode _read_boolean_expression() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.boolean_expression);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.boolean_expression, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            address0 = _read_disjunctive();
            if (address0 == FAILURE) {
                offset = index1;
                address0 = _read_test();
                if (address0 == FAILURE) {
                    offset = index1;
                }
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_conjonctive() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.conjonctive);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.conjonctive, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            int index2 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(5);
            TreeNode address1 = FAILURE;
            address1 = _read_test();
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_spacer();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    String chunk0 = null;
                    if (offset < inputSize) {
                        chunk0 = input.substring(offset, offset + 3);
                    }
                    if (chunk0 != null && chunk0.equals("and")) {
                        address3 = new TreeNode(input.substring(offset, offset + 3), offset);
                        offset = offset + 3;
                    } else {
                        address3 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("\"and\"");
                        }
                    }
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                        TreeNode address4 = FAILURE;
                        address4 = _read_spacer();
                        if (address4 != FAILURE) {
                            elements0.add(3, address4);
                            TreeNode address5 = FAILURE;
                            address5 = _read_conjonctive();
                            if (address5 != FAILURE) {
                                elements0.add(4, address5);
                            } else {
                                elements0 = null;
                                offset = index2;
                            }
                        } else {
                            elements0 = null;
                            offset = index2;
                        }
                    } else {
                        elements0 = null;
                        offset = index2;
                    }
                } else {
                    elements0 = null;
                    offset = index2;
                }
            } else {
                elements0 = null;
                offset = index2;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode1(input.substring(index2, offset), index2, elements0);
                offset = offset;
            }
            if (address0 == FAILURE) {
                offset = index1;
                address0 = _read_test();
                if (address0 == FAILURE) {
                    offset = index1;
                }
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_disjunctive() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.disjunctive);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.disjunctive, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            int index2 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(5);
            TreeNode address1 = FAILURE;
            address1 = _read_conjonctive();
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_spacer();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    String chunk0 = null;
                    if (offset < inputSize) {
                        chunk0 = input.substring(offset, offset + 2);
                    }
                    if (chunk0 != null && chunk0.equals("or")) {
                        address3 = new TreeNode(input.substring(offset, offset + 2), offset);
                        offset = offset + 2;
                    } else {
                        address3 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("\"or\"");
                        }
                    }
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                        TreeNode address4 = FAILURE;
                        address4 = _read_spacer();
                        if (address4 != FAILURE) {
                            elements0.add(3, address4);
                            TreeNode address5 = FAILURE;
                            address5 = _read_disjunctive();
                            if (address5 != FAILURE) {
                                elements0.add(4, address5);
                            } else {
                                elements0 = null;
                                offset = index2;
                            }
                        } else {
                            elements0 = null;
                            offset = index2;
                        }
                    } else {
                        elements0 = null;
                        offset = index2;
                    }
                } else {
                    elements0 = null;
                    offset = index2;
                }
            } else {
                elements0 = null;
                offset = index2;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode2(input.substring(index2, offset), index2, elements0);
                offset = offset;
            }
            if (address0 == FAILURE) {
                offset = index1;
                address0 = _read_conjonctive();
                if (address0 == FAILURE) {
                    offset = index1;
                }
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_test() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.test);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.test, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            address0 = _read_abilitive();
            if (address0 == FAILURE) {
                offset = index1;
                address0 = _read_essence();
                if (address0 == FAILURE) {
                    offset = index1;
                    address0 = _read_non_essence();
                    if (address0 == FAILURE) {
                        offset = index1;
                        address0 = _read_derivative();
                        if (address0 == FAILURE) {
                            offset = index1;
                            address0 = _read_non_derivative();
                            if (address0 == FAILURE) {
                                offset = index1;
                                address0 = _read_inclusion();
                                if (address0 == FAILURE) {
                                    offset = index1;
                                    address0 = _read_indicative();
                                    if (address0 == FAILURE) {
                                        offset = index1;
                                        address0 = _read_negative();
                                        if (address0 == FAILURE) {
                                            offset = index1;
                                            int index2 = offset;
                                            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
                                            TreeNode address1 = FAILURE;
                                            String chunk0 = null;
                                            if (offset < inputSize) {
                                                chunk0 = input.substring(offset, offset + 1);
                                            }
                                            if (chunk0 != null && chunk0.equals("(")) {
                                                address1 = new TreeNode(input.substring(offset, offset + 1), offset);
                                                offset = offset + 1;
                                            } else {
                                                address1 = FAILURE;
                                                if (offset > failure) {
                                                    failure = offset;
                                                    expected = new ArrayList<String>();
                                                }
                                                if (offset == failure) {
                                                    expected.add("\"(\"");
                                                }
                                            }
                                            if (address1 != FAILURE) {
                                                elements0.add(0, address1);
                                                TreeNode address2 = FAILURE;
                                                address2 = _read_boolean_expression();
                                                if (address2 != FAILURE) {
                                                    elements0.add(1, address2);
                                                    TreeNode address3 = FAILURE;
                                                    String chunk1 = null;
                                                    if (offset < inputSize) {
                                                        chunk1 = input.substring(offset, offset + 1);
                                                    }
                                                    if (chunk1 != null && chunk1.equals(")")) {
                                                        address3 = new TreeNode(input.substring(offset, offset + 1), offset);
                                                        offset = offset + 1;
                                                    } else {
                                                        address3 = FAILURE;
                                                        if (offset > failure) {
                                                            failure = offset;
                                                            expected = new ArrayList<String>();
                                                        }
                                                        if (offset == failure) {
                                                            expected.add("\")\"");
                                                        }
                                                    }
                                                    if (address3 != FAILURE) {
                                                        elements0.add(2, address3);
                                                    } else {
                                                        elements0 = null;
                                                        offset = index2;
                                                    }
                                                } else {
                                                    elements0 = null;
                                                    offset = index2;
                                                }
                                            } else {
                                                elements0 = null;
                                                offset = index2;
                                            }
                                            if (elements0 == null) {
                                                address0 = FAILURE;
                                            } else {
                                                address0 = new TreeNode3(input.substring(index2, offset), index2, elements0);
                                                offset = offset;
                                            }
                                            if (address0 == FAILURE) {
                                                offset = index1;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_essence() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.essence);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.essence, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 2);
            }
            if (chunk0 != null && chunk0.equals("is")) {
                address1 = new TreeNode(input.substring(offset, offset + 2), offset);
                offset = offset + 2;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"is\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_spacer();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    address3 = _read_variety_name();
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode4(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_non_essence() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.non_essence);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.non_essence, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 4);
            }
            if (chunk0 != null && chunk0.equals("isnt")) {
                address1 = new TreeNode(input.substring(offset, offset + 4), offset);
                offset = offset + 4;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"isnt\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_spacer();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    address3 = _read_variety_name();
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode5(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_derivative() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.derivative);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.derivative, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(5);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 7);
            }
            if (chunk0 != null && chunk0.equals("derives")) {
                address1 = new TreeNode(input.substring(offset, offset + 7), offset);
                offset = offset + 7;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"derives\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_spacer();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    String chunk1 = null;
                    if (offset < inputSize) {
                        chunk1 = input.substring(offset, offset + 4);
                    }
                    if (chunk1 != null && chunk1.equals("from")) {
                        address3 = new TreeNode(input.substring(offset, offset + 4), offset);
                        offset = offset + 4;
                    } else {
                        address3 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("\"from\"");
                        }
                    }
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                        TreeNode address4 = FAILURE;
                        address4 = _read_spacer();
                        if (address4 != FAILURE) {
                            elements0.add(3, address4);
                            TreeNode address5 = FAILURE;
                            address5 = _read_variety_name();
                            if (address5 != FAILURE) {
                                elements0.add(4, address5);
                            } else {
                                elements0 = null;
                                offset = index1;
                            }
                        } else {
                            elements0 = null;
                            offset = index1;
                        }
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode6(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_non_derivative() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.non_derivative);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.non_derivative, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(7);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 4);
            }
            if (chunk0 != null && chunk0.equals("dont")) {
                address1 = new TreeNode(input.substring(offset, offset + 4), offset);
                offset = offset + 4;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"dont\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_spacer();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    String chunk1 = null;
                    if (offset < inputSize) {
                        chunk1 = input.substring(offset, offset + 6);
                    }
                    if (chunk1 != null && chunk1.equals("derive")) {
                        address3 = new TreeNode(input.substring(offset, offset + 6), offset);
                        offset = offset + 6;
                    } else {
                        address3 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("\"derive\"");
                        }
                    }
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                        TreeNode address4 = FAILURE;
                        address4 = _read_spacer();
                        if (address4 != FAILURE) {
                            elements0.add(3, address4);
                            TreeNode address5 = FAILURE;
                            String chunk2 = null;
                            if (offset < inputSize) {
                                chunk2 = input.substring(offset, offset + 4);
                            }
                            if (chunk2 != null && chunk2.equals("from")) {
                                address5 = new TreeNode(input.substring(offset, offset + 4), offset);
                                offset = offset + 4;
                            } else {
                                address5 = FAILURE;
                                if (offset > failure) {
                                    failure = offset;
                                    expected = new ArrayList<String>();
                                }
                                if (offset == failure) {
                                    expected.add("\"from\"");
                                }
                            }
                            if (address5 != FAILURE) {
                                elements0.add(4, address5);
                                TreeNode address6 = FAILURE;
                                address6 = _read_spacer();
                                if (address6 != FAILURE) {
                                    elements0.add(5, address6);
                                    TreeNode address7 = FAILURE;
                                    address7 = _read_variety_name();
                                    if (address7 != FAILURE) {
                                        elements0.add(6, address7);
                                    } else {
                                        elements0 = null;
                                        offset = index1;
                                    }
                                } else {
                                    elements0 = null;
                                    offset = index1;
                                }
                            } else {
                                elements0 = null;
                                offset = index1;
                            }
                        } else {
                            elements0 = null;
                            offset = index1;
                        }
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode7(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_inclusion() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.inclusion);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.inclusion, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 8);
            }
            if (chunk0 != null && chunk0.equals("includes")) {
                address1 = new TreeNode(input.substring(offset, offset + 8), offset);
                offset = offset + 8;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"includes\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_spacer();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    address3 = _read_variety_name();
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode8(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_indicative() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.indicative);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.indicative, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(6);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 3);
            }
            if (chunk0 != null && chunk0.equals("has")) {
                address1 = new TreeNode(input.substring(offset, offset + 3), offset);
                offset = offset + 3;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"has\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                int index2 = offset;
                int index3 = offset;
                List<TreeNode> elements1 = new ArrayList<TreeNode>(2);
                TreeNode address3 = FAILURE;
                address3 = _read_spacer();
                if (address3 != FAILURE) {
                    elements1.add(0, address3);
                    TreeNode address4 = FAILURE;
                    int index4 = offset;
                    String chunk1 = null;
                    if (offset < inputSize) {
                        chunk1 = input.substring(offset, offset + 6);
                    }
                    if (chunk1 != null && chunk1.equals("frozen")) {
                        address4 = new TreeNode(input.substring(offset, offset + 6), offset);
                        offset = offset + 6;
                    } else {
                        address4 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("\"frozen\"");
                        }
                    }
                    if (address4 == FAILURE) {
                        offset = index4;
                        String chunk2 = null;
                        if (offset < inputSize) {
                            chunk2 = input.substring(offset, offset + 8);
                        }
                        if (chunk2 != null && chunk2.equals("variable")) {
                            address4 = new TreeNode(input.substring(offset, offset + 8), offset);
                            offset = offset + 8;
                        } else {
                            address4 = FAILURE;
                            if (offset > failure) {
                                failure = offset;
                                expected = new ArrayList<String>();
                            }
                            if (offset == failure) {
                                expected.add("\"variable\"");
                            }
                        }
                        if (address4 == FAILURE) {
                            offset = index4;
                        }
                    }
                    if (address4 != FAILURE) {
                        elements1.add(1, address4);
                    } else {
                        elements1 = null;
                        offset = index3;
                    }
                } else {
                    elements1 = null;
                    offset = index3;
                }
                if (elements1 == null) {
                    address2 = FAILURE;
                } else {
                    address2 = new TreeNode10(input.substring(index3, offset), index3, elements1);
                    offset = offset;
                }
                if (address2 == FAILURE) {
                    address2 = new TreeNode(input.substring(index2, index2), index2);
                    offset = index2;
                }
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address5 = FAILURE;
                    address5 = _read_spacer();
                    if (address5 != FAILURE) {
                        elements0.add(2, address5);
                        TreeNode address6 = FAILURE;
                        String chunk3 = null;
                        if (offset < inputSize) {
                            chunk3 = input.substring(offset, offset + 9);
                        }
                        if (chunk3 != null && chunk3.equals("indicator")) {
                            address6 = new TreeNode(input.substring(offset, offset + 9), offset);
                            offset = offset + 9;
                        } else {
                            address6 = FAILURE;
                            if (offset > failure) {
                                failure = offset;
                                expected = new ArrayList<String>();
                            }
                            if (offset == failure) {
                                expected.add("\"indicator\"");
                            }
                        }
                        if (address6 != FAILURE) {
                            elements0.add(3, address6);
                            TreeNode address7 = FAILURE;
                            address7 = _read_spacer();
                            if (address7 != FAILURE) {
                                elements0.add(4, address7);
                                TreeNode address8 = FAILURE;
                                address8 = _read_indicator_name();
                                if (address8 != FAILURE) {
                                    elements0.add(5, address8);
                                } else {
                                    elements0 = null;
                                    offset = index1;
                                }
                            } else {
                                elements0 = null;
                                offset = index1;
                            }
                        } else {
                            elements0 = null;
                            offset = index1;
                        }
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode9(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_abilitive() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.abilitive);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.abilitive, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 3);
            }
            if (chunk0 != null && chunk0.equals("can")) {
                address1 = new TreeNode(input.substring(offset, offset + 3), offset);
                offset = offset + 3;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"can\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_spacer();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    address3 = _read_ability();
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode11(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_ability() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.ability);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.ability, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(2);
            TreeNode address1 = FAILURE;
            address1 = _read_ability_name();
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                int index2 = offset;
                int index3 = offset;
                List<TreeNode> elements1 = new ArrayList<TreeNode>(5);
                TreeNode address3 = FAILURE;
                String chunk0 = null;
                if (offset < inputSize) {
                    chunk0 = input.substring(offset, offset + 1);
                }
                if (chunk0 != null && chunk0.equals("(")) {
                    address3 = new TreeNode(input.substring(offset, offset + 1), offset);
                    offset = offset + 1;
                } else {
                    address3 = FAILURE;
                    if (offset > failure) {
                        failure = offset;
                        expected = new ArrayList<String>();
                    }
                    if (offset == failure) {
                        expected.add("\"(\"");
                    }
                }
                if (address3 != FAILURE) {
                    elements1.add(0, address3);
                    TreeNode address4 = FAILURE;
                    int index4 = offset;
                    address4 = _read_space();
                    if (address4 == FAILURE) {
                        address4 = new TreeNode(input.substring(index4, index4), index4);
                        offset = index4;
                    }
                    if (address4 != FAILURE) {
                        elements1.add(1, address4);
                        TreeNode address5 = FAILURE;
                        int index5 = offset;
                        int index6 = offset;
                        List<TreeNode> elements2 = new ArrayList<TreeNode>(2);
                        TreeNode address6 = FAILURE;
                        address6 = _read_ability_argument();
                        if (address6 != FAILURE) {
                            elements2.add(0, address6);
                            TreeNode address7 = FAILURE;
                            int remaining0 = 0;
                            int index7 = offset;
                            List<TreeNode> elements3 = new ArrayList<TreeNode>();
                            TreeNode address8 = new TreeNode("", -1);
                            while (address8 != FAILURE) {
                                int index8 = offset;
                                List<TreeNode> elements4 = new ArrayList<TreeNode>(4);
                                TreeNode address9 = FAILURE;
                                int index9 = offset;
                                address9 = _read_space();
                                if (address9 == FAILURE) {
                                    address9 = new TreeNode(input.substring(index9, index9), index9);
                                    offset = index9;
                                }
                                if (address9 != FAILURE) {
                                    elements4.add(0, address9);
                                    TreeNode address10 = FAILURE;
                                    String chunk1 = null;
                                    if (offset < inputSize) {
                                        chunk1 = input.substring(offset, offset + 1);
                                    }
                                    if (chunk1 != null && chunk1.equals(",")) {
                                        address10 = new TreeNode(input.substring(offset, offset + 1), offset);
                                        offset = offset + 1;
                                    } else {
                                        address10 = FAILURE;
                                        if (offset > failure) {
                                            failure = offset;
                                            expected = new ArrayList<String>();
                                        }
                                        if (offset == failure) {
                                            expected.add("\",\"");
                                        }
                                    }
                                    if (address10 != FAILURE) {
                                        elements4.add(1, address10);
                                        TreeNode address11 = FAILURE;
                                        int index10 = offset;
                                        address11 = _read_space();
                                        if (address11 == FAILURE) {
                                            address11 = new TreeNode(input.substring(index10, index10), index10);
                                            offset = index10;
                                        }
                                        if (address11 != FAILURE) {
                                            elements4.add(2, address11);
                                            TreeNode address12 = FAILURE;
                                            address12 = _read_ability_argument();
                                            if (address12 != FAILURE) {
                                                elements4.add(3, address12);
                                            } else {
                                                elements4 = null;
                                                offset = index8;
                                            }
                                        } else {
                                            elements4 = null;
                                            offset = index8;
                                        }
                                    } else {
                                        elements4 = null;
                                        offset = index8;
                                    }
                                } else {
                                    elements4 = null;
                                    offset = index8;
                                }
                                if (elements4 == null) {
                                    address8 = FAILURE;
                                } else {
                                    address8 = new TreeNode15(input.substring(index8, offset), index8, elements4);
                                    offset = offset;
                                }
                                if (address8 != FAILURE) {
                                    elements3.add(address8);
                                    --remaining0;
                                }
                            }
                            if (remaining0 <= 0) {
                                address7 = new TreeNode(input.substring(index7, offset), index7, elements3);
                                offset = offset;
                            } else {
                                address7 = FAILURE;
                            }
                            if (address7 != FAILURE) {
                                elements2.add(1, address7);
                            } else {
                                elements2 = null;
                                offset = index6;
                            }
                        } else {
                            elements2 = null;
                            offset = index6;
                        }
                        if (elements2 == null) {
                            address5 = FAILURE;
                        } else {
                            address5 = new TreeNode14(input.substring(index6, offset), index6, elements2);
                            offset = offset;
                        }
                        if (address5 == FAILURE) {
                            address5 = new TreeNode(input.substring(index5, index5), index5);
                            offset = index5;
                        }
                        if (address5 != FAILURE) {
                            elements1.add(2, address5);
                            TreeNode address13 = FAILURE;
                            int index11 = offset;
                            address13 = _read_space();
                            if (address13 == FAILURE) {
                                address13 = new TreeNode(input.substring(index11, index11), index11);
                                offset = index11;
                            }
                            if (address13 != FAILURE) {
                                elements1.add(3, address13);
                                TreeNode address14 = FAILURE;
                                String chunk2 = null;
                                if (offset < inputSize) {
                                    chunk2 = input.substring(offset, offset + 1);
                                }
                                if (chunk2 != null && chunk2.equals(")")) {
                                    address14 = new TreeNode(input.substring(offset, offset + 1), offset);
                                    offset = offset + 1;
                                } else {
                                    address14 = FAILURE;
                                    if (offset > failure) {
                                        failure = offset;
                                        expected = new ArrayList<String>();
                                    }
                                    if (offset == failure) {
                                        expected.add("\")\"");
                                    }
                                }
                                if (address14 != FAILURE) {
                                    elements1.add(4, address14);
                                } else {
                                    elements1 = null;
                                    offset = index3;
                                }
                            } else {
                                elements1 = null;
                                offset = index3;
                            }
                        } else {
                            elements1 = null;
                            offset = index3;
                        }
                    } else {
                        elements1 = null;
                        offset = index3;
                    }
                } else {
                    elements1 = null;
                    offset = index3;
                }
                if (elements1 == null) {
                    address2 = FAILURE;
                } else {
                    address2 = new TreeNode13(input.substring(index3, offset), index3, elements1);
                    offset = offset;
                }
                if (address2 == FAILURE) {
                    address2 = new TreeNode(input.substring(index2, index2), index2);
                    offset = index2;
                }
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode12(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_abilities_list() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.abilities_list);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.abilities_list, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            int index2 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(2);
            TreeNode address1 = FAILURE;
            address1 = _read_ability();
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                int remaining0 = 0;
                int index3 = offset;
                List<TreeNode> elements1 = new ArrayList<TreeNode>();
                TreeNode address3 = new TreeNode("", -1);
                while (address3 != FAILURE) {
                    int index4 = offset;
                    List<TreeNode> elements2 = new ArrayList<TreeNode>(4);
                    TreeNode address4 = FAILURE;
                    int index5 = offset;
                    address4 = _read_space();
                    if (address4 == FAILURE) {
                        address4 = new TreeNode(input.substring(index5, index5), index5);
                        offset = index5;
                    }
                    if (address4 != FAILURE) {
                        elements2.add(0, address4);
                        TreeNode address5 = FAILURE;
                        String chunk0 = null;
                        if (offset < inputSize) {
                            chunk0 = input.substring(offset, offset + 1);
                        }
                        if (chunk0 != null && chunk0.equals(",")) {
                            address5 = new TreeNode(input.substring(offset, offset + 1), offset);
                            offset = offset + 1;
                        } else {
                            address5 = FAILURE;
                            if (offset > failure) {
                                failure = offset;
                                expected = new ArrayList<String>();
                            }
                            if (offset == failure) {
                                expected.add("\",\"");
                            }
                        }
                        if (address5 != FAILURE) {
                            elements2.add(1, address5);
                            TreeNode address6 = FAILURE;
                            int index6 = offset;
                            address6 = _read_space();
                            if (address6 == FAILURE) {
                                address6 = new TreeNode(input.substring(index6, index6), index6);
                                offset = index6;
                            }
                            if (address6 != FAILURE) {
                                elements2.add(2, address6);
                                TreeNode address7 = FAILURE;
                                address7 = _read_ability();
                                if (address7 != FAILURE) {
                                    elements2.add(3, address7);
                                } else {
                                    elements2 = null;
                                    offset = index4;
                                }
                            } else {
                                elements2 = null;
                                offset = index4;
                            }
                        } else {
                            elements2 = null;
                            offset = index4;
                        }
                    } else {
                        elements2 = null;
                        offset = index4;
                    }
                    if (elements2 == null) {
                        address3 = FAILURE;
                    } else {
                        address3 = new TreeNode17(input.substring(index4, offset), index4, elements2);
                        offset = offset;
                    }
                    if (address3 != FAILURE) {
                        elements1.add(address3);
                        --remaining0;
                    }
                }
                if (remaining0 <= 0) {
                    address2 = new TreeNode(input.substring(index3, offset), index3, elements1);
                    offset = offset;
                } else {
                    address2 = FAILURE;
                }
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                } else {
                    elements0 = null;
                    offset = index2;
                }
            } else {
                elements0 = null;
                offset = index2;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode16(input.substring(index2, offset), index2, elements0);
                offset = offset;
            }
            if (address0 == FAILURE) {
                address0 = new TreeNode(input.substring(index1, index1), index1);
                offset = index1;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_ability_parameters() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.ability_parameters);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.ability_parameters, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(5);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 1);
            }
            if (chunk0 != null && chunk0.equals("(")) {
                address1 = new TreeNode(input.substring(offset, offset + 1), offset);
                offset = offset + 1;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"(\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                int index2 = offset;
                address2 = _read_space();
                if (address2 == FAILURE) {
                    address2 = new TreeNode(input.substring(index2, index2), index2);
                    offset = index2;
                }
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address3 = FAILURE;
                    int index3 = offset;
                    int index4 = offset;
                    List<TreeNode> elements1 = new ArrayList<TreeNode>(2);
                    TreeNode address4 = FAILURE;
                    address4 = _read_ability_argument();
                    if (address4 != FAILURE) {
                        elements1.add(0, address4);
                        TreeNode address5 = FAILURE;
                        int remaining0 = 0;
                        int index5 = offset;
                        List<TreeNode> elements2 = new ArrayList<TreeNode>();
                        TreeNode address6 = new TreeNode("", -1);
                        while (address6 != FAILURE) {
                            int index6 = offset;
                            List<TreeNode> elements3 = new ArrayList<TreeNode>(4);
                            TreeNode address7 = FAILURE;
                            int index7 = offset;
                            address7 = _read_space();
                            if (address7 == FAILURE) {
                                address7 = new TreeNode(input.substring(index7, index7), index7);
                                offset = index7;
                            }
                            if (address7 != FAILURE) {
                                elements3.add(0, address7);
                                TreeNode address8 = FAILURE;
                                String chunk1 = null;
                                if (offset < inputSize) {
                                    chunk1 = input.substring(offset, offset + 1);
                                }
                                if (chunk1 != null && chunk1.equals(",")) {
                                    address8 = new TreeNode(input.substring(offset, offset + 1), offset);
                                    offset = offset + 1;
                                } else {
                                    address8 = FAILURE;
                                    if (offset > failure) {
                                        failure = offset;
                                        expected = new ArrayList<String>();
                                    }
                                    if (offset == failure) {
                                        expected.add("\",\"");
                                    }
                                }
                                if (address8 != FAILURE) {
                                    elements3.add(1, address8);
                                    TreeNode address9 = FAILURE;
                                    int index8 = offset;
                                    address9 = _read_space();
                                    if (address9 == FAILURE) {
                                        address9 = new TreeNode(input.substring(index8, index8), index8);
                                        offset = index8;
                                    }
                                    if (address9 != FAILURE) {
                                        elements3.add(2, address9);
                                        TreeNode address10 = FAILURE;
                                        address10 = _read_ability_argument();
                                        if (address10 != FAILURE) {
                                            elements3.add(3, address10);
                                        } else {
                                            elements3 = null;
                                            offset = index6;
                                        }
                                    } else {
                                        elements3 = null;
                                        offset = index6;
                                    }
                                } else {
                                    elements3 = null;
                                    offset = index6;
                                }
                            } else {
                                elements3 = null;
                                offset = index6;
                            }
                            if (elements3 == null) {
                                address6 = FAILURE;
                            } else {
                                address6 = new TreeNode20(input.substring(index6, offset), index6, elements3);
                                offset = offset;
                            }
                            if (address6 != FAILURE) {
                                elements2.add(address6);
                                --remaining0;
                            }
                        }
                        if (remaining0 <= 0) {
                            address5 = new TreeNode(input.substring(index5, offset), index5, elements2);
                            offset = offset;
                        } else {
                            address5 = FAILURE;
                        }
                        if (address5 != FAILURE) {
                            elements1.add(1, address5);
                        } else {
                            elements1 = null;
                            offset = index4;
                        }
                    } else {
                        elements1 = null;
                        offset = index4;
                    }
                    if (elements1 == null) {
                        address3 = FAILURE;
                    } else {
                        address3 = new TreeNode19(input.substring(index4, offset), index4, elements1);
                        offset = offset;
                    }
                    if (address3 == FAILURE) {
                        address3 = new TreeNode(input.substring(index3, index3), index3);
                        offset = index3;
                    }
                    if (address3 != FAILURE) {
                        elements0.add(2, address3);
                        TreeNode address11 = FAILURE;
                        int index9 = offset;
                        address11 = _read_space();
                        if (address11 == FAILURE) {
                            address11 = new TreeNode(input.substring(index9, index9), index9);
                            offset = index9;
                        }
                        if (address11 != FAILURE) {
                            elements0.add(3, address11);
                            TreeNode address12 = FAILURE;
                            String chunk2 = null;
                            if (offset < inputSize) {
                                chunk2 = input.substring(offset, offset + 1);
                            }
                            if (chunk2 != null && chunk2.equals(")")) {
                                address12 = new TreeNode(input.substring(offset, offset + 1), offset);
                                offset = offset + 1;
                            } else {
                                address12 = FAILURE;
                                if (offset > failure) {
                                    failure = offset;
                                    expected = new ArrayList<String>();
                                }
                                if (offset == failure) {
                                    expected.add("\")\"");
                                }
                            }
                            if (address12 != FAILURE) {
                                elements0.add(4, address12);
                            } else {
                                elements0 = null;
                                offset = index1;
                            }
                        } else {
                            elements0 = null;
                            offset = index1;
                        }
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode18(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_ability_name() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.ability_name);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.ability_name, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 1);
            }
            if (chunk0 != null && REGEX_1.matcher(chunk0).matches()) {
                address1 = new TreeNode(input.substring(offset, offset + 1), offset);
                offset = offset + 1;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("[a-z]");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                int remaining0 = 0;
                int index2 = offset;
                List<TreeNode> elements1 = new ArrayList<TreeNode>();
                TreeNode address3 = new TreeNode("", -1);
                while (address3 != FAILURE) {
                    String chunk1 = null;
                    if (offset < inputSize) {
                        chunk1 = input.substring(offset, offset + 1);
                    }
                    if (chunk1 != null && REGEX_2.matcher(chunk1).matches()) {
                        address3 = new TreeNode(input.substring(offset, offset + 1), offset);
                        offset = offset + 1;
                    } else {
                        address3 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("[a-z0-9]");
                        }
                    }
                    if (address3 != FAILURE) {
                        elements1.add(address3);
                        --remaining0;
                    }
                }
                if (remaining0 <= 0) {
                    address2 = new TreeNode(input.substring(index2, offset), index2, elements1);
                    offset = offset;
                } else {
                    address2 = FAILURE;
                }
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address4 = FAILURE;
                    int remaining1 = 0;
                    int index3 = offset;
                    List<TreeNode> elements2 = new ArrayList<TreeNode>();
                    TreeNode address5 = new TreeNode("", -1);
                    while (address5 != FAILURE) {
                        int index4 = offset;
                        List<TreeNode> elements3 = new ArrayList<TreeNode>(2);
                        TreeNode address6 = FAILURE;
                        String chunk2 = null;
                        if (offset < inputSize) {
                            chunk2 = input.substring(offset, offset + 1);
                        }
                        if (chunk2 != null && chunk2.equals("_")) {
                            address6 = new TreeNode(input.substring(offset, offset + 1), offset);
                            offset = offset + 1;
                        } else {
                            address6 = FAILURE;
                            if (offset > failure) {
                                failure = offset;
                                expected = new ArrayList<String>();
                            }
                            if (offset == failure) {
                                expected.add("\"_\"");
                            }
                        }
                        if (address6 != FAILURE) {
                            elements3.add(0, address6);
                            TreeNode address7 = FAILURE;
                            int remaining2 = 1;
                            int index5 = offset;
                            List<TreeNode> elements4 = new ArrayList<TreeNode>();
                            TreeNode address8 = new TreeNode("", -1);
                            while (address8 != FAILURE) {
                                String chunk3 = null;
                                if (offset < inputSize) {
                                    chunk3 = input.substring(offset, offset + 1);
                                }
                                if (chunk3 != null && REGEX_3.matcher(chunk3).matches()) {
                                    address8 = new TreeNode(input.substring(offset, offset + 1), offset);
                                    offset = offset + 1;
                                } else {
                                    address8 = FAILURE;
                                    if (offset > failure) {
                                        failure = offset;
                                        expected = new ArrayList<String>();
                                    }
                                    if (offset == failure) {
                                        expected.add("[a-z0-9]");
                                    }
                                }
                                if (address8 != FAILURE) {
                                    elements4.add(address8);
                                    --remaining2;
                                }
                            }
                            if (remaining2 <= 0) {
                                address7 = new TreeNode(input.substring(index5, offset), index5, elements4);
                                offset = offset;
                            } else {
                                address7 = FAILURE;
                            }
                            if (address7 != FAILURE) {
                                elements3.add(1, address7);
                            } else {
                                elements3 = null;
                                offset = index4;
                            }
                        } else {
                            elements3 = null;
                            offset = index4;
                        }
                        if (elements3 == null) {
                            address5 = FAILURE;
                        } else {
                            address5 = new TreeNode(input.substring(index4, offset), index4, elements3);
                            offset = offset;
                        }
                        if (address5 != FAILURE) {
                            elements2.add(address5);
                            --remaining1;
                        }
                    }
                    if (remaining1 <= 0) {
                        address4 = new TreeNode(input.substring(index3, offset), index3, elements2);
                        offset = offset;
                    } else {
                        address4 = FAILURE;
                    }
                    if (address4 != FAILURE) {
                        elements0.add(2, address4);
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_ability_argument() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.ability_argument);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.ability_argument, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 1);
            }
            if (chunk0 != null && REGEX_4.matcher(chunk0).matches()) {
                address1 = new TreeNode(input.substring(offset, offset + 1), offset);
                offset = offset + 1;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("[a-z]");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                int remaining0 = 0;
                int index2 = offset;
                List<TreeNode> elements1 = new ArrayList<TreeNode>();
                TreeNode address3 = new TreeNode("", -1);
                while (address3 != FAILURE) {
                    String chunk1 = null;
                    if (offset < inputSize) {
                        chunk1 = input.substring(offset, offset + 1);
                    }
                    if (chunk1 != null && REGEX_5.matcher(chunk1).matches()) {
                        address3 = new TreeNode(input.substring(offset, offset + 1), offset);
                        offset = offset + 1;
                    } else {
                        address3 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("[a-z0-9]");
                        }
                    }
                    if (address3 != FAILURE) {
                        elements1.add(address3);
                        --remaining0;
                    }
                }
                if (remaining0 <= 0) {
                    address2 = new TreeNode(input.substring(index2, offset), index2, elements1);
                    offset = offset;
                } else {
                    address2 = FAILURE;
                }
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address4 = FAILURE;
                    int remaining1 = 0;
                    int index3 = offset;
                    List<TreeNode> elements2 = new ArrayList<TreeNode>();
                    TreeNode address5 = new TreeNode("", -1);
                    while (address5 != FAILURE) {
                        int index4 = offset;
                        List<TreeNode> elements3 = new ArrayList<TreeNode>(2);
                        TreeNode address6 = FAILURE;
                        String chunk2 = null;
                        if (offset < inputSize) {
                            chunk2 = input.substring(offset, offset + 1);
                        }
                        if (chunk2 != null && chunk2.equals("_")) {
                            address6 = new TreeNode(input.substring(offset, offset + 1), offset);
                            offset = offset + 1;
                        } else {
                            address6 = FAILURE;
                            if (offset > failure) {
                                failure = offset;
                                expected = new ArrayList<String>();
                            }
                            if (offset == failure) {
                                expected.add("\"_\"");
                            }
                        }
                        if (address6 != FAILURE) {
                            elements3.add(0, address6);
                            TreeNode address7 = FAILURE;
                            int remaining2 = 1;
                            int index5 = offset;
                            List<TreeNode> elements4 = new ArrayList<TreeNode>();
                            TreeNode address8 = new TreeNode("", -1);
                            while (address8 != FAILURE) {
                                String chunk3 = null;
                                if (offset < inputSize) {
                                    chunk3 = input.substring(offset, offset + 1);
                                }
                                if (chunk3 != null && REGEX_6.matcher(chunk3).matches()) {
                                    address8 = new TreeNode(input.substring(offset, offset + 1), offset);
                                    offset = offset + 1;
                                } else {
                                    address8 = FAILURE;
                                    if (offset > failure) {
                                        failure = offset;
                                        expected = new ArrayList<String>();
                                    }
                                    if (offset == failure) {
                                        expected.add("[a-z0-9]");
                                    }
                                }
                                if (address8 != FAILURE) {
                                    elements4.add(address8);
                                    --remaining2;
                                }
                            }
                            if (remaining2 <= 0) {
                                address7 = new TreeNode(input.substring(index5, offset), index5, elements4);
                                offset = offset;
                            } else {
                                address7 = FAILURE;
                            }
                            if (address7 != FAILURE) {
                                elements3.add(1, address7);
                            } else {
                                elements3 = null;
                                offset = index4;
                            }
                        } else {
                            elements3 = null;
                            offset = index4;
                        }
                        if (elements3 == null) {
                            address5 = FAILURE;
                        } else {
                            address5 = new TreeNode(input.substring(index4, offset), index4, elements3);
                            offset = offset;
                        }
                        if (address5 != FAILURE) {
                            elements2.add(address5);
                            --remaining1;
                        }
                    }
                    if (remaining1 <= 0) {
                        address4 = new TreeNode(input.substring(index3, offset), index3, elements2);
                        offset = offset;
                    } else {
                        address4 = FAILURE;
                    }
                    if (address4 != FAILURE) {
                        elements0.add(2, address4);
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_variety_name() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.variety_name);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.variety_name, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 1);
            }
            if (chunk0 != null && REGEX_7.matcher(chunk0).matches()) {
                address1 = new TreeNode(input.substring(offset, offset + 1), offset);
                offset = offset + 1;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("[a-z]");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                int remaining0 = 0;
                int index2 = offset;
                List<TreeNode> elements1 = new ArrayList<TreeNode>();
                TreeNode address3 = new TreeNode("", -1);
                while (address3 != FAILURE) {
                    String chunk1 = null;
                    if (offset < inputSize) {
                        chunk1 = input.substring(offset, offset + 1);
                    }
                    if (chunk1 != null && REGEX_8.matcher(chunk1).matches()) {
                        address3 = new TreeNode(input.substring(offset, offset + 1), offset);
                        offset = offset + 1;
                    } else {
                        address3 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("[a-z0-9]");
                        }
                    }
                    if (address3 != FAILURE) {
                        elements1.add(address3);
                        --remaining0;
                    }
                }
                if (remaining0 <= 0) {
                    address2 = new TreeNode(input.substring(index2, offset), index2, elements1);
                    offset = offset;
                } else {
                    address2 = FAILURE;
                }
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address4 = FAILURE;
                    int remaining1 = 0;
                    int index3 = offset;
                    List<TreeNode> elements2 = new ArrayList<TreeNode>();
                    TreeNode address5 = new TreeNode("", -1);
                    while (address5 != FAILURE) {
                        int index4 = offset;
                        List<TreeNode> elements3 = new ArrayList<TreeNode>(2);
                        TreeNode address6 = FAILURE;
                        String chunk2 = null;
                        if (offset < inputSize) {
                            chunk2 = input.substring(offset, offset + 1);
                        }
                        if (chunk2 != null && chunk2.equals("_")) {
                            address6 = new TreeNode(input.substring(offset, offset + 1), offset);
                            offset = offset + 1;
                        } else {
                            address6 = FAILURE;
                            if (offset > failure) {
                                failure = offset;
                                expected = new ArrayList<String>();
                            }
                            if (offset == failure) {
                                expected.add("\"_\"");
                            }
                        }
                        if (address6 != FAILURE) {
                            elements3.add(0, address6);
                            TreeNode address7 = FAILURE;
                            int remaining2 = 1;
                            int index5 = offset;
                            List<TreeNode> elements4 = new ArrayList<TreeNode>();
                            TreeNode address8 = new TreeNode("", -1);
                            while (address8 != FAILURE) {
                                String chunk3 = null;
                                if (offset < inputSize) {
                                    chunk3 = input.substring(offset, offset + 1);
                                }
                                if (chunk3 != null && REGEX_9.matcher(chunk3).matches()) {
                                    address8 = new TreeNode(input.substring(offset, offset + 1), offset);
                                    offset = offset + 1;
                                } else {
                                    address8 = FAILURE;
                                    if (offset > failure) {
                                        failure = offset;
                                        expected = new ArrayList<String>();
                                    }
                                    if (offset == failure) {
                                        expected.add("[a-z0-9]");
                                    }
                                }
                                if (address8 != FAILURE) {
                                    elements4.add(address8);
                                    --remaining2;
                                }
                            }
                            if (remaining2 <= 0) {
                                address7 = new TreeNode(input.substring(index5, offset), index5, elements4);
                                offset = offset;
                            } else {
                                address7 = FAILURE;
                            }
                            if (address7 != FAILURE) {
                                elements3.add(1, address7);
                            } else {
                                elements3 = null;
                                offset = index4;
                            }
                        } else {
                            elements3 = null;
                            offset = index4;
                        }
                        if (elements3 == null) {
                            address5 = FAILURE;
                        } else {
                            address5 = new TreeNode(input.substring(index4, offset), index4, elements3);
                            offset = offset;
                        }
                        if (address5 != FAILURE) {
                            elements2.add(address5);
                            --remaining1;
                        }
                    }
                    if (remaining1 <= 0) {
                        address4 = new TreeNode(input.substring(index3, offset), index3, elements2);
                        offset = offset;
                    } else {
                        address4 = FAILURE;
                    }
                    if (address4 != FAILURE) {
                        elements0.add(2, address4);
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_indicator_name() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.indicator_name);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.indicator_name, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(3);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 1);
            }
            if (chunk0 != null && REGEX_10.matcher(chunk0).matches()) {
                address1 = new TreeNode(input.substring(offset, offset + 1), offset);
                offset = offset + 1;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("[a-z]");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                int remaining0 = 0;
                int index2 = offset;
                List<TreeNode> elements1 = new ArrayList<TreeNode>();
                TreeNode address3 = new TreeNode("", -1);
                while (address3 != FAILURE) {
                    String chunk1 = null;
                    if (offset < inputSize) {
                        chunk1 = input.substring(offset, offset + 1);
                    }
                    if (chunk1 != null && REGEX_11.matcher(chunk1).matches()) {
                        address3 = new TreeNode(input.substring(offset, offset + 1), offset);
                        offset = offset + 1;
                    } else {
                        address3 = FAILURE;
                        if (offset > failure) {
                            failure = offset;
                            expected = new ArrayList<String>();
                        }
                        if (offset == failure) {
                            expected.add("[a-z0-9]");
                        }
                    }
                    if (address3 != FAILURE) {
                        elements1.add(address3);
                        --remaining0;
                    }
                }
                if (remaining0 <= 0) {
                    address2 = new TreeNode(input.substring(index2, offset), index2, elements1);
                    offset = offset;
                } else {
                    address2 = FAILURE;
                }
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                    TreeNode address4 = FAILURE;
                    int remaining1 = 0;
                    int index3 = offset;
                    List<TreeNode> elements2 = new ArrayList<TreeNode>();
                    TreeNode address5 = new TreeNode("", -1);
                    while (address5 != FAILURE) {
                        int index4 = offset;
                        List<TreeNode> elements3 = new ArrayList<TreeNode>(2);
                        TreeNode address6 = FAILURE;
                        String chunk2 = null;
                        if (offset < inputSize) {
                            chunk2 = input.substring(offset, offset + 1);
                        }
                        if (chunk2 != null && chunk2.equals("_")) {
                            address6 = new TreeNode(input.substring(offset, offset + 1), offset);
                            offset = offset + 1;
                        } else {
                            address6 = FAILURE;
                            if (offset > failure) {
                                failure = offset;
                                expected = new ArrayList<String>();
                            }
                            if (offset == failure) {
                                expected.add("\"_\"");
                            }
                        }
                        if (address6 != FAILURE) {
                            elements3.add(0, address6);
                            TreeNode address7 = FAILURE;
                            int remaining2 = 1;
                            int index5 = offset;
                            List<TreeNode> elements4 = new ArrayList<TreeNode>();
                            TreeNode address8 = new TreeNode("", -1);
                            while (address8 != FAILURE) {
                                String chunk3 = null;
                                if (offset < inputSize) {
                                    chunk3 = input.substring(offset, offset + 1);
                                }
                                if (chunk3 != null && REGEX_12.matcher(chunk3).matches()) {
                                    address8 = new TreeNode(input.substring(offset, offset + 1), offset);
                                    offset = offset + 1;
                                } else {
                                    address8 = FAILURE;
                                    if (offset > failure) {
                                        failure = offset;
                                        expected = new ArrayList<String>();
                                    }
                                    if (offset == failure) {
                                        expected.add("[a-z0-9]");
                                    }
                                }
                                if (address8 != FAILURE) {
                                    elements4.add(address8);
                                    --remaining2;
                                }
                            }
                            if (remaining2 <= 0) {
                                address7 = new TreeNode(input.substring(index5, offset), index5, elements4);
                                offset = offset;
                            } else {
                                address7 = FAILURE;
                            }
                            if (address7 != FAILURE) {
                                elements3.add(1, address7);
                            } else {
                                elements3 = null;
                                offset = index4;
                            }
                        } else {
                            elements3 = null;
                            offset = index4;
                        }
                        if (elements3 == null) {
                            address5 = FAILURE;
                        } else {
                            address5 = new TreeNode(input.substring(index4, offset), index4, elements3);
                            offset = offset;
                        }
                        if (address5 != FAILURE) {
                            elements2.add(address5);
                            --remaining1;
                        }
                    }
                    if (remaining1 <= 0) {
                        address4 = new TreeNode(input.substring(index3, offset), index3, elements2);
                        offset = offset;
                    } else {
                        address4 = FAILURE;
                    }
                    if (address4 != FAILURE) {
                        elements0.add(2, address4);
                    } else {
                        elements0 = null;
                        offset = index1;
                    }
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_negative() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.negative);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.negative, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(2);
            TreeNode address1 = FAILURE;
            String chunk0 = null;
            if (offset < inputSize) {
                chunk0 = input.substring(offset, offset + 3);
            }
            if (chunk0 != null && chunk0.equals("not")) {
                address1 = new TreeNode(input.substring(offset, offset + 3), offset);
                offset = offset + 3;
            } else {
                address1 = FAILURE;
                if (offset > failure) {
                    failure = offset;
                    expected = new ArrayList<String>();
                }
                if (offset == failure) {
                    expected.add("\"not\"");
                }
            }
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_negated_test();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                } else {
                    elements0 = null;
                    offset = index1;
                }
            } else {
                elements0 = null;
                offset = index1;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode21(input.substring(index1, offset), index1, elements0);
                offset = offset;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_negated_test() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.negated_test);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.negated_test, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int index1 = offset;
            int index2 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>(2);
            TreeNode address1 = FAILURE;
            address1 = _read_spacer();
            if (address1 != FAILURE) {
                elements0.add(0, address1);
                TreeNode address2 = FAILURE;
                address2 = _read_ability();
                if (address2 != FAILURE) {
                    elements0.add(1, address2);
                } else {
                    elements0 = null;
                    offset = index2;
                }
            } else {
                elements0 = null;
                offset = index2;
            }
            if (elements0 == null) {
                address0 = FAILURE;
            } else {
                address0 = new TreeNode22(input.substring(index2, offset), index2, elements0);
                offset = offset;
            }
            if (address0 == FAILURE) {
                offset = index1;
                int index3 = offset;
                List<TreeNode> elements1 = new ArrayList<TreeNode>(2);
                TreeNode address3 = FAILURE;
                address3 = _read_spacer();
                if (address3 != FAILURE) {
                    elements1.add(0, address3);
                    TreeNode address4 = FAILURE;
                    address4 = _read_essence();
                    if (address4 != FAILURE) {
                        elements1.add(1, address4);
                    } else {
                        elements1 = null;
                        offset = index3;
                    }
                } else {
                    elements1 = null;
                    offset = index3;
                }
                if (elements1 == null) {
                    address0 = FAILURE;
                } else {
                    address0 = new TreeNode23(input.substring(index3, offset), index3, elements1);
                    offset = offset;
                }
                if (address0 == FAILURE) {
                    offset = index1;
                    int index4 = offset;
                    List<TreeNode> elements2 = new ArrayList<TreeNode>(2);
                    TreeNode address5 = FAILURE;
                    address5 = _read_spacer();
                    if (address5 != FAILURE) {
                        elements2.add(0, address5);
                        TreeNode address6 = FAILURE;
                        address6 = _read_derivative();
                        if (address6 != FAILURE) {
                            elements2.add(1, address6);
                        } else {
                            elements2 = null;
                            offset = index4;
                        }
                    } else {
                        elements2 = null;
                        offset = index4;
                    }
                    if (elements2 == null) {
                        address0 = FAILURE;
                    } else {
                        address0 = new TreeNode24(input.substring(index4, offset), index4, elements2);
                        offset = offset;
                    }
                    if (address0 == FAILURE) {
                        offset = index1;
                        int index5 = offset;
                        List<TreeNode> elements3 = new ArrayList<TreeNode>(3);
                        TreeNode address7 = FAILURE;
                        String chunk0 = null;
                        if (offset < inputSize) {
                            chunk0 = input.substring(offset, offset + 1);
                        }
                        if (chunk0 != null && chunk0.equals("(")) {
                            address7 = new TreeNode(input.substring(offset, offset + 1), offset);
                            offset = offset + 1;
                        } else {
                            address7 = FAILURE;
                            if (offset > failure) {
                                failure = offset;
                                expected = new ArrayList<String>();
                            }
                            if (offset == failure) {
                                expected.add("\"(\"");
                            }
                        }
                        if (address7 != FAILURE) {
                            elements3.add(0, address7);
                            TreeNode address8 = FAILURE;
                            address8 = _read_boolean_expression();
                            if (address8 != FAILURE) {
                                elements3.add(1, address8);
                                TreeNode address9 = FAILURE;
                                String chunk1 = null;
                                if (offset < inputSize) {
                                    chunk1 = input.substring(offset, offset + 1);
                                }
                                if (chunk1 != null && chunk1.equals(")")) {
                                    address9 = new TreeNode(input.substring(offset, offset + 1), offset);
                                    offset = offset + 1;
                                } else {
                                    address9 = FAILURE;
                                    if (offset > failure) {
                                        failure = offset;
                                        expected = new ArrayList<String>();
                                    }
                                    if (offset == failure) {
                                        expected.add("\")\"");
                                    }
                                }
                                if (address9 != FAILURE) {
                                    elements3.add(2, address9);
                                } else {
                                    elements3 = null;
                                    offset = index5;
                                }
                            } else {
                                elements3 = null;
                                offset = index5;
                            }
                        } else {
                            elements3 = null;
                            offset = index5;
                        }
                        if (elements3 == null) {
                            address0 = FAILURE;
                        } else {
                            address0 = new TreeNode25(input.substring(index5, offset), index5, elements3);
                            offset = offset;
                        }
                        if (address0 == FAILURE) {
                            offset = index1;
                        }
                    }
                }
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_spacer() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.spacer);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.spacer, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int remaining0 = 1;
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>();
            TreeNode address1 = new TreeNode("", -1);
            while (address1 != FAILURE) {
                String chunk0 = null;
                if (offset < inputSize) {
                    chunk0 = input.substring(offset, offset + 1);
                }
                if (chunk0 != null && chunk0.equals(" ")) {
                    address1 = new TreeNode(input.substring(offset, offset + 1), offset);
                    offset = offset + 1;
                } else {
                    address1 = FAILURE;
                    if (offset > failure) {
                        failure = offset;
                        expected = new ArrayList<String>();
                    }
                    if (offset == failure) {
                        expected.add("\" \"");
                    }
                }
                if (address1 != FAILURE) {
                    elements0.add(address1);
                    --remaining0;
                }
            }
            if (remaining0 <= 0) {
                address0 = new TreeNode(input.substring(index1, offset), index1, elements0);
                offset = offset;
            } else {
                address0 = FAILURE;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }

    TreeNode _read_space() {
        TreeNode address0 = FAILURE;
        int index0 = offset;
        Map<Integer, CacheRecord> rule = cache.get(Label.space);
        if (rule == null) {
            rule = new HashMap<Integer, CacheRecord>();
            cache.put(Label.space, rule);
        }
        if (rule.containsKey(offset)) {
            address0 = rule.get(offset).node;
            offset = rule.get(offset).tail;
        } else {
            int remaining0 = 0;
            int index1 = offset;
            List<TreeNode> elements0 = new ArrayList<TreeNode>();
            TreeNode address1 = new TreeNode("", -1);
            while (address1 != FAILURE) {
                String chunk0 = null;
                if (offset < inputSize) {
                    chunk0 = input.substring(offset, offset + 1);
                }
                if (chunk0 != null && REGEX_13.matcher(chunk0).matches()) {
                    address1 = new TreeNode(input.substring(offset, offset + 1), offset);
                    offset = offset + 1;
                } else {
                    address1 = FAILURE;
                    if (offset > failure) {
                        failure = offset;
                        expected = new ArrayList<String>();
                    }
                    if (offset == failure) {
                        expected.add("[\\s\\n]");
                    }
                }
                if (address1 != FAILURE) {
                    elements0.add(address1);
                    --remaining0;
                }
            }
            if (remaining0 <= 0) {
                address0 = new TreeNode(input.substring(index1, offset), index1, elements0);
                offset = offset;
            } else {
                address0 = FAILURE;
            }
            rule.put(index0, new CacheRecord(address0, offset));
        }
        return address0;
    }
}
