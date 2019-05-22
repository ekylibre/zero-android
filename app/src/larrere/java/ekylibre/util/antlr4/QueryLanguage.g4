grammar QueryLanguage;

boolean_expression
    : disjunctive | test
    ;

conjonctive
    : head=test ' and ' operand=conjonctive <Conjunction> | test
    ;

disjunctive
    : head=conjonctive ' or ' operand=disjunctive <Disjunction> | conjonctive
    ;

test
    : abilitive | essence | non_essence | derivative | non_derivative | inclusion | indicative | negative | '(' boolean_expression ')' <BooleanExpression>
    ;

essence
    : 'is ' variety_name <EssenceTest>
    ;

non_essence
    : 'isnt ' variety_name <NonEssenceTest>
    ;

derivative
    : 'derives from ' variety_name <DerivativeTest>
    ;

non_derivative
    : 'dont derive from ' variety_name <DerivativeTest>
    ;

inclusion
    : 'includes ' variety_name <InclusionTest>
    ;

indicative
    : 'has ' ('frozen ' | 'variable ')? 'indicator ' indicator_name <IndicatorTest>
    ;

abilitive
    : 'can ' ability <AbilityTest>
    ;

ability
    : ability_name ability_parameters?
    ;

abilities_list
    : (ability (SPACE? ',' SPACE? ability)* )?
    ;

ability_parameters
    : '(' SPACE* (ability_argument (SPACE* ',' SPACE* ability_argument)* )? SPACE* ')'
    ;

ability_name
    : name <AbilityName>
    ;

ability_argument
    : name <AbilityArgument>
    ;

variety_name
    : name <VarietyName>
    ;

indicator_name
    : name <VarietyName>
    ;

negative
    : 'not' negated_test <NegativeTest>
    ;

negated_test
    : spacer+ ability | spacer+ essence | spacer+ derivative | '(' boolean_expression ')' <BooleanExpression>
    ;

spacer
    : ' '
    ;

name
    : CHAR (CHAR|DIGIT)* ('_' (CHAR|DIGIT)+)*
    ;

SPACE
    : [\r\n\t\f ]
    ;

CHAR : [a-z] ;

DIGIT : [0-9] ;

