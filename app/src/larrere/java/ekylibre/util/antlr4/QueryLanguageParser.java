// Generated from /home/remi/Git/ekylibre/zero-android/app/src/larrere/java/ekylibre/util/antlr4/QueryLanguage.g4 by ANTLR 4.7.2
package ekylibre.util.antlr4;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryLanguageParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, SPACE=19, CHAR=20, DIGIT=21;
	public static final int
		RULE_boolean_expression = 0, RULE_conjonctive = 1, RULE_disjunctive = 2, 
		RULE_test = 3, RULE_essence = 4, RULE_non_essence = 5, RULE_derivative = 6, 
		RULE_non_derivative = 7, RULE_inclusion = 8, RULE_indicative = 9, RULE_abilitive = 10, 
		RULE_ability = 11, RULE_abilities_list = 12, RULE_ability_parameters = 13, 
		RULE_ability_name = 14, RULE_ability_argument = 15, RULE_variety_name = 16, 
		RULE_indicator_name = 17, RULE_negative = 18, RULE_negated_test = 19, 
		RULE_spacer = 20, RULE_name = 21;
	private static String[] makeRuleNames() {
		return new String[] {
			"boolean_expression", "conjonctive", "disjunctive", "test", "essence", 
			"non_essence", "derivative", "non_derivative", "inclusion", "indicative", 
			"abilitive", "ability", "abilities_list", "ability_parameters", "ability_name", 
			"ability_argument", "variety_name", "indicator_name", "negative", "negated_test", 
			"spacer", "name"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "' and '", "' or '", "'('", "')'", "'is '", "'isnt '", "'derives from '", 
			"'dont derive from '", "'includes '", "'has '", "'frozen '", "'variable '", 
			"'indicator '", "'can '", "','", "'not'", "' '", "'_'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "SPACE", "CHAR", "DIGIT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "QueryLanguage.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public QueryLanguageParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class Boolean_expressionContext extends ParserRuleContext {
		public DisjunctiveContext disjunctive() {
			return getRuleContext(DisjunctiveContext.class,0);
		}
		public TestContext test() {
			return getRuleContext(TestContext.class,0);
		}
		public Boolean_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boolean_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterBoolean_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitBoolean_expression(this);
		}
	}

	public final Boolean_expressionContext boolean_expression() throws RecognitionException {
		Boolean_expressionContext _localctx = new Boolean_expressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_boolean_expression);
		try {
			setState(46);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				disjunctive();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(45);
				test();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConjonctiveContext extends ParserRuleContext {
		public TestContext head;
		public ConjonctiveContext operand;
		public TestContext test() {
			return getRuleContext(TestContext.class,0);
		}
		public ConjonctiveContext conjonctive() {
			return getRuleContext(ConjonctiveContext.class,0);
		}
		public ConjonctiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conjonctive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterConjonctive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitConjonctive(this);
		}
	}

	public final ConjonctiveContext conjonctive() throws RecognitionException {
		ConjonctiveContext _localctx = new ConjonctiveContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_conjonctive);
		try {
			setState(53);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(48);
				((ConjonctiveContext)_localctx).head = test();
				setState(49);
				match(T__0);
				setState(50);
				((ConjonctiveContext)_localctx).operand = conjonctive();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(52);
				test();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DisjunctiveContext extends ParserRuleContext {
		public ConjonctiveContext head;
		public DisjunctiveContext operand;
		public ConjonctiveContext conjonctive() {
			return getRuleContext(ConjonctiveContext.class,0);
		}
		public DisjunctiveContext disjunctive() {
			return getRuleContext(DisjunctiveContext.class,0);
		}
		public DisjunctiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_disjunctive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterDisjunctive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitDisjunctive(this);
		}
	}

	public final DisjunctiveContext disjunctive() throws RecognitionException {
		DisjunctiveContext _localctx = new DisjunctiveContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_disjunctive);
		try {
			setState(60);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(55);
				((DisjunctiveContext)_localctx).head = conjonctive();
				setState(56);
				match(T__1);
				setState(57);
				((DisjunctiveContext)_localctx).operand = disjunctive();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(59);
				conjonctive();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TestContext extends ParserRuleContext {
		public AbilitiveContext abilitive() {
			return getRuleContext(AbilitiveContext.class,0);
		}
		public EssenceContext essence() {
			return getRuleContext(EssenceContext.class,0);
		}
		public Non_essenceContext non_essence() {
			return getRuleContext(Non_essenceContext.class,0);
		}
		public DerivativeContext derivative() {
			return getRuleContext(DerivativeContext.class,0);
		}
		public Non_derivativeContext non_derivative() {
			return getRuleContext(Non_derivativeContext.class,0);
		}
		public InclusionContext inclusion() {
			return getRuleContext(InclusionContext.class,0);
		}
		public IndicativeContext indicative() {
			return getRuleContext(IndicativeContext.class,0);
		}
		public NegativeContext negative() {
			return getRuleContext(NegativeContext.class,0);
		}
		public Boolean_expressionContext boolean_expression() {
			return getRuleContext(Boolean_expressionContext.class,0);
		}
		public TestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_test; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterTest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitTest(this);
		}
	}

	public final TestContext test() throws RecognitionException {
		TestContext _localctx = new TestContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_test);
		try {
			setState(74);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__13:
				enterOuterAlt(_localctx, 1);
				{
				setState(62);
				abilitive();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(63);
				essence();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 3);
				{
				setState(64);
				non_essence();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 4);
				{
				setState(65);
				derivative();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 5);
				{
				setState(66);
				non_derivative();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 6);
				{
				setState(67);
				inclusion();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 7);
				{
				setState(68);
				indicative();
				}
				break;
			case T__15:
				enterOuterAlt(_localctx, 8);
				{
				setState(69);
				negative();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 9);
				{
				setState(70);
				match(T__2);
				setState(71);
				boolean_expression();
				setState(72);
				match(T__3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EssenceContext extends ParserRuleContext {
		public Variety_nameContext variety_name() {
			return getRuleContext(Variety_nameContext.class,0);
		}
		public EssenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_essence; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterEssence(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitEssence(this);
		}
	}

	public final EssenceContext essence() throws RecognitionException {
		EssenceContext _localctx = new EssenceContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_essence);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			match(T__4);
			setState(77);
			variety_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Non_essenceContext extends ParserRuleContext {
		public Variety_nameContext variety_name() {
			return getRuleContext(Variety_nameContext.class,0);
		}
		public Non_essenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_non_essence; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterNon_essence(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitNon_essence(this);
		}
	}

	public final Non_essenceContext non_essence() throws RecognitionException {
		Non_essenceContext _localctx = new Non_essenceContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_non_essence);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			match(T__5);
			setState(80);
			variety_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DerivativeContext extends ParserRuleContext {
		public Variety_nameContext variety_name() {
			return getRuleContext(Variety_nameContext.class,0);
		}
		public DerivativeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_derivative; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterDerivative(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitDerivative(this);
		}
	}

	public final DerivativeContext derivative() throws RecognitionException {
		DerivativeContext _localctx = new DerivativeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_derivative);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			match(T__6);
			setState(83);
			variety_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Non_derivativeContext extends ParserRuleContext {
		public Variety_nameContext variety_name() {
			return getRuleContext(Variety_nameContext.class,0);
		}
		public Non_derivativeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_non_derivative; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterNon_derivative(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitNon_derivative(this);
		}
	}

	public final Non_derivativeContext non_derivative() throws RecognitionException {
		Non_derivativeContext _localctx = new Non_derivativeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_non_derivative);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(T__7);
			setState(86);
			variety_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InclusionContext extends ParserRuleContext {
		public Variety_nameContext variety_name() {
			return getRuleContext(Variety_nameContext.class,0);
		}
		public InclusionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inclusion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterInclusion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitInclusion(this);
		}
	}

	public final InclusionContext inclusion() throws RecognitionException {
		InclusionContext _localctx = new InclusionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_inclusion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(T__8);
			setState(89);
			variety_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IndicativeContext extends ParserRuleContext {
		public Indicator_nameContext indicator_name() {
			return getRuleContext(Indicator_nameContext.class,0);
		}
		public IndicativeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indicative; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterIndicative(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitIndicative(this);
		}
	}

	public final IndicativeContext indicative() throws RecognitionException {
		IndicativeContext _localctx = new IndicativeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_indicative);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			match(T__9);
			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10 || _la==T__11) {
				{
				setState(92);
				_la = _input.LA(1);
				if ( !(_la==T__10 || _la==T__11) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(95);
			match(T__12);
			setState(96);
			indicator_name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AbilitiveContext extends ParserRuleContext {
		public AbilityContext ability() {
			return getRuleContext(AbilityContext.class,0);
		}
		public AbilitiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abilitive; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterAbilitive(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitAbilitive(this);
		}
	}

	public final AbilitiveContext abilitive() throws RecognitionException {
		AbilitiveContext _localctx = new AbilitiveContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_abilitive);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(T__13);
			setState(99);
			ability();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AbilityContext extends ParserRuleContext {
		public Ability_nameContext ability_name() {
			return getRuleContext(Ability_nameContext.class,0);
		}
		public Ability_parametersContext ability_parameters() {
			return getRuleContext(Ability_parametersContext.class,0);
		}
		public AbilityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ability; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterAbility(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitAbility(this);
		}
	}

	public final AbilityContext ability() throws RecognitionException {
		AbilityContext _localctx = new AbilityContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_ability);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			ability_name();
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(102);
				ability_parameters();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Abilities_listContext extends ParserRuleContext {
		public List<AbilityContext> ability() {
			return getRuleContexts(AbilityContext.class);
		}
		public AbilityContext ability(int i) {
			return getRuleContext(AbilityContext.class,i);
		}
		public List<TerminalNode> SPACE() { return getTokens(QueryLanguageParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(QueryLanguageParser.SPACE, i);
		}
		public Abilities_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abilities_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterAbilities_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitAbilities_list(this);
		}
	}

	public final Abilities_listContext abilities_list() throws RecognitionException {
		Abilities_listContext _localctx = new Abilities_listContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_abilities_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CHAR) {
				{
				setState(105);
				ability();
				setState(116);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__14 || _la==SPACE) {
					{
					{
					setState(107);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==SPACE) {
						{
						setState(106);
						match(SPACE);
						}
					}

					setState(109);
					match(T__14);
					setState(111);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==SPACE) {
						{
						setState(110);
						match(SPACE);
						}
					}

					setState(113);
					ability();
					}
					}
					setState(118);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ability_parametersContext extends ParserRuleContext {
		public List<TerminalNode> SPACE() { return getTokens(QueryLanguageParser.SPACE); }
		public TerminalNode SPACE(int i) {
			return getToken(QueryLanguageParser.SPACE, i);
		}
		public List<Ability_argumentContext> ability_argument() {
			return getRuleContexts(Ability_argumentContext.class);
		}
		public Ability_argumentContext ability_argument(int i) {
			return getRuleContext(Ability_argumentContext.class,i);
		}
		public Ability_parametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ability_parameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterAbility_parameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitAbility_parameters(this);
		}
	}

	public final Ability_parametersContext ability_parameters() throws RecognitionException {
		Ability_parametersContext _localctx = new Ability_parametersContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_ability_parameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(T__2);
			setState(125);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(122);
					match(SPACE);
					}
					} 
				}
				setState(127);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
			}
			setState(148);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==CHAR) {
				{
				setState(128);
				ability_argument();
				setState(145);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(132);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==SPACE) {
							{
							{
							setState(129);
							match(SPACE);
							}
							}
							setState(134);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(135);
						match(T__14);
						setState(139);
						_errHandler.sync(this);
						_la = _input.LA(1);
						while (_la==SPACE) {
							{
							{
							setState(136);
							match(SPACE);
							}
							}
							setState(141);
							_errHandler.sync(this);
							_la = _input.LA(1);
						}
						setState(142);
						ability_argument();
						}
						} 
					}
					setState(147);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				}
			}

			setState(153);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SPACE) {
				{
				{
				setState(150);
				match(SPACE);
				}
				}
				setState(155);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(156);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ability_nameContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public Ability_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ability_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterAbility_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitAbility_name(this);
		}
	}

	public final Ability_nameContext ability_name() throws RecognitionException {
		Ability_nameContext _localctx = new Ability_nameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_ability_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Ability_argumentContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public Ability_argumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ability_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterAbility_argument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitAbility_argument(this);
		}
	}

	public final Ability_argumentContext ability_argument() throws RecognitionException {
		Ability_argumentContext _localctx = new Ability_argumentContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_ability_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Variety_nameContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public Variety_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variety_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterVariety_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitVariety_name(this);
		}
	}

	public final Variety_nameContext variety_name() throws RecognitionException {
		Variety_nameContext _localctx = new Variety_nameContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_variety_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Indicator_nameContext extends ParserRuleContext {
		public NameContext name() {
			return getRuleContext(NameContext.class,0);
		}
		public Indicator_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_indicator_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterIndicator_name(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitIndicator_name(this);
		}
	}

	public final Indicator_nameContext indicator_name() throws RecognitionException {
		Indicator_nameContext _localctx = new Indicator_nameContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_indicator_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			name();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NegativeContext extends ParserRuleContext {
		public Negated_testContext negated_test() {
			return getRuleContext(Negated_testContext.class,0);
		}
		public NegativeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negative; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterNegative(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitNegative(this);
		}
	}

	public final NegativeContext negative() throws RecognitionException {
		NegativeContext _localctx = new NegativeContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_negative);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			match(T__15);
			setState(167);
			negated_test();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Negated_testContext extends ParserRuleContext {
		public AbilityContext ability() {
			return getRuleContext(AbilityContext.class,0);
		}
		public List<SpacerContext> spacer() {
			return getRuleContexts(SpacerContext.class);
		}
		public SpacerContext spacer(int i) {
			return getRuleContext(SpacerContext.class,i);
		}
		public EssenceContext essence() {
			return getRuleContext(EssenceContext.class,0);
		}
		public DerivativeContext derivative() {
			return getRuleContext(DerivativeContext.class,0);
		}
		public Boolean_expressionContext boolean_expression() {
			return getRuleContext(Boolean_expressionContext.class,0);
		}
		public Negated_testContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negated_test; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterNegated_test(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitNegated_test(this);
		}
	}

	public final Negated_testContext negated_test() throws RecognitionException {
		Negated_testContext _localctx = new Negated_testContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_negated_test);
		int _la;
		try {
			setState(194);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(170); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(169);
					spacer();
					}
					}
					setState(172); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__16 );
				setState(174);
				ability();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(177); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(176);
					spacer();
					}
					}
					setState(179); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__16 );
				setState(181);
				essence();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(184); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(183);
					spacer();
					}
					}
					setState(186); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==T__16 );
				setState(188);
				derivative();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(190);
				match(T__2);
				setState(191);
				boolean_expression();
				setState(192);
				match(T__3);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SpacerContext extends ParserRuleContext {
		public SpacerContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_spacer; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterSpacer(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitSpacer(this);
		}
	}

	public final SpacerContext spacer() throws RecognitionException {
		SpacerContext _localctx = new SpacerContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_spacer);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			match(T__16);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NameContext extends ParserRuleContext {
		public List<TerminalNode> CHAR() { return getTokens(QueryLanguageParser.CHAR); }
		public TerminalNode CHAR(int i) {
			return getToken(QueryLanguageParser.CHAR, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(QueryLanguageParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(QueryLanguageParser.DIGIT, i);
		}
		public NameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_name; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).enterName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof QueryLanguageListener ) ((QueryLanguageListener)listener).exitName(this);
		}
	}

	public final NameContext name() throws RecognitionException {
		NameContext _localctx = new NameContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(198);
			match(CHAR);
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CHAR || _la==DIGIT) {
				{
				{
				setState(199);
				_la = _input.LA(1);
				if ( !(_la==CHAR || _la==DIGIT) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(204);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(213);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(205);
				match(T__17);
				setState(207); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(206);
					_la = _input.LA(1);
					if ( !(_la==CHAR || _la==DIGIT) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(209); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CHAR || _la==DIGIT );
				}
				}
				setState(215);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\27\u00db\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\5\2\61\n\2"+
		"\3\3\3\3\3\3\3\3\3\3\5\38\n\3\3\4\3\4\3\4\3\4\3\4\5\4?\n\4\3\5\3\5\3\5"+
		"\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5M\n\5\3\6\3\6\3\6\3\7\3\7\3\7"+
		"\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\5\13`\n\13\3\13\3\13\3"+
		"\13\3\f\3\f\3\f\3\r\3\r\5\rj\n\r\3\16\3\16\5\16n\n\16\3\16\3\16\5\16r"+
		"\n\16\3\16\7\16u\n\16\f\16\16\16x\13\16\5\16z\n\16\3\17\3\17\7\17~\n\17"+
		"\f\17\16\17\u0081\13\17\3\17\3\17\7\17\u0085\n\17\f\17\16\17\u0088\13"+
		"\17\3\17\3\17\7\17\u008c\n\17\f\17\16\17\u008f\13\17\3\17\7\17\u0092\n"+
		"\17\f\17\16\17\u0095\13\17\5\17\u0097\n\17\3\17\7\17\u009a\n\17\f\17\16"+
		"\17\u009d\13\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24"+
		"\3\24\3\24\3\25\6\25\u00ad\n\25\r\25\16\25\u00ae\3\25\3\25\3\25\6\25\u00b4"+
		"\n\25\r\25\16\25\u00b5\3\25\3\25\3\25\6\25\u00bb\n\25\r\25\16\25\u00bc"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u00c5\n\25\3\26\3\26\3\27\3\27\7\27"+
		"\u00cb\n\27\f\27\16\27\u00ce\13\27\3\27\3\27\6\27\u00d2\n\27\r\27\16\27"+
		"\u00d3\7\27\u00d6\n\27\f\27\16\27\u00d9\13\27\3\27\2\2\30\2\4\6\b\n\f"+
		"\16\20\22\24\26\30\32\34\36 \"$&(*,\2\4\3\2\r\16\3\2\26\27\2\u00e4\2\60"+
		"\3\2\2\2\4\67\3\2\2\2\6>\3\2\2\2\bL\3\2\2\2\nN\3\2\2\2\fQ\3\2\2\2\16T"+
		"\3\2\2\2\20W\3\2\2\2\22Z\3\2\2\2\24]\3\2\2\2\26d\3\2\2\2\30g\3\2\2\2\32"+
		"y\3\2\2\2\34{\3\2\2\2\36\u00a0\3\2\2\2 \u00a2\3\2\2\2\"\u00a4\3\2\2\2"+
		"$\u00a6\3\2\2\2&\u00a8\3\2\2\2(\u00c4\3\2\2\2*\u00c6\3\2\2\2,\u00c8\3"+
		"\2\2\2.\61\5\6\4\2/\61\5\b\5\2\60.\3\2\2\2\60/\3\2\2\2\61\3\3\2\2\2\62"+
		"\63\5\b\5\2\63\64\7\3\2\2\64\65\5\4\3\2\658\3\2\2\2\668\5\b\5\2\67\62"+
		"\3\2\2\2\67\66\3\2\2\28\5\3\2\2\29:\5\4\3\2:;\7\4\2\2;<\5\6\4\2<?\3\2"+
		"\2\2=?\5\4\3\2>9\3\2\2\2>=\3\2\2\2?\7\3\2\2\2@M\5\26\f\2AM\5\n\6\2BM\5"+
		"\f\7\2CM\5\16\b\2DM\5\20\t\2EM\5\22\n\2FM\5\24\13\2GM\5&\24\2HI\7\5\2"+
		"\2IJ\5\2\2\2JK\7\6\2\2KM\3\2\2\2L@\3\2\2\2LA\3\2\2\2LB\3\2\2\2LC\3\2\2"+
		"\2LD\3\2\2\2LE\3\2\2\2LF\3\2\2\2LG\3\2\2\2LH\3\2\2\2M\t\3\2\2\2NO\7\7"+
		"\2\2OP\5\"\22\2P\13\3\2\2\2QR\7\b\2\2RS\5\"\22\2S\r\3\2\2\2TU\7\t\2\2"+
		"UV\5\"\22\2V\17\3\2\2\2WX\7\n\2\2XY\5\"\22\2Y\21\3\2\2\2Z[\7\13\2\2[\\"+
		"\5\"\22\2\\\23\3\2\2\2]_\7\f\2\2^`\t\2\2\2_^\3\2\2\2_`\3\2\2\2`a\3\2\2"+
		"\2ab\7\17\2\2bc\5$\23\2c\25\3\2\2\2de\7\20\2\2ef\5\30\r\2f\27\3\2\2\2"+
		"gi\5\36\20\2hj\5\34\17\2ih\3\2\2\2ij\3\2\2\2j\31\3\2\2\2kv\5\30\r\2ln"+
		"\7\25\2\2ml\3\2\2\2mn\3\2\2\2no\3\2\2\2oq\7\21\2\2pr\7\25\2\2qp\3\2\2"+
		"\2qr\3\2\2\2rs\3\2\2\2su\5\30\r\2tm\3\2\2\2ux\3\2\2\2vt\3\2\2\2vw\3\2"+
		"\2\2wz\3\2\2\2xv\3\2\2\2yk\3\2\2\2yz\3\2\2\2z\33\3\2\2\2{\177\7\5\2\2"+
		"|~\7\25\2\2}|\3\2\2\2~\u0081\3\2\2\2\177}\3\2\2\2\177\u0080\3\2\2\2\u0080"+
		"\u0096\3\2\2\2\u0081\177\3\2\2\2\u0082\u0093\5 \21\2\u0083\u0085\7\25"+
		"\2\2\u0084\u0083\3\2\2\2\u0085\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0086"+
		"\u0087\3\2\2\2\u0087\u0089\3\2\2\2\u0088\u0086\3\2\2\2\u0089\u008d\7\21"+
		"\2\2\u008a\u008c\7\25\2\2\u008b\u008a\3\2\2\2\u008c\u008f\3\2\2\2\u008d"+
		"\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u0090\3\2\2\2\u008f\u008d\3\2"+
		"\2\2\u0090\u0092\5 \21\2\u0091\u0086\3\2\2\2\u0092\u0095\3\2\2\2\u0093"+
		"\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3\2"+
		"\2\2\u0096\u0082\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u009b\3\2\2\2\u0098"+
		"\u009a\7\25\2\2\u0099\u0098\3\2\2\2\u009a\u009d\3\2\2\2\u009b\u0099\3"+
		"\2\2\2\u009b\u009c\3\2\2\2\u009c\u009e\3\2\2\2\u009d\u009b\3\2\2\2\u009e"+
		"\u009f\7\6\2\2\u009f\35\3\2\2\2\u00a0\u00a1\5,\27\2\u00a1\37\3\2\2\2\u00a2"+
		"\u00a3\5,\27\2\u00a3!\3\2\2\2\u00a4\u00a5\5,\27\2\u00a5#\3\2\2\2\u00a6"+
		"\u00a7\5,\27\2\u00a7%\3\2\2\2\u00a8\u00a9\7\22\2\2\u00a9\u00aa\5(\25\2"+
		"\u00aa\'\3\2\2\2\u00ab\u00ad\5*\26\2\u00ac\u00ab\3\2\2\2\u00ad\u00ae\3"+
		"\2\2\2\u00ae\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0"+
		"\u00b1\5\30\r\2\u00b1\u00c5\3\2\2\2\u00b2\u00b4\5*\26\2\u00b3\u00b2\3"+
		"\2\2\2\u00b4\u00b5\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6"+
		"\u00b7\3\2\2\2\u00b7\u00b8\5\n\6\2\u00b8\u00c5\3\2\2\2\u00b9\u00bb\5*"+
		"\26\2\u00ba\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00ba\3\2\2\2\u00bc"+
		"\u00bd\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00bf\5\16\b\2\u00bf\u00c5\3"+
		"\2\2\2\u00c0\u00c1\7\5\2\2\u00c1\u00c2\5\2\2\2\u00c2\u00c3\7\6\2\2\u00c3"+
		"\u00c5\3\2\2\2\u00c4\u00ac\3\2\2\2\u00c4\u00b3\3\2\2\2\u00c4\u00ba\3\2"+
		"\2\2\u00c4\u00c0\3\2\2\2\u00c5)\3\2\2\2\u00c6\u00c7\7\23\2\2\u00c7+\3"+
		"\2\2\2\u00c8\u00cc\7\26\2\2\u00c9\u00cb\t\3\2\2\u00ca\u00c9\3\2\2\2\u00cb"+
		"\u00ce\3\2\2\2\u00cc\u00ca\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00d7\3\2"+
		"\2\2\u00ce\u00cc\3\2\2\2\u00cf\u00d1\7\24\2\2\u00d0\u00d2\t\3\2\2\u00d1"+
		"\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d3\u00d4\3\2"+
		"\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00cf\3\2\2\2\u00d6\u00d9\3\2\2\2\u00d7"+
		"\u00d5\3\2\2\2\u00d7\u00d8\3\2\2\2\u00d8-\3\2\2\2\u00d9\u00d7\3\2\2\2"+
		"\31\60\67>L_imqvy\177\u0086\u008d\u0093\u0096\u009b\u00ae\u00b5\u00bc"+
		"\u00c4\u00cc\u00d3\u00d7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}