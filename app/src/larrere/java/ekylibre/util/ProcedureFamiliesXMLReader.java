package ekylibre.util;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.util.Pair;
import ekylibre.zero.BuildConfig;


public class ProcedureFamiliesXMLReader {

    private static final String TAG = "XMLParser";
    private static final String ns = null;
    private static Map<String, List<Pair<String,String>>> map;
    private Context context;

    public ProcedureFamiliesXMLReader(Context context) {
        this.context = context;
    }

    public Map<String, List<Pair<String,String>>> parse(InputStream in) throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private Map<String, List<Pair<String,String>>> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        Log.e(TAG, "parsing nomenclatures...");

        map = new HashMap<>();

        parser.require(XmlPullParser.START_TAG, ns, "nomenclatures");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Starts by looking for the entry tag
            if (parser.getName().equals("nomenclature")
                    && parser.getAttributeValue(ns, "name").equals("procedure_categories")) {

                parser.require(XmlPullParser.START_TAG, ns, "nomenclature");

                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG)
                        continue;

                    if (parser.getName().equals("items"))
                        readItem(parser);
                    else
                        skip(parser);
                }

            } else {
                skip(parser);
            }
        }

        return map;
    }

    private void readItem(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "items");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (parser.getName().equals("item")) {
                parser.require(XmlPullParser.START_TAG, ns, "item");

                String name = parser.getAttributeValue(ns, "name");
                String activityFamily = parser.getAttributeValue(ns, "activity_family");
                String[] families = activityFamily.split(", ");

                int resId = context.getResources().getIdentifier(name, "string", context.getPackageName());
                Pair<String,String> categoryPair = Pair.create(name, context.getString(resId));

                for (String family : families) {
                    if (map.containsKey(family)) {
                        List<Pair<String,String>> categoryList = map.get(family);
                        if (categoryList == null)
                            categoryList = new ArrayList<>();
                        categoryList.add(categoryPair);
                        map.put(family, categoryList);
                    } else {
                        map.put(family, new ArrayList<>(Collections.singletonList(categoryPair)));
                    }
                }

                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, ns, "item");

                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "Item name = " + name);
                }

            } else {
                skip(parser);
            }

        }
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if (BuildConfig.DEBUG)
            Log.i(TAG, "skipping...");

        if (parser.getEventType() != XmlPullParser.START_TAG)
            throw new IllegalStateException();

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
