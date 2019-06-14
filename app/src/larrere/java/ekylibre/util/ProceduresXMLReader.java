package ekylibre.util;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ekylibre.util.pojo.GenericEntity;
import ekylibre.util.pojo.HandlerEntity;
import ekylibre.util.pojo.ProcedureEntity;
import ekylibre.zero.BuildConfig;


public class ProceduresXMLReader {

    private static final String TAG = "XMLParser";
    private static final String ns = null;
    private boolean inGroup = false;
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
                procedureEntity.categories = String.valueOf(parser.getAttributeValue(ns, "categories")).split(",");

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
        // Parser is in a group
        inGroup = true;

        procedureEntity.group = parser.getAttributeValue(ns, "name");

        // Skip inside balises
        do {
            parser.nextTag();
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            switch (parser.getName()) {
                case "target":
                    procedureEntity.target.add(readTarget(parser));
                    break;
                case "input":
                    procedureEntity.input.add(readInput(parser));
                    break;
                case "output":
                    procedureEntity.output.add(readOuput(parser));
                    break;
                default:
                    skip(parser);
                    break;
            }

        } while (!parser.getName().equals("group"));

        parser.require(XmlPullParser.END_TAG, ns, "group");
        // Parser quit the group
        inGroup = false;
    }

    private GenericEntity readTarget(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "target");
        GenericEntity targetEntity = new GenericEntity();

        targetEntity.name = parser.getAttributeValue(ns, "name");
        targetEntity.filter = parser.getAttributeValue(ns, "filter");
        targetEntity.cardinality = parser.getAttributeValue(ns, "cardinality");
        if (inGroup)
            targetEntity.group = procedureEntity.group;

        // Skip attribute balises for now
        while (true) {
            parser.nextTag();
            String name = parser.getName();
            if (name.equals("target"))
                break;
        }

        parser.require(XmlPullParser.END_TAG, ns, "target");

        return targetEntity;
    }

    private GenericEntity readInput(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "input");
        GenericEntity inputEntity = new GenericEntity();

        inputEntity.name = parser.getAttributeValue(ns, "name");
        inputEntity.filter = parser.getAttributeValue(ns, "filter");
        inputEntity.cardinality = parser.getAttributeValue(ns, "cardinality");
        if (inGroup)
            inputEntity.group = procedureEntity.group;

        do {
            parser.nextTag();

            // Continue to next tag if not START_TAG
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (parser.getName().equals("handler"))
                inputEntity.handler.add(readHandler(parser));
            else
                skip(parser);

        } while (!parser.getName().equals("input"));

        parser.require(XmlPullParser.END_TAG, ns, "input");

        return inputEntity;
    }

    private HandlerEntity readAttribute(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "attribute");
        HandlerEntity handlerEntity = new HandlerEntity();

        handlerEntity.name = parser.getAttributeValue(ns, "name");
        handlerEntity.indicator = parser.getAttributeValue(ns, "indicator");
        handlerEntity.unit = parser.getAttributeValue(ns, "unit");

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "handler");

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Name = " + handlerEntity.name);
            Log.i(TAG, "Indicator = " + handlerEntity.indicator);
            Log.i(TAG, "Unit = " + handlerEntity.unit);
        }

        return handlerEntity;
    }

    private HandlerEntity readHandler(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "handler");
        HandlerEntity handlerEntity = new HandlerEntity();

        handlerEntity.name = parser.getAttributeValue(ns, "name");
        handlerEntity.indicator = parser.getAttributeValue(ns, "indicator");
        handlerEntity.unit = parser.getAttributeValue(ns, "unit");

        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "handler");

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Name = " + handlerEntity.name);
            Log.i(TAG, "Indicator = " + handlerEntity.indicator);
            Log.i(TAG, "Unit = " + handlerEntity.unit);
        }

        return handlerEntity;
    }

        private GenericEntity readOuput(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, ns, "output");
        GenericEntity outputEntity = new GenericEntity();

        outputEntity.name = parser.getAttributeValue(ns, "name");
        outputEntity.filter = parser.getAttributeValue(ns, "filter");
        outputEntity.cardinality = parser.getAttributeValue(ns, "cardinality");
        if (inGroup)
            outputEntity.group = procedureEntity.group;

        do {
            parser.nextTag();

            // Continue to next tag if not START_TAG
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (parser.getName().equals("handler"))
                outputEntity.handler.add(readHandler(parser));
            else
                skip(parser);

        } while (!parser.getName().equals("output"));

        parser.require(XmlPullParser.END_TAG, ns, "output");

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

        return toolEntity;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if (BuildConfig.DEBUG)
            Log.d(TAG, "skipping..." + parser.getName());

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
