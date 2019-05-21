//package ekylibre.util.PEG;
//
//import org.parboiled.BaseParser;
//import org.parboiled.Rule;
//import org.parboiled.annotations.BuildParseTree;
//
//
//@SuppressWarnings({"InfiniteRecursion", "WeakerAccess"})
//@BuildParseTree
//public class AbilitiesParser extends BaseParser<Object> {
//
//    public Rule booleanExpression() {
//        return FirstOf(disjonctive(), test());
//    }
//
//    public Rule conjonctive() {
//        return FirstOf(
//                Sequence(test(), spacer(), "and", spacer(), conjonctive()),
//                test()
//        );
//    }
//
//    public Rule disjonctive() {
//        return FirstOf(
//                Sequence(conjonctive(), spacer(), "or", spacer(), disjonctive()),
//                conjonctive()
//        );
//    }
//
//    public Rule test() {
//        return FirstOf(
//                abilitive(),
//                essence(),
//                nonEssence(),
//                derivative(),
//                nonDerivative(),
//                inclusion(),
//                indicative(),
//                negative(),
//                Sequence('(', booleanExpression(), ')')
//        );
//    }
//
//    public Rule essence() {
//        return Sequence("is", spacer(), varietyName());
//    }
//
//    public Rule nonEssence() {
//        return Sequence("isnt", spacer(), varietyName());
//    }
//
//    public Rule derivative() {
//        return Sequence("derives", spacer(), "from", varietyName());
//    }
//
//    public Rule nonDerivative() {
//        return Sequence("dont", spacer(), "derive", spacer(), "from", varietyName());
//    }
//
//    public Rule inclusion() {
//        return Sequence("includes", spacer(), varietyName());
//    }
//
//    public Rule indicative() {
//        return Sequence(
//                "has", Optional(Sequence(spacer(), FirstOf("frozen", "variable"))),
//                spacer(), "indicator", spacer(), indicatorName()
//        );
//    }
//
//    public Rule abilitive() {
//        return Sequence("can", spacer(), ability());
//    }
//
//    public Rule ability() {
//        return Sequence(
//                abilityName(),
//                abilityParameter()
//        );
//    }
//
//    public Rule abilityList() {
//        return Optional(
//                Sequence(
//                    ability(),
//                    ZeroOrMore(Sequence(Optional(space()), ",", Optional(space()), ability()))
//                )
//        );
//    }
//
//    public Rule abilityParameter() {
//        return Sequence(
//                "(",
//                Optional(space()),
//                Optional(Sequence(
//                    abilityArgument(),
//                    ZeroOrMore(Optional(space()), ",", Optional(space()), abilityArgument())
//                )),
//                Optional(space()),
//                ")"
//        );
//    }
//
//    public Rule abilityName() {
//        return Sequence(
//                CharRange('a', 'z'),
//                ZeroOrMore(FirstOf(CharRange('a', 'z'), CharRange('0', '9'))),
//                ZeroOrMore(Sequence("_", OneOrMore(FirstOf(CharRange('a', 'z'), CharRange('0', '9')))))
//        );
//    }
//
//    public Rule abilityArgument() {
//        return Sequence(
//                CharRange('a', 'z'),
//                ZeroOrMore(FirstOf(CharRange('a', 'z'), CharRange('0', '9'))),
//                ZeroOrMore(Sequence("_", OneOrMore(FirstOf(CharRange('a', 'z'), CharRange('0', '9')))))
//        );
//    }
//
//    public Rule varietyName() {
//        return Sequence(
//                CharRange('a', 'z'),
//                ZeroOrMore(FirstOf(CharRange('a', 'z'), CharRange('0', '9'))),
//                ZeroOrMore(Sequence("_", OneOrMore(FirstOf(CharRange('a', 'z'), CharRange('0', '9')))))
//        );
//    }
//
//    public Rule indicatorName() {
//        return Sequence(
//                CharRange('a', 'z'),
//                ZeroOrMore(FirstOf(CharRange('a', 'z'), CharRange('0', '9'))),
//                ZeroOrMore(Sequence("_", OneOrMore(FirstOf(CharRange('a', 'z'), CharRange('0', '9')))))
//        );
//    }
//
//    public Rule negative() {
//        return Sequence("not", negativeTest());
//    }
//
//    public Rule negativeTest() {
//        return FirstOf(
//                Sequence(spacer(), ability()),
//                Sequence(spacer(), essence()),
//                Sequence(spacer(), derivative()),
//                Sequence("(", booleanExpression(), ")")
//        );
//    }
//
//    public Rule spacer() {
//        return OneOrMore(" ");
//    }
//
//    public Rule space() {
//        return ZeroOrMore(Sequence("\\s", "\\n"));
//    }
//
//}
