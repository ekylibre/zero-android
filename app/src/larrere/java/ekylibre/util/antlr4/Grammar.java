package ekylibre.util.antlr4;

import android.util.Log;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ekylibre.util.antlr4.QueryLanguageParser.AbilityContext;
import ekylibre.util.antlr4.QueryLanguageParser.Variety_nameContext;
import ekylibre.util.ontology.Ontology;
import ekylibre.zero.inter.model.GenericItem;

import static ekylibre.zero.inter.InterActivity.ontology;

public class Grammar {

    private static final String TAG = "Grammar";

    public List<GenericItem> getFilteredItems(String procedureFilter, List<GenericItem> items, String search) {

//        Pattern pattern = Pattern.compile("can [a-z]*\\((.*)\\)");
        List<GenericItem> list = new ArrayList<>();

        // Prepare required abilities variants
        List<List<String>> requiredAbilities = computeAbilities(procedureFilter, true);

        // Loop over all available items
        outer: for (GenericItem item : items) {
            if (item.abilities != null) {

                // Check for all mandatory abilities for the procedure
                abilityLoop: for (List<String> requiredAbility : requiredAbilities) {

                    // Loop each item ability to check if match required ability
                    for (String itemAbility : item.abilities) {

                        // If one item ability fullfil current mandatory ability, continue to next requirement
                        if (requiredAbility.contains(itemAbility))
                            continue abilityLoop;
                    }

                    // If this code is reached, mandatory ability requirement is not fullfiled
                    continue outer;

                    // Check if we have an ability parameter
//                    Matcher matcher = pattern.matcher(requiredAbility);
//                    if (matcher.matches()) {
//                        String parameter = matcher.group(1);
//                        Log.i(TAG, "ability param --> " + parameter);
//
//                        // Loop over required abilities variants
//                        List<String> parameterVariants = Ontology.findInTree(ontology, parameter);
//                        Log.e(TAG, "Variants --> " + Ontology.findInTree(ontology, "equipment"));
//
//                        Log.i(TAG, "parameterVariants --> " + parameterVariants);
//                        for (String parameterVariant : parameterVariants) {
//
//                            String abilityVariant = matcher.group(0).replace(parameter, parameterVariant);
//                            Log.i(TAG, "abilityVariant --> " + abilityVariant);
//

                }

                // Abilities test passed ! Do things here with matching item :
                Log.d(TAG, "Item ability -> " + Arrays.toString(item.abilities));
                Log.d(TAG, "Required ability -> " + requiredAbilities);
                if (search != null) {
                    String filterText = StringUtils.stripAccents(search.toLowerCase());
                    String name = StringUtils.stripAccents(item.name.toLowerCase());
                    String number = StringUtils.stripAccents(item.number.toLowerCase());
                    if (name.contains(filterText) || number.contains(filterText))
                        list.add(item);
                } else
                    list.add(item);
            }
        }

        // Return the selected items after browsing all items
        return list;
    }

    public List<List<String>> computeAbilities(String input, boolean getLowerAbilities) {  //boolean getLowerAbilities
        Ontology ontologyInstance = new Ontology();
        List<List<String>> output = new ArrayList<>();
        QueryLanguageLexer lexer = new QueryLanguageLexer(CharStreams.fromString(input));
        QueryLanguageParser parser = new QueryLanguageParser(new CommonTokenStream(lexer));
        ParseTreeWalker.DEFAULT.walk(new QueryLanguageBaseListener() {
            @Override
            public void enterVariety_name(Variety_nameContext ctx) {
                super.enterVariety_name(ctx);
                if (!getLowerAbilities)
                    output.add(Collections.singletonList("is " + ctx.getText()));
            }
//            @Override
//            public void enterAbility_name(QueryLanguageParser.Ability_nameContext ctx) {
//                super.enterAbility_name(ctx);
//                if (BuildConfig.DEBUG)
//                    Log.e(TAG, "ability name - > " + ctx.name().getText());
//                output.add(ctx.name().getText());
//            }
            @Override
            public void enterAbility(AbilityContext ctx) {
                super.enterAbility(ctx);

                if (getLowerAbilities && ctx.ability_parameters() != null) {

                    String ability = ctx.ability_name().name().getText();

                    String abilityParameter = ctx.ability_parameters().ability_argument().get(0).getText();
                    Log.d(TAG, "Ability parameter = "+ abilityParameter);

                    List<String> parameterVariants = ontologyInstance.findInTree(ontology, abilityParameter);
                    List<String> newList = new ArrayList<>();
                    for (String param : parameterVariants)
                        newList.add("can " + ability + "(" + param + ")");
                    output.add(newList);

                } else {
                    output.add(Collections.singletonList("can " + ctx.getText()));
                }
            }
        }, parser.boolean_expression());
        return output;
    }
}
