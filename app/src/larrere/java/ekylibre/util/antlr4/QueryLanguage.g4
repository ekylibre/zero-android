grammar QueryLanguage;

boolean_expression
    : disjunctive | test
    ;

conjonctive
    : head=test spacer 'and' spacer operand=conjonctive | test
    ;

disjunctive
    : head=conjonctive spacer 'or' spacer operand=disjunctive <Disjunction> | conjonctive
    ;

test
    : abilitive | essence | non_essence | derivative | non_derivative | inclusion | indicative | negative | '(' boolean_expression ')' <BooleanExpression>
    ;

essence
    : 'is' spacer variety_name <EssenceTest>
    ;

non_essence
    : 'isnt' spacer variety_name <NonEssenceTest>
    ;

derivative
    : 'derives' spacer 'from' spacer variety_name <DerivativeTest>
    ;

non_derivative
    : 'dont' spacer 'derive' spacer 'from' spacer variety_name <DerivativeTest>
    ;

inclusion
    : 'includes' spacer variety_name <InclusionTest>
    ;

indicative
    : 'has' (spacer ('frozen' | 'variable') )? spacer 'indicator' spacer indicator_name <IndicatorTest>
    ;

abilitive
    : 'can' spacer ability <AbilityTest>
    ;

ability
    : ability_name ('(' SPACE? (ability_argument (SPACE? ',' SPACE? ability_argument)* )? SPACE? ')')?
    ;

//abilities_list
//    : (ability (SPACE? ',' SPACE? ability)* )?
//    ;
//
//ability_parameters
//    : '(' SPACE? (ability_argument (SPACE? ',' SPACE? ability_argument)* )? SPACE? ')'
//    ;

ability_name
    : CHAR (CHAR|DIGIT)* ('_' (CHAR|DIGIT)+)*
    ;

ability_argument
    : CHAR (CHAR|DIGIT)* ('_' (CHAR|DIGIT)+)*
    ;

variety_name
    : CHAR (CHAR|DIGIT)* ('_' (CHAR|DIGIT)+)*
    ;

indicator_name
    : CHAR (CHAR|DIGIT)* ('_' (CHAR|DIGIT)+)*
    ;

negative
    : 'not' negated_test <NegativeTest>
    ;

negated_test
    : spacer ability | spacer essence | spacer derivative | '(' boolean_expression ')' <BooleanExpression>
    ;

spacer
    : ' '+
    ;

SPACE
    : [\r\n\t\f ]*
    ;

CHAR : [a-z] ;

DIGIT : [0-9] ;

