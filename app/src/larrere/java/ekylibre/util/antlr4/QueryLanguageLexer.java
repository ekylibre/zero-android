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
		T__17=18, SPACE=19, CHAR=20, DIGIT=21;
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
			"T__17", "SPACE", "CHAR", "DIGIT"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\27\u00a7\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3"+
		"\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3"+
		"\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3"+
		"\17\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3"+
		"\25\3\26\3\26\2\2\27\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27\3\2\5\5\2\13\f\16"+
		"\17\"\"\3\2c|\3\2\62;\2\u00a6\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2"+
		"+\3\2\2\2\3-\3\2\2\2\5\63\3\2\2\2\78\3\2\2\2\t:\3\2\2\2\13<\3\2\2\2\r"+
		"@\3\2\2\2\17F\3\2\2\2\21T\3\2\2\2\23f\3\2\2\2\25p\3\2\2\2\27u\3\2\2\2"+
		"\31}\3\2\2\2\33\u0087\3\2\2\2\35\u0092\3\2\2\2\37\u0097\3\2\2\2!\u0099"+
		"\3\2\2\2#\u009d\3\2\2\2%\u009f\3\2\2\2\'\u00a1\3\2\2\2)\u00a3\3\2\2\2"+
		"+\u00a5\3\2\2\2-.\7\"\2\2./\7c\2\2/\60\7p\2\2\60\61\7f\2\2\61\62\7\"\2"+
		"\2\62\4\3\2\2\2\63\64\7\"\2\2\64\65\7q\2\2\65\66\7t\2\2\66\67\7\"\2\2"+
		"\67\6\3\2\2\289\7*\2\29\b\3\2\2\2:;\7+\2\2;\n\3\2\2\2<=\7k\2\2=>\7u\2"+
		"\2>?\7\"\2\2?\f\3\2\2\2@A\7k\2\2AB\7u\2\2BC\7p\2\2CD\7v\2\2DE\7\"\2\2"+
		"E\16\3\2\2\2FG\7f\2\2GH\7g\2\2HI\7t\2\2IJ\7k\2\2JK\7x\2\2KL\7g\2\2LM\7"+
		"u\2\2MN\7\"\2\2NO\7h\2\2OP\7t\2\2PQ\7q\2\2QR\7o\2\2RS\7\"\2\2S\20\3\2"+
		"\2\2TU\7f\2\2UV\7q\2\2VW\7p\2\2WX\7v\2\2XY\7\"\2\2YZ\7f\2\2Z[\7g\2\2["+
		"\\\7t\2\2\\]\7k\2\2]^\7x\2\2^_\7g\2\2_`\7\"\2\2`a\7h\2\2ab\7t\2\2bc\7"+
		"q\2\2cd\7o\2\2de\7\"\2\2e\22\3\2\2\2fg\7k\2\2gh\7p\2\2hi\7e\2\2ij\7n\2"+
		"\2jk\7w\2\2kl\7f\2\2lm\7g\2\2mn\7u\2\2no\7\"\2\2o\24\3\2\2\2pq\7j\2\2"+
		"qr\7c\2\2rs\7u\2\2st\7\"\2\2t\26\3\2\2\2uv\7h\2\2vw\7t\2\2wx\7q\2\2xy"+
		"\7|\2\2yz\7g\2\2z{\7p\2\2{|\7\"\2\2|\30\3\2\2\2}~\7x\2\2~\177\7c\2\2\177"+
		"\u0080\7t\2\2\u0080\u0081\7k\2\2\u0081\u0082\7c\2\2\u0082\u0083\7d\2\2"+
		"\u0083\u0084\7n\2\2\u0084\u0085\7g\2\2\u0085\u0086\7\"\2\2\u0086\32\3"+
		"\2\2\2\u0087\u0088\7k\2\2\u0088\u0089\7p\2\2\u0089\u008a\7f\2\2\u008a"+
		"\u008b\7k\2\2\u008b\u008c\7e\2\2\u008c\u008d\7c\2\2\u008d\u008e\7v\2\2"+
		"\u008e\u008f\7q\2\2\u008f\u0090\7t\2\2\u0090\u0091\7\"\2\2\u0091\34\3"+
		"\2\2\2\u0092\u0093\7e\2\2\u0093\u0094\7c\2\2\u0094\u0095\7p\2\2\u0095"+
		"\u0096\7\"\2\2\u0096\36\3\2\2\2\u0097\u0098\7.\2\2\u0098 \3\2\2\2\u0099"+
		"\u009a\7p\2\2\u009a\u009b\7q\2\2\u009b\u009c\7v\2\2\u009c\"\3\2\2\2\u009d"+
		"\u009e\7\"\2\2\u009e$\3\2\2\2\u009f\u00a0\7a\2\2\u00a0&\3\2\2\2\u00a1"+
		"\u00a2\t\2\2\2\u00a2(\3\2\2\2\u00a3\u00a4\t\3\2\2\u00a4*\3\2\2\2\u00a5"+
		"\u00a6\t\4\2\2\u00a6,\3\2\2\2\3\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}