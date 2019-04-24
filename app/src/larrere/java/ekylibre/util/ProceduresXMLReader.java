package ekylibre.util;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ekylibre.util.pojo.GenericEntity;
import ekylibre.util.pojo.InputEntity;
import ekylibre.util.pojo.OutputEntity;
import ekylibre.util.pojo.ProcedureEntity;
import ekylibre.util.pojo.TargetEntity;
import ekylibre.zero.BuildConfig;


public class ProceduresXMLReader {

    private static final String TAG = "XMLParser";
    private static final String ns = null;
    private ProcedureEntity procedureEntity;

    public ProceduresXMLReader() {}

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
                    Log.d(TAG, "parsing [procedure] node...");
                    Log.d(TAG, "Procedure name = " + procedureEntity.name);
                    Log.d(TAG, "Categories = " + procedureEntity.categories);
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

            String name = parser.getName();

            switch (name) {
                case "doer":
                    procedureEntity.doer.add(readDoer(parser));
                    break;
                case "target":
                    procedureEntity.target.add(readTarget(parser));
                    break;
                case "input":
                    procedureEntity.input.add(readInput(parser));
                    break;
                case "output":
                    procedureEntity.output.add(readOuput(parser));
                    break;
                case "tool":
                    procedureEntity.tool.add(readTool(parser));
                    break;
                case "group":
                    readGroup(parser);
                    break;
                default:
                    skip(parser);
                    break;
            }
        }
    }

    private void readGroup(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "group");

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "parsing [group] node...");
        }

        String name = parser.getName();

        if (name.equals("zone")) {

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
                    case "input":
                        procedureEntity.input.add(readInput(parser));
                        break;
                    case "output":
                        procedureEntity.output.add(readOuput(parser));
                        break;
                    case "tool":
                        procedureEntity.tool.add(readTool(parser));
                        break;
                    default:
                        skip(parser);
                        break;
                }
            }
        } else
            skip(parser);
    }

    private GenericEntity readTarget(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "target");
        GenericEntity targetEntity = new GenericEntity();

        targetEntity.name = parser.getAttributeValue(ns, "name");
        targetEntity.filter = parser.getAttributeValue(ns, "filter");
        targetEntity.cardinality = parser.getAttributeValue(ns, "cardinality");

        // Skip attribute balises for now
        while (true) {
            parser.nextTag();
            String name = parser.getName();
            if (BuildConfig.DEBUG)
                Log.d(TAG, "node --> " + name);
            if (name.equals("target"))
                break;
        }

        parser.require(XmlPullParser.END_TAG, ns, "target");

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "parsing [target] node...");
            Log.d(TAG, "Taget name = " + targetEntity.name);
            Log.d(TAG, "Target filter = " + targetEntity.filter);
        }

        return targetEntity;
    }

    private InputEntity readInput(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "input");
        InputEntity inputEntity = new InputEntity();

        inputEntity.name = parser.getAttributeValue(ns, "name");
        inputEntity.filter = parser.getAttributeValue(ns, "filter");

        // Skip attribute balises for now
        while (true) {
            parser.nextTag();
            String name = parser.getName();
            if (BuildConfig.DEBUG)
                Log.d(TAG, "node --> " + name);
            if (name.equals("input"))
                break;
        }

        parser.require(XmlPullParser.END_TAG, ns, "input");

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "parsing [input] node...");
            Log.d(TAG, "Input name = " + inputEntity.name);
            Log.d(TAG, "Input filter = " + inputEntity.filter);
        }

        return inputEntity;
    }

    private OutputEntity readOuput(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "output");
        OutputEntity outputEntity = new OutputEntity();

        outputEntity.name = parser.getAttributeValue(ns, "name");
        outputEntity.filter = parser.getAttributeValue(ns, "filter");

        // Skip attribute balises for now
        while (true) {
            parser.nextTag();
            String name = parser.getName();
            if (BuildConfig.DEBUG)
                Log.d(TAG, "node --> " + name);
            if (name.equals("output"))
                break;
        }

        parser.require(XmlPullParser.END_TAG, ns, "output");

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "parsing [output] node...");
            Log.d(TAG, "output name = " + outputEntity.name);
            Log.d(TAG, "output filter = " + outputEntity.filter);
        }

        return outputEntity;
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
            Log.d(TAG, "parsing [doer] node...");
            Log.d(TAG, "Doer name = " + doerEntity.name);
            Log.d(TAG, "Doer filter = " + doerEntity.filter);
            Log.d(TAG, "Doer cardinality = " + doerEntity.cardinality);
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
            Log.d(TAG, "parsing [tool] node...");
            Log.d(TAG, "Tool name = " + toolEntity.name);
            Log.d(TAG, "Tool filter = " + toolEntity.filter);
            Log.d(TAG, "Tool cardinality = " + toolEntity.cardinality);
        }

        return toolEntity;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "skipping...");

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
