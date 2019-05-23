package ekylibre.util.antlr4;

import android.util.Log;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.List;

import ekylibre.zero.BuildConfig;

public class Grammar {

    private static final String TAG = "GrammarParser";

    public static List<String> parse(String input) {
        List<String> output = new ArrayList<>();
        QueryLanguageLexer lexer = new QueryLanguageLexer(CharStreams.fromString(input));
        QueryLanguageParser parser = new QueryLanguageParser(new CommonTokenStream(lexer));
        ParseTreeWalker.DEFAULT.walk(new QueryLanguageBaseListener() {
            @Override
            public void enterVariety_name(QueryLanguageParser.Variety_nameContext ctx) {
                super.enterVariety_name(ctx);
                if (BuildConfig.DEBUG)
                    Log.e(TAG, "essence name - > " + ctx.name().getText());
                output.add(ctx.name().getText());
            }
            @Override
            public void enterAbility_name(QueryLanguageParser.Ability_nameContext ctx) {
                super.enterAbility_name(ctx);
                if (BuildConfig.DEBUG)
                    Log.e(TAG, "ability name - > " + ctx.name().getText());
                output.add(ctx.name().getText());
            }
        }, parser.boolean_expression());
        return output;
    }
}
