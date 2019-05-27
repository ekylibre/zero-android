package ekylibre.util.xml;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ekylibre.util.ontology.Node;

public class XMLReader {

    private static final String TAG = "XMLReader";
    private Node<String> root;
    private boolean notCreated;

    public XMLReader() { }

    public Node<String> parse(InputStream inputStream) throws XmlPullParserException, IOException {

        // "product" is the root note of our tree
        root = new Node<>("product");

        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            readFeed(parser);

        } finally {
            inputStream.close();
        }

        return root;
    }

    private void readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, null, "nomenclatures");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            // Starts by looking for the entry tag
            if (parser.getName().equals("nomenclature")
                    && parser.getAttributeValue(null, "name").equals("varieties")) {

                parser.require(XmlPullParser.START_TAG, null, "nomenclature");

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
    }

    private void readItem(XmlPullParser parser) throws IOException, XmlPullParserException {

        parser.require(XmlPullParser.START_TAG, null, "items");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;

            if (parser.getName().equals("item")) {
                parser.require(XmlPullParser.START_TAG, null, "item");

                String name = parser.getAttributeValue(null, "name");
                String parent = parser.getAttributeValue(null, "parent");

                Log.i(TAG, name + " <- " + parent);

                // Continue to next item if this is the root node
                if (parent != null) {
                    notCreated = true;
                    findParent(name, parent, root);
                }

                parser.nextTag();
                parser.require(XmlPullParser.END_TAG, null, "item");

            } else {
                skip(parser);
            }

        }
    }

    private void findParent(String name, String parent, Node<String> node) {

        // First check if current node is matching the request = is parent
        if (node.getName().equals(parent)) {
            Log.e(TAG, "\tFound !");
            node.addChild(new Node<>(name));
            notCreated = false;
        } else
            // Then check in children, recursively...
            for (Node<String> child : node.getChildren())
                findParent(name, parent, child);
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

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
