// Generated from /home/remi/Git/ekylibre/zero-android/app/src/larrere/java/ekylibre/util/antlr4/QueryLanguage.g4 by ANTLR 4.7.2
package ekylibre.util.antlr4;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link QueryLanguageParser}.
 */
public interface QueryLanguageListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#boolean_expression}.
	 * @param ctx the parse tree
	 */
	void enterBoolean_expression(QueryLanguageParser.Boolean_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#boolean_expression}.
	 * @param ctx the parse tree
	 */
	void exitBoolean_expression(QueryLanguageParser.Boolean_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#conjonctive}.
	 * @param ctx the parse tree
	 */
	void enterConjonctive(QueryLanguageParser.ConjonctiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#conjonctive}.
	 * @param ctx the parse tree
	 */
	void exitConjonctive(QueryLanguageParser.ConjonctiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#disjunctive}.
	 * @param ctx the parse tree
	 */
	void enterDisjunctive(QueryLanguageParser.DisjunctiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#disjunctive}.
	 * @param ctx the parse tree
	 */
	void exitDisjunctive(QueryLanguageParser.DisjunctiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#test}.
	 * @param ctx the parse tree
	 */
	void enterTest(QueryLanguageParser.TestContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#test}.
	 * @param ctx the parse tree
	 */
	void exitTest(QueryLanguageParser.TestContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#essence}.
	 * @param ctx the parse tree
	 */
	void enterEssence(QueryLanguageParser.EssenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#essence}.
	 * @param ctx the parse tree
	 */
	void exitEssence(QueryLanguageParser.EssenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#non_essence}.
	 * @param ctx the parse tree
	 */
	void enterNon_essence(QueryLanguageParser.Non_essenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#non_essence}.
	 * @param ctx the parse tree
	 */
	void exitNon_essence(QueryLanguageParser.Non_essenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#derivative}.
	 * @param ctx the parse tree
	 */
	void enterDerivative(QueryLanguageParser.DerivativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#derivative}.
	 * @param ctx the parse tree
	 */
	void exitDerivative(QueryLanguageParser.DerivativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#non_derivative}.
	 * @param ctx the parse tree
	 */
	void enterNon_derivative(QueryLanguageParser.Non_derivativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#non_derivative}.
	 * @param ctx the parse tree
	 */
	void exitNon_derivative(QueryLanguageParser.Non_derivativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#inclusion}.
	 * @param ctx the parse tree
	 */
	void enterInclusion(QueryLanguageParser.InclusionContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#inclusion}.
	 * @param ctx the parse tree
	 */
	void exitInclusion(QueryLanguageParser.InclusionContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#indicative}.
	 * @param ctx the parse tree
	 */
	void enterIndicative(QueryLanguageParser.IndicativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#indicative}.
	 * @param ctx the parse tree
	 */
	void exitIndicative(QueryLanguageParser.IndicativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#abilitive}.
	 * @param ctx the parse tree
	 */
	void enterAbilitive(QueryLanguageParser.AbilitiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#abilitive}.
	 * @param ctx the parse tree
	 */
	void exitAbilitive(QueryLanguageParser.AbilitiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#ability}.
	 * @param ctx the parse tree
	 */
	void enterAbility(QueryLanguageParser.AbilityContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#ability}.
	 * @param ctx the parse tree
	 */
	void exitAbility(QueryLanguageParser.AbilityContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#ability_name}.
	 * @param ctx the parse tree
	 */
	void enterAbility_name(QueryLanguageParser.Ability_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#ability_name}.
	 * @param ctx the parse tree
	 */
	void exitAbility_name(QueryLanguageParser.Ability_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#ability_argument}.
	 * @param ctx the parse tree
	 */
	void enterAbility_argument(QueryLanguageParser.Ability_argumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#ability_argument}.
	 * @param ctx the parse tree
	 */
	void exitAbility_argument(QueryLanguageParser.Ability_argumentContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#variety_name}.
	 * @param ctx the parse tree
	 */
	void enterVariety_name(QueryLanguageParser.Variety_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#variety_name}.
	 * @param ctx the parse tree
	 */
	void exitVariety_name(QueryLanguageParser.Variety_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#indicator_name}.
	 * @param ctx the parse tree
	 */
	void enterIndicator_name(QueryLanguageParser.Indicator_nameContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#indicator_name}.
	 * @param ctx the parse tree
	 */
	void exitIndicator_name(QueryLanguageParser.Indicator_nameContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#negative}.
	 * @param ctx the parse tree
	 */
	void enterNegative(QueryLanguageParser.NegativeContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#negative}.
	 * @param ctx the parse tree
	 */
	void exitNegative(QueryLanguageParser.NegativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#negated_test}.
	 * @param ctx the parse tree
	 */
	void enterNegated_test(QueryLanguageParser.Negated_testContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#negated_test}.
	 * @param ctx the parse tree
	 */
	void exitNegated_test(QueryLanguageParser.Negated_testContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryLanguageParser#spacer}.
	 * @param ctx the parse tree
	 */
	void enterSpacer(QueryLanguageParser.SpacerContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryLanguageParser#spacer}.
	 * @param ctx the parse tree
	 */
	void exitSpacer(QueryLanguageParser.SpacerContext ctx);
}