// Generated from /home/remi/Git/ekylibre/zero-android/app/src/larrere/java/ekylibre/util/antlr4/QueryLanguage.g4 by ANTLR 4.7.2
package ekylibre.util.antlr4;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class QueryLanguageLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, SPACE=21, CHAR=22, DIGIT=23;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "T__18", "T__19", "SPACE", "CHAR", "DIGIT"
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


	public QueryLanguageLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "QueryLanguage.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\31\u009c\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2"+
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n"+
		"\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3"+
		"\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3"+
		"\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3"+
		"\24\3\25\3\25\3\26\7\26\u0094\n\26\f\26\16\26\u0097\13\26\3\27\3\27\3"+
		"\30\3\30\2\2\31\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\3\2\5\5\2\13\f"+
		"\16\17\"\"\3\2c|\3\2\62;\2\u009c\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
		"\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\3\61\3\2\2\2\5\65\3\2\2\2\78\3\2\2\2"+
		"\t:\3\2\2\2\13<\3\2\2\2\r?\3\2\2\2\17D\3\2\2\2\21L\3\2\2\2\23Q\3\2\2\2"+
		"\25V\3\2\2\2\27]\3\2\2\2\31f\3\2\2\2\33j\3\2\2\2\35q\3\2\2\2\37z\3\2\2"+
		"\2!\u0084\3\2\2\2#\u0088\3\2\2\2%\u008a\3\2\2\2\'\u008c\3\2\2\2)\u0090"+
		"\3\2\2\2+\u0095\3\2\2\2-\u0098\3\2\2\2/\u009a\3\2\2\2\61\62\7c\2\2\62"+
		"\63\7p\2\2\63\64\7f\2\2\64\4\3\2\2\2\65\66\7q\2\2\66\67\7t\2\2\67\6\3"+
		"\2\2\289\7*\2\29\b\3\2\2\2:;\7+\2\2;\n\3\2\2\2<=\7k\2\2=>\7u\2\2>\f\3"+
		"\2\2\2?@\7k\2\2@A\7u\2\2AB\7p\2\2BC\7v\2\2C\16\3\2\2\2DE\7f\2\2EF\7g\2"+
		"\2FG\7t\2\2GH\7k\2\2HI\7x\2\2IJ\7g\2\2JK\7u\2\2K\20\3\2\2\2LM\7h\2\2M"+
		"N\7t\2\2NO\7q\2\2OP\7o\2\2P\22\3\2\2\2QR\7f\2\2RS\7q\2\2ST\7p\2\2TU\7"+
		"v\2\2U\24\3\2\2\2VW\7f\2\2WX\7g\2\2XY\7t\2\2YZ\7k\2\2Z[\7x\2\2[\\\7g\2"+
		"\2\\\26\3\2\2\2]^\7k\2\2^_\7p\2\2_`\7e\2\2`a\7n\2\2ab\7w\2\2bc\7f\2\2"+
		"cd\7g\2\2de\7u\2\2e\30\3\2\2\2fg\7j\2\2gh\7c\2\2hi\7u\2\2i\32\3\2\2\2"+
		"jk\7h\2\2kl\7t\2\2lm\7q\2\2mn\7|\2\2no\7g\2\2op\7p\2\2p\34\3\2\2\2qr\7"+
		"x\2\2rs\7c\2\2st\7t\2\2tu\7k\2\2uv\7c\2\2vw\7d\2\2wx\7n\2\2xy\7g\2\2y"+
		"\36\3\2\2\2z{\7k\2\2{|\7p\2\2|}\7f\2\2}~\7k\2\2~\177\7e\2\2\177\u0080"+
		"\7c\2\2\u0080\u0081\7v\2\2\u0081\u0082\7q\2\2\u0082\u0083\7t\2\2\u0083"+
		" \3\2\2\2\u0084\u0085\7e\2\2\u0085\u0086\7c\2\2\u0086\u0087\7p\2\2\u0087"+
		"\"\3\2\2\2\u0088\u0089\7.\2\2\u0089$\3\2\2\2\u008a\u008b\7a\2\2\u008b"+
		"&\3\2\2\2\u008c\u008d\7p\2\2\u008d\u008e\7q\2\2\u008e\u008f\7v\2\2\u008f"+
		"(\3\2\2\2\u0090\u0091\7\"\2\2\u0091*\3\2\2\2\u0092\u0094\t\2\2\2\u0093"+
		"\u0092\3\2\2\2\u0094\u0097\3\2\2\2\u0095\u0093\3\2\2\2\u0095\u0096\3\2"+
		"\2\2\u0096,\3\2\2\2\u0097\u0095\3\2\2\2\u0098\u0099\t\3\2\2\u0099.\3\2"+
		"\2\2\u009a\u009b\t\4\2\2\u009b\60\3\2\2\2\4\2\u0095\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}