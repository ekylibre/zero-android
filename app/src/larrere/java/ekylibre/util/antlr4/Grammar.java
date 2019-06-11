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

public class Grammar {

    private static final String TAG = "Grammar";

//    public List<GenericItem> getFilteredItems(String procedureFilter, List<GenericItem> items, String search) {
//
//        List<GenericItem> list = new ArrayList<>();
//
//        // Prepare required abilities variants
//        List<String> requiredAbilities = computeAbilitiesPhrase(procedureFilter, true);
//
//        // Loop over all available items
//        outer: for (GenericItem item : items) {
//            if (item.abilities != null) {
//
//                // Check all procedure mandatory abilities one by one
//                required: for (String requiredAbility : requiredAbilities) {
//                    List<String> extendedRequiredAbilities = Ontology.findParentsInRealm(requiredAbility);
//                    for (String extendedRequiredAbility : extendedRequiredAbilities)
//                        if (Arrays.asList(item.abilities).contains(extendedRequiredAbility))
//                            continue required;
//
//                    // If here, none of current extended ability variant match, skip item
//                    Log.e(TAG, Arrays.toString(item.abilities) + " does not contains [" + requiredAbility + "]");
//                    continue outer;
//                }
//
//                // Abilities test passed ! Do things here with matching item :
//                Log.i(TAG, "["+item.name+"] abilities -> " + Arrays.toString(item.abilities));
//                Log.d(TAG, "Required ability -> " + requiredAbilities);
//
//                if (search != null) {
//                    String filterText = StringUtils.stripAccents(search.toLowerCase());
//                    String name = StringUtils.stripAccents(item.name.toLowerCase());
//                    String number = StringUtils.stripAccents(item.number.toLowerCase());
//                    if (name.contains(filterText) || number.contains(filterText))
//                        list.add(item);
//                } else
//                    list.add(item);
//            }
//        }
//
//        // Return the selected items after browsing all items
//        return list;
//    }

//
//    private List<List<String>> computeAbilitiesPhrase(String input, boolean withParents) {  //boolean getLowerAbilities
//        List<String> output = new ArrayList<>();
//        QueryLanguageLexer lexer = new QueryLanguageLexer(CharStreams.fromString(input));
//        QueryLanguageParser parser = new QueryLanguageParser(new CommonTokenStream(lexer));
//        ParseTreeWalker.DEFAULT.walk(new QueryLanguageBaseListener() {
//            @Override
//            public void enterVariety_name(Variety_nameContext ctx) {
//                super.enterVariety_name(ctx);
//
//                if (withParents) {
//                    List<String> varietyParents = Ontology.findParentsInRealm(ctx.getText());
//                    for (String variety : varietyParents)
//                        output.add("is " + variety);
//                } else
//                    output.add("is " + ctx.getText());
//            }
//            @Override
//            public void enterAbility(AbilityContext ctx) {
//                super.enterAbility(ctx);
//
//                if (withParents && ctx.ability_parameters() != null) {
//
//                    String ability = ctx.ability_name().name().getText();
//
//                    String abilityParameter = ctx.ability_parameters().ability_argument().get(0).getText();
//
//                    List<String> parameterVariants = Ontology.findParentsInRealm(abilityParameter);
//                    for (String param : parameterVariants)
//                        output.add("can " + ability + "(" + param + ")");
//
//                } else {
//                    output.add("can " + ctx.getText());
//                }
//            }
//        }, parser.boolean_expression());
//        return output;
//    }

    public static List<GenericItem> getFilteredItems(String procedureFilter, List<GenericItem> items, String search) {

        List<GenericItem> list = new ArrayList<>();

        // Prepare required abilities variants
        List<List<String>> requiredAbilities = computeAbilitiesPhrase(procedureFilter);

        Log.e(TAG, "Mandatory abilities --> " + requiredAbilities);

        // Loop over all available items
        itemLoop: for (GenericItem item : items) {
            if (item.abilities != null) {

                // Check all procedure mandatory abilities one by one
                abilityLoop: for (List<String> extendedAbility : requiredAbilities) {

                    for (String ability : item.abilities)
                        if (extendedAbility.contains(ability))
                            continue abilityLoop;

                    // If here reached, none of current extended ability variant match, skip item
                    continue itemLoop;
                }

                // Abilities test passed ! Do things here with matching item :
                Log.i(TAG, "["+item.name+"] abilities -> " + Arrays.toString(item.abilities));
                Log.d(TAG, "Required ability -> " + requiredAbilities);

                if (search != null) {
                    String filterText = StringUtils.stripAccents(search.toLowerCase());
                    String name = StringUtils.stripAccents(item.name.toLowerCase());
                    String number = StringUtils.stripAccents(item.number.toLowerCase());
                    String workNumber = "";
                    if (item.workNumber != null)
                        workNumber = StringUtils.stripAccents(item.workNumber.toLowerCase());
                    if (name.contains(filterText) || number.contains(filterText) || workNumber.contains(filterText))
                        list.add(item);
                } else {
                    list.add(item);
                }
            }
        }

        // Return the selected items after browsing all items
        return list;
    }

    private static List<List<String>> computeAbilitiesPhrase(String input) {  //boolean getLowerAbilities

        List<List<String>> output = new ArrayList<>();

        QueryLanguageLexer lexer = new QueryLanguageLexer(CharStreams.fromString(input));
        QueryLanguageParser parser = new QueryLanguageParser(new CommonTokenStream(lexer));
        ParseTreeWalker.DEFAULT.walk(new QueryLanguageBaseListener() {

            @Override
            public void enterEssence(QueryLanguageParser.EssenceContext ctx) {
                super.enterEssence(ctx);

                output.add(Collections.singletonList(ctx.getText()));

            }

            @Override
            public void enterAbility(AbilityContext ctx) {
                super.enterAbility(ctx);

                String ability = ctx.ability_name().name().getText();
                List<String> extendedVariants = new ArrayList<>();

                if (ctx.ability_parameters() != null) {

                    String abilityParameter = ctx.ability_parameters().ability_argument().get(0).getText();
                    List<String> parameterVariants = Ontology.findParentsInRealm(abilityParameter);

                    for (String param : parameterVariants)
                        extendedVariants.add("can " + ability + "(" + param + ")");

                    output.add(extendedVariants);

                } else {
                    output.add(Collections.singletonList("can " + ability));
                }
            }
        }, parser.boolean_expression());

        return output;
    }

    public static List<String> computeItemAbilities(String input) {  //boolean getLowerAbilities

        List<String> output = new ArrayList<>();

        QueryLanguageLexer lexer = new QueryLanguageLexer(CharStreams.fromString(input));
        QueryLanguageParser parser = new QueryLanguageParser(new CommonTokenStream(lexer));
        ParseTreeWalker.DEFAULT.walk(new QueryLanguageBaseListener() {

            @Override
            public void enterEssence(QueryLanguageParser.EssenceContext ctx) {
                super.enterEssence(ctx);

                List<String> varietyParents = Ontology.findParentsInRealm(ctx.variety_name().getText());

                for (String variety : varietyParents)
                    output.add("is " + variety);
            }

        }, parser.boolean_expression());

        return output;
    }
}
