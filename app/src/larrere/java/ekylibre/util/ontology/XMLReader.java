package ekylibre.util.ontology;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ekylibre.zero.BuildConfig;
import ekylibre.zero.inter.InterActivity;

public class XMLReader extends AsyncTask<Void,Void,Node<String>> {

    private static final String TAG = "XMLReader";

    private int totalItemsNumber = 0;  //2721
    private int createdtemsNumber = 0;
    private int counter = 0;
    private InputStream inputStream;
    private Node<String> root;

    public XMLReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    protected Node<String> doInBackground(Void... voids) {

        try {
            parse(inputStream);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return root;
    }

    @Override
    protected void onPostExecute(Node<String> node) {
        super.onPostExecute(node);
        InterActivity.ontology = node;
//        Log.e(TAG, "Variants --> " + Ontology.findInTree(node, "equipment"));

    }

    public Node<String> parse(InputStream inputStream) throws XmlPullParserException, IOException {

            // "product" is the root note of our tree
            root = new Node<>("product");

            try {
                do {
                    Log.i(TAG, "Loop #"+ ++counter);
                    inputStream.reset();
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);
                    parser.nextTag();
                    readFeed(parser);

                    Log.i(TAG, "Created: "+createdtemsNumber + "/" + totalItemsNumber);

                } while (createdtemsNumber < totalItemsNumber -1);

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

                // This are current xml node attributes
                String name = parser.getAttributeValue(null, "name");
                String parent = parser.getAttributeValue(null, "parent");

                // Avoid some nodes
//                String itis_tsn = parser.getAttributeValue(null, "itis_tsn");
//                String french_race_code = parser.getAttributeValue(null, "french_race_code");
//                String europa_tsn = parser.getAttributeValue(null, "europa_tsn");

                if (BuildConfig.DEBUG)
                    if (counter == 1)
                        Log.d(TAG, "Position #" + totalItemsNumber + ": " + name + " <- " + parent);

                // Count total item in the firt loop
                if (counter == 1)
                    ++totalItemsNumber;

                if (parent != null)
                    findParent(name, parent, root);

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

            boolean doNotExists = true;
            // Do not create child if already exists
            for (Node child : node.getChildren())
                if (child.getName().equals(name)) {
                    doNotExists = false;
                    break;
                }

            // Create the node
            if (doNotExists) {
                node.addChild(new Node<>(name));
                ++createdtemsNumber;
            }

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


