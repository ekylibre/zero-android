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
		T__17=18, T__18=19, T__19=20, SPACE=21, CHAR=22, DIGIT=23;
	public static final int
		RULE_boolean_expression = 0, RULE_conjonctive = 1, RULE_disjunctive = 2, 
		RULE_test = 3, RULE_essence = 4, RULE_non_essence = 5, RULE_derivative = 6, 
		RULE_non_derivative = 7, RULE_inclusion = 8, RULE_indicative = 9, RULE_abilitive = 10, 
		RULE_ability = 11, RULE_ability_name = 12, RULE_ability_argument = 13, 
		RULE_variety_name = 14, RULE_indicator_name = 15, RULE_negative = 16, 
		RULE_negated_test = 17, RULE_spacer = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"boolean_expression", "conjonctive", "disjunctive", "test", "essence", 
			"non_essence", "derivative", "non_derivative", "inclusion", "indicative", 
			"abilitive", "ability", "ability_name", "ability_argument", "variety_name", 
			"indicator_name", "negative", "negated_test", "spacer"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'and'", "'or'", "'('", "')'", "'is'", "'isnt'", "'derives'", "'from'", 
			"'dont'", "'derive'", "'includes'", "'has'", "'frozen'", "'variable'", 
			"'indicator'", "'can'", "','", "'_'", "'not'", "' '"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "SPACE", "CHAR", 
			"DIGIT"
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
			setState(40);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(38);
				disjunctive();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(39);
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
		public List<SpacerContext> spacer() {
			return getRuleContexts(SpacerContext.class);
		}
		public SpacerContext spacer(int i) {
			return getRuleContext(SpacerContext.class,i);
		}
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
			setState(49);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(42);
				((ConjonctiveContext)_localctx).head = test();
				setState(43);
				spacer();
				setState(44);
				match(T__0);
				setState(45);
				spacer();
				setState(46);
				((ConjonctiveContext)_localctx).operand = conjonctive();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(48);
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
		public List<SpacerContext> spacer() {
			return getRuleContexts(SpacerContext.class);
		}
		public SpacerContext spacer(int i) {
			return getRuleContext(SpacerContext.class,i);
		}
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
			setState(58);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(51);
				((DisjunctiveContext)_localctx).head = conjonctive();
				setState(52);
				spacer();
				setState(53);
				match(T__1);
				setState(54);
				spacer();
				setState(55);
				((DisjunctiveContext)_localctx).operand = disjunctive();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
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
			setState(72);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__15:
				enterOuterAlt(_localctx, 1);
				{
				setState(60);
				abilitive();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(61);
				essence();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 3);
				{
				setState(62);
				non_essence();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 4);
				{
				setState(63);
				derivative();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 5);
				{
				setState(64);
				non_derivative();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 6);
				{
				setState(65);
				inclusion();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 7);
				{
				setState(66);
				indicative();
				}
				break;
			case T__18:
				enterOuterAlt(_localctx, 8);
				{
				setState(67);
				negative();
				}
				break;
			case T__2:
				enterOuterAlt(_localctx, 9);
				{
				setState(68);
				match(T__2);
				setState(69);
				boolean_expression();
				setState(70);
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
		public SpacerContext spacer() {
			return getRuleContext(SpacerContext.class,0);
		}
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
			setState(74);
			match(T__4);
			setState(75);
			spacer();
			setState(76);
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
		public SpacerContext spacer() {
			return getRuleContext(SpacerContext.class,0);
		}
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
			setState(78);
			match(T__5);
			setState(79);
			spacer();
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
		public List<SpacerContext> spacer() {
			return getRuleContexts(SpacerContext.class);
		}
		public SpacerContext spacer(int i) {
			return getRuleContext(SpacerContext.class,i);
		}
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
			spacer();
			setState(84);
			match(T__7);
			setState(85);
			spacer();
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

	public static class Non_derivativeContext extends ParserRuleContext {
		public List<SpacerContext> spacer() {
			return getRuleContexts(SpacerContext.class);
		}
		public SpacerContext spacer(int i) {
			return getRuleContext(SpacerContext.class,i);
		}
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
			setState(88);
			match(T__8);
			setState(89);
			spacer();
			setState(90);
			match(T__9);
			setState(91);
			spacer();
			setState(92);
			match(T__7);
			setState(93);
			spacer();
			setState(94);
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
		public SpacerContext spacer() {
			return getRuleContext(SpacerContext.class,0);
		}
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
			setState(96);
			match(T__10);
			setState(97);
			spacer();
			setState(98);
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
		public List<SpacerContext> spacer() {
			return getRuleContexts(SpacerContext.class);
		}
		public SpacerContext spacer(int i) {
			return getRuleContext(SpacerContext.class,i);
		}
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
			setState(100);
			match(T__11);
			setState(104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(101);
				spacer();
				setState(102);
				_la = _input.LA(1);
				if ( !(_la==T__12 || _la==T__13) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				break;
			}
			setState(106);
			spacer();
			setState(107);
			match(T__14);
			setState(108);
			spacer();
			setState(109);
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
		public SpacerContext spacer() {
			return getRuleContext(SpacerContext.class,0);
		}
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
			setState(111);
			match(T__15);
			setState(112);
			spacer();
			setState(113);
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
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			ability_name();
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(116);
				match(T__2);
				setState(118);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
				case 1:
					{
					setState(117);
					match(SPACE);
					}
					break;
				}
				setState(134);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==CHAR) {
					{
					setState(120);
					ability_argument();
					setState(131);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(122);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==SPACE) {
								{
								setState(121);
								match(SPACE);
								}
							}

							setState(124);
							match(T__16);
							setState(126);
							_errHandler.sync(this);
							_la = _input.LA(1);
							if (_la==SPACE) {
								{
								setState(125);
								match(SPACE);
								}
							}

							setState(128);
							ability_argument();
							}
							} 
						}
						setState(133);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
					}
					}
				}

				setState(137);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SPACE) {
					{
					setState(136);
					match(SPACE);
					}
				}

				setState(139);
				match(T__3);
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

	public static class Ability_nameContext extends ParserRuleContext {
		public List<TerminalNode> CHAR() { return getTokens(QueryLanguageParser.CHAR); }
		public TerminalNode CHAR(int i) {
			return getToken(QueryLanguageParser.CHAR, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(QueryLanguageParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(QueryLanguageParser.DIGIT, i);
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
		enterRule(_localctx, 24, RULE_ability_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(142);
			match(CHAR);
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CHAR || _la==DIGIT) {
				{
				{
				setState(143);
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
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(157);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(149);
				match(T__17);
				setState(151); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(150);
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
					setState(153); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CHAR || _la==DIGIT );
				}
				}
				setState(159);
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

	public static class Ability_argumentContext extends ParserRuleContext {
		public List<TerminalNode> CHAR() { return getTokens(QueryLanguageParser.CHAR); }
		public TerminalNode CHAR(int i) {
			return getToken(QueryLanguageParser.CHAR, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(QueryLanguageParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(QueryLanguageParser.DIGIT, i);
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
		enterRule(_localctx, 26, RULE_ability_argument);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			match(CHAR);
			setState(164);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CHAR || _la==DIGIT) {
				{
				{
				setState(161);
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
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(167);
				match(T__17);
				setState(169); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(168);
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
					setState(171); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CHAR || _la==DIGIT );
				}
				}
				setState(177);
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

	public static class Variety_nameContext extends ParserRuleContext {
		public List<TerminalNode> CHAR() { return getTokens(QueryLanguageParser.CHAR); }
		public TerminalNode CHAR(int i) {
			return getToken(QueryLanguageParser.CHAR, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(QueryLanguageParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(QueryLanguageParser.DIGIT, i);
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
		enterRule(_localctx, 28, RULE_variety_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			match(CHAR);
			setState(182);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CHAR || _la==DIGIT) {
				{
				{
				setState(179);
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
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(185);
				match(T__17);
				setState(187); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(186);
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
					setState(189); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CHAR || _la==DIGIT );
				}
				}
				setState(195);
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

	public static class Indicator_nameContext extends ParserRuleContext {
		public List<TerminalNode> CHAR() { return getTokens(QueryLanguageParser.CHAR); }
		public TerminalNode CHAR(int i) {
			return getToken(QueryLanguageParser.CHAR, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(QueryLanguageParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(QueryLanguageParser.DIGIT, i);
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
		enterRule(_localctx, 30, RULE_indicator_name);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			match(CHAR);
			setState(200);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==CHAR || _la==DIGIT) {
				{
				{
				setState(197);
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
				setState(202);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(211);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__17) {
				{
				{
				setState(203);
				match(T__17);
				setState(205); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(204);
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
					setState(207); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==CHAR || _la==DIGIT );
				}
				}
				setState(213);
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
		enterRule(_localctx, 32, RULE_negative);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			match(T__18);
			setState(215);
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
		public SpacerContext spacer() {
			return getRuleContext(SpacerContext.class,0);
		}
		public AbilityContext ability() {
			return getRuleContext(AbilityContext.class,0);
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
		enterRule(_localctx, 34, RULE_negated_test);
		try {
			setState(230);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,24,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(217);
				spacer();
				setState(218);
				ability();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(220);
				spacer();
				setState(221);
				essence();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(223);
				spacer();
				setState(224);
				derivative();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(226);
				match(T__2);
				setState(227);
				boolean_expression();
				setState(228);
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
		enterRule(_localctx, 36, RULE_spacer);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(232);
				match(T__19);
				}
				}
				setState(235); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==T__19 );
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\31\u00f0\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\3\2\5\2+\n\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\64\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4=\n\4\3\5\3\5\3\5\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\5\5K\n\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13"+
		"\3\13\3\13\3\13\5\13k\n\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3"+
		"\r\3\r\3\r\5\ry\n\r\3\r\3\r\5\r}\n\r\3\r\3\r\5\r\u0081\n\r\3\r\7\r\u0084"+
		"\n\r\f\r\16\r\u0087\13\r\5\r\u0089\n\r\3\r\5\r\u008c\n\r\3\r\5\r\u008f"+
		"\n\r\3\16\3\16\7\16\u0093\n\16\f\16\16\16\u0096\13\16\3\16\3\16\6\16\u009a"+
		"\n\16\r\16\16\16\u009b\7\16\u009e\n\16\f\16\16\16\u00a1\13\16\3\17\3\17"+
		"\7\17\u00a5\n\17\f\17\16\17\u00a8\13\17\3\17\3\17\6\17\u00ac\n\17\r\17"+
		"\16\17\u00ad\7\17\u00b0\n\17\f\17\16\17\u00b3\13\17\3\20\3\20\7\20\u00b7"+
		"\n\20\f\20\16\20\u00ba\13\20\3\20\3\20\6\20\u00be\n\20\r\20\16\20\u00bf"+
		"\7\20\u00c2\n\20\f\20\16\20\u00c5\13\20\3\21\3\21\7\21\u00c9\n\21\f\21"+
		"\16\21\u00cc\13\21\3\21\3\21\6\21\u00d0\n\21\r\21\16\21\u00d1\7\21\u00d4"+
		"\n\21\f\21\16\21\u00d7\13\21\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u00e9\n\23\3\24\6\24\u00ec"+
		"\n\24\r\24\16\24\u00ed\3\24\2\2\25\2\4\6\b\n\f\16\20\22\24\26\30\32\34"+
		"\36 \"$&\2\4\3\2\17\20\3\2\30\31\2\u00ff\2*\3\2\2\2\4\63\3\2\2\2\6<\3"+
		"\2\2\2\bJ\3\2\2\2\nL\3\2\2\2\fP\3\2\2\2\16T\3\2\2\2\20Z\3\2\2\2\22b\3"+
		"\2\2\2\24f\3\2\2\2\26q\3\2\2\2\30u\3\2\2\2\32\u0090\3\2\2\2\34\u00a2\3"+
		"\2\2\2\36\u00b4\3\2\2\2 \u00c6\3\2\2\2\"\u00d8\3\2\2\2$\u00e8\3\2\2\2"+
		"&\u00eb\3\2\2\2(+\5\6\4\2)+\5\b\5\2*(\3\2\2\2*)\3\2\2\2+\3\3\2\2\2,-\5"+
		"\b\5\2-.\5&\24\2./\7\3\2\2/\60\5&\24\2\60\61\5\4\3\2\61\64\3\2\2\2\62"+
		"\64\5\b\5\2\63,\3\2\2\2\63\62\3\2\2\2\64\5\3\2\2\2\65\66\5\4\3\2\66\67"+
		"\5&\24\2\678\7\4\2\289\5&\24\29:\5\6\4\2:=\3\2\2\2;=\5\4\3\2<\65\3\2\2"+
		"\2<;\3\2\2\2=\7\3\2\2\2>K\5\26\f\2?K\5\n\6\2@K\5\f\7\2AK\5\16\b\2BK\5"+
		"\20\t\2CK\5\22\n\2DK\5\24\13\2EK\5\"\22\2FG\7\5\2\2GH\5\2\2\2HI\7\6\2"+
		"\2IK\3\2\2\2J>\3\2\2\2J?\3\2\2\2J@\3\2\2\2JA\3\2\2\2JB\3\2\2\2JC\3\2\2"+
		"\2JD\3\2\2\2JE\3\2\2\2JF\3\2\2\2K\t\3\2\2\2LM\7\7\2\2MN\5&\24\2NO\5\36"+
		"\20\2O\13\3\2\2\2PQ\7\b\2\2QR\5&\24\2RS\5\36\20\2S\r\3\2\2\2TU\7\t\2\2"+
		"UV\5&\24\2VW\7\n\2\2WX\5&\24\2XY\5\36\20\2Y\17\3\2\2\2Z[\7\13\2\2[\\\5"+
		"&\24\2\\]\7\f\2\2]^\5&\24\2^_\7\n\2\2_`\5&\24\2`a\5\36\20\2a\21\3\2\2"+
		"\2bc\7\r\2\2cd\5&\24\2de\5\36\20\2e\23\3\2\2\2fj\7\16\2\2gh\5&\24\2hi"+
		"\t\2\2\2ik\3\2\2\2jg\3\2\2\2jk\3\2\2\2kl\3\2\2\2lm\5&\24\2mn\7\21\2\2"+
		"no\5&\24\2op\5 \21\2p\25\3\2\2\2qr\7\22\2\2rs\5&\24\2st\5\30\r\2t\27\3"+
		"\2\2\2u\u008e\5\32\16\2vx\7\5\2\2wy\7\27\2\2xw\3\2\2\2xy\3\2\2\2y\u0088"+
		"\3\2\2\2z\u0085\5\34\17\2{}\7\27\2\2|{\3\2\2\2|}\3\2\2\2}~\3\2\2\2~\u0080"+
		"\7\23\2\2\177\u0081\7\27\2\2\u0080\177\3\2\2\2\u0080\u0081\3\2\2\2\u0081"+
		"\u0082\3\2\2\2\u0082\u0084\5\34\17\2\u0083|\3\2\2\2\u0084\u0087\3\2\2"+
		"\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085"+
		"\3\2\2\2\u0088z\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u008b\3\2\2\2\u008a"+
		"\u008c\7\27\2\2\u008b\u008a\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008d\3"+
		"\2\2\2\u008d\u008f\7\6\2\2\u008ev\3\2\2\2\u008e\u008f\3\2\2\2\u008f\31"+
		"\3\2\2\2\u0090\u0094\7\30\2\2\u0091\u0093\t\3\2\2\u0092\u0091\3\2\2\2"+
		"\u0093\u0096\3\2\2\2\u0094\u0092\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u009f"+
		"\3\2\2\2\u0096\u0094\3\2\2\2\u0097\u0099\7\24\2\2\u0098\u009a\t\3\2\2"+
		"\u0099\u0098\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u0099\3\2\2\2\u009b\u009c"+
		"\3\2\2\2\u009c\u009e\3\2\2\2\u009d\u0097\3\2\2\2\u009e\u00a1\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0\33\3\2\2\2\u00a1\u009f\3\2\2"+
		"\2\u00a2\u00a6\7\30\2\2\u00a3\u00a5\t\3\2\2\u00a4\u00a3\3\2\2\2\u00a5"+
		"\u00a8\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00b1\3\2"+
		"\2\2\u00a8\u00a6\3\2\2\2\u00a9\u00ab\7\24\2\2\u00aa\u00ac\t\3\2\2\u00ab"+
		"\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ad\u00ae\3\2"+
		"\2\2\u00ae\u00b0\3\2\2\2\u00af\u00a9\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1"+
		"\u00af\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\35\3\2\2\2\u00b3\u00b1\3\2\2"+
		"\2\u00b4\u00b8\7\30\2\2\u00b5\u00b7\t\3\2\2\u00b6\u00b5\3\2\2\2\u00b7"+
		"\u00ba\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00c3\3\2"+
		"\2\2\u00ba\u00b8\3\2\2\2\u00bb\u00bd\7\24\2\2\u00bc\u00be\t\3\2\2\u00bd"+
		"\u00bc\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00bd\3\2\2\2\u00bf\u00c0\3\2"+
		"\2\2\u00c0\u00c2\3\2\2\2\u00c1\u00bb\3\2\2\2\u00c2\u00c5\3\2\2\2\u00c3"+
		"\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\37\3\2\2\2\u00c5\u00c3\3\2\2"+
		"\2\u00c6\u00ca\7\30\2\2\u00c7\u00c9\t\3\2\2\u00c8\u00c7\3\2\2\2\u00c9"+
		"\u00cc\3\2\2\2\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00d5\3\2"+
		"\2\2\u00cc\u00ca\3\2\2\2\u00cd\u00cf\7\24\2\2\u00ce\u00d0\t\3\2\2\u00cf"+
		"\u00ce\3\2\2\2\u00d0\u00d1\3\2\2\2\u00d1\u00cf\3\2\2\2\u00d1\u00d2\3\2"+
		"\2\2\u00d2\u00d4\3\2\2\2\u00d3\u00cd\3\2\2\2\u00d4\u00d7\3\2\2\2\u00d5"+
		"\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6!\3\2\2\2\u00d7\u00d5\3\2\2\2"+
		"\u00d8\u00d9\7\25\2\2\u00d9\u00da\5$\23\2\u00da#\3\2\2\2\u00db\u00dc\5"+
		"&\24\2\u00dc\u00dd\5\30\r\2\u00dd\u00e9\3\2\2\2\u00de\u00df\5&\24\2\u00df"+
		"\u00e0\5\n\6\2\u00e0\u00e9\3\2\2\2\u00e1\u00e2\5&\24\2\u00e2\u00e3\5\16"+
		"\b\2\u00e3\u00e9\3\2\2\2\u00e4\u00e5\7\5\2\2\u00e5\u00e6\5\2\2\2\u00e6"+
		"\u00e7\7\6\2\2\u00e7\u00e9\3\2\2\2\u00e8\u00db\3\2\2\2\u00e8\u00de\3\2"+
		"\2\2\u00e8\u00e1\3\2\2\2\u00e8\u00e4\3\2\2\2\u00e9%\3\2\2\2\u00ea\u00ec"+
		"\7\26\2\2\u00eb\u00ea\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00eb\3\2\2\2"+
		"\u00ed\u00ee\3\2\2\2\u00ee\'\3\2\2\2\34*\63<Jjx|\u0080\u0085\u0088\u008b"+
		"\u008e\u0094\u009b\u009f\u00a6\u00ad\u00b1\u00b8\u00bf\u00c3\u00ca\u00d1"+
		"\u00d5\u00e8\u00ed";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}