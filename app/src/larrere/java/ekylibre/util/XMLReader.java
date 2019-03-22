package ekylibre.util;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ekylibre.util.pojo.GenericEntity;
import ekylibre.util.pojo.ProcedureEntity;
import ekylibre.util.pojo.TargetEntity;
import ekylibre.zero.BuildConfig;


public class XMLReader {

    private static final String TAG = "XMLParser";
    private static final String ns = null;
    private ProcedureEntity procedureEntity;

    public XMLReader() {}

    public ProcedureEntity parse(InputStream in) throws XmlPullParserException, IOException {
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

    private ProcedureEntity readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        procedureEntity = new ProcedureEntity();

        parser.require(XmlPullParser.START_TAG, ns, "procedures");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Name of the current node
            String name = parser.getName();

            // Starts by looking for the entry tag
            if (name.equals("procedure")) {

                // Get categories attribute
                procedureEntity.name = parser.getAttributeValue(ns, "name");
                procedureEntity.categories = parser.getAttributeValue(ns, "categories");

                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "parsing [procedure] node...");
                    Log.i(TAG, "Procedure name = " + procedureEntity.name);
                    Log.i(TAG, "Categories = " + procedureEntity.categories);
                }

                readProcedure(parser);

            } else {
                skip(parser);
            }
        }

        return procedureEntity;
    }

    /**
     * Parses the contents of an entry.
     * If it encounters a title, summary, or link tag, hands them off
     * to their respective "read" methods for processing. Otherwise, skips the tag.
     */
    private void readProcedure(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "procedure");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            String name = parser.getName();

            if (name.equals("parameters"))
                readParameters(parser);
            else
                skip(parser);
        }
    }


    private void readParameters(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "parameters");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            switch (parser.getName()) {
                case "doer":
                    procedureEntity.doer.add(readDoer(parser));
                    break;
                case "target":
                    procedureEntity.target.add(readTarget(parser));
                    break;
                case "tool":
                    procedureEntity.tool.add(readTool(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
    }

    private TargetEntity readTarget(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "target");
        TargetEntity targetEntity = new TargetEntity();

        targetEntity.name = parser.getAttributeValue(ns, "name");
        targetEntity.filter = parser.getAttributeValue(ns, "filter");

        // Skip attribute balises for now
        while (true) {
            parser.nextTag();
            String name = parser.getName();
            if (BuildConfig.DEBUG)
                Log.i(TAG, "node --> " + name);
            if (name.equals("target"))
                break;
        }

        parser.require(XmlPullParser.END_TAG, ns, "target");

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "parsing [target] node...");
            Log.i(TAG, "Taget name = " + targetEntity.name);
            Log.i(TAG, "Target filter = " + targetEntity.filter);
        }

        return targetEntity;
    }

    private GenericEntity readDoer(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "doer");
        GenericEntity doerEntity = new GenericEntity();

        doerEntity.name = parser.getAttributeValue(ns, "name");
        doerEntity.filter = parser.getAttributeValue(ns, "filter");
        doerEntity.cardinality = parser.getAttributeValue(ns, "cardinality");

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "doer");

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "parsing [doer] node...");
            Log.i(TAG, "Doer name = " + doerEntity.name);
            Log.i(TAG, "Doer filter = " + doerEntity.filter);
            Log.i(TAG, "Doer cardinality = " + doerEntity.cardinality);
        }

        return doerEntity;
    }

    private GenericEntity readTool(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "tool");
        GenericEntity toolEntity = new GenericEntity();

        toolEntity.name = parser.getAttributeValue(ns, "name");
        toolEntity.filter = parser.getAttributeValue(ns, "filter");
        toolEntity.cardinality = parser.getAttributeValue(ns, "cardinality");

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "tool");

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "parsing [tool] node...");
            Log.i(TAG, "Tool name = " + toolEntity.name);
            Log.i(TAG, "Tool filter = " + toolEntity.filter);
            Log.i(TAG, "Tool cardinality = " + toolEntity.cardinality);
        }

        return toolEntity;
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
