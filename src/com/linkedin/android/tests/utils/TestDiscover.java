package com.linkedin.android.tests.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Environment;

import com.linkedin.android.test.R;
import com.linkedin.android.tests.data.DataProvider;
import com.linkedin.android.utils.Logger;

/**
 * Class for get tags for run from various sources.
 * 
 * @author alexander.makarov
 * @created Jan 5, 2013 3:57:43 PM
 */
public class TestDiscover {
    // CONSTANTS ------------------------------------------------------------
    private static final String KEY_TESTSUITE= "TestSuite";
    
    // TODO implement using groups.
    //private static final String KEY_GROUPS= "groups";
    
    private static final String KEY_TEST= "Test";
    private static final String KEY_NAME= "name";
    private static final String KEY_ID= "id";
    private static final String KEY_STEP = "Step";
    private static final String KEY_KEY = "key";
    private static final String KEY_PAYLOAD = "payload";
    
    // TODO implement using ID's.
    //private static final String KEY_ID = "id";

    // PROPERTIES -----------------------------------------------------------

    // CONSTRUCTORS ---------------------------------------------------------

    // METHODS --------------------------------------------------------------
    /**
     * Gets string from specified file.
     * 
     * @param pathToFile
     *            path to file.
     * @return string from file_with_tags.
     */
    public static String getStringFromFile(String pathToFile) {
        pathToFile = pathToFile != null ? pathToFile : DataProvider.getInstance().getContext()
                .getString(R.string.file_with_tags);
        // Check that file exist.
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = new File(Environment.getExternalStorageDirectory(), pathToFile);
        } else {
            Assert.fail("Cannot find file with tags '" + pathToFile + "'");
        }

        // Try open stream and get data.
        try {
            FileInputStream stream = new FileInputStream(file);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                return Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                Assert.fail("Cannot get data from file '" + file.getAbsolutePath() + "': "
                        + e.toString());
            } finally {
                stream.close();
            }
        } catch (Exception e) {
            Assert.fail("Cannot create stream from file '" + file.getAbsolutePath() + "': "
                    + e.toString());
        }
        return null;
    }

    /**
     * Parsed specified file with JSON array of helpers and returns array of {@code Tag} objects.
     * 
     * @param pathToFile path to file.
     * @return array of {@code Tag} objects.
     */
    public static Tag[] getTagsFromFile(String pathToFile) {
        String stringToParse = TestDiscover.getStringFromFile(pathToFile);
        Tag[] tags = null;
        try {
            JSONObject entries = new JSONObject(stringToParse);
            JSONArray tagNames = entries.names();
            // Logger.i("names=" + tagNames + ", length=" + tagNames.length());
            tags = new Tag[tagNames.length()];
            for (int i = 0; i < tagNames.length(); i++) {
                JSONArray array = entries.getJSONArray(tagNames.getString(i));
                String[] actions = new String[array.length()];
                for (int j = 0; j < array.length(); j++) {
                    actions[j] = array.getString(j);
                }
                tags[i] = new Tag(tagNames.getString(i), actions, null);
            }
        } catch (Exception e) {
            Assert.fail("Cannot parse JSON array '" + stringToParse + "': " + e.toString());
        }
        return tags;
    }
    
    /**
     * Returns array of {@code Tag} objects from specified xml.
     * 
     * @param pathToXml path to xml for parse.
     * @return array of {@code Tag} objects.
     */
    public static ArrayList<Tag> getTagsFromXml(String pathToXml) {
        if (pathToXml == null) {
            pathToXml = DataProvider.getInstance().getContext().getString(R.string.file_with_xml);
        }
        // Get XNL content as string.
        String stringToParse = TestDiscover.getStringFromFile(pathToXml);
        // Parse to Document object.
        ArrayList<Tag> tags = new ArrayList<Tag>();
        Document doc = getDomElement(stringToParse);
        // Get TestSuites.
        NodeList suites = doc.getElementsByTagName(KEY_TESTSUITE);
        Assert.assertTrue("XML not contain test suites.", suites.getLength() > 0);
        // TODO implement suites.
        Node suite = suites.item(0);

        // Look through all tests in suite.
        NodeList tests = suite.getChildNodes();
        Assert.assertTrue("Test suite not contain tests.", tests.getLength() > 0);
        for (int i = 0; i < tests.getLength(); i++) {
            Node test = tests.item(i);
            // Only if it node "key".
            if (test.getNodeName().equals(KEY_TEST)) {
                // Get name and id.
                String testName = getAttributeValue(test, KEY_NAME);
                String testId = getAttributeValue(test, KEY_ID);
                // Get actions.
                ArrayList<RegistryKey> steps = new ArrayList<RegistryKey>();
                NodeList actions = test.getChildNodes();
                Assert.assertTrue("Test " + testName + " has not actions.", actions.getLength() > 0);
                // Look through all actions.
                for (int j = 0; j < actions.getLength(); j++) {
                    Node action = actions.item(j);
                    // Only if it node "step".
                    if (action.getNodeName().equals(KEY_STEP)) {
                        try {
                            String key = getAttributeValue(action, KEY_KEY);
                            String payload = getAttributeValue(action, KEY_PAYLOAD);
                            String[] payloadArray = null;
                            if (payload != null)
                                payloadArray = payload.split(",");
                            steps.add(new RegistryKey(key, (Object[])payloadArray));
                        } catch (Exception e) {
                            Logger.e("Wrong step '" + action.getNodeName() + "'", e);
                        }
                    }
                }
                tags.add(new Tag(testName, testId, steps));
            }
        }
        return tags;
    }
    
    /**
     * Returns Document object from string with xml.
     * 
     * @param xml string for parse
     * @return Document object
     */
    private static Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
        } catch (Exception e) {
            Logger.e("Cannot get Document object from xml", e);
            Assert.fail("Cannot get Document object from xml");
        }
        return doc;
    }
    
    /**
     * Returns specified attribute for specified Node.
     * 
     * @param item Node to get attribute.
     * @param attrName name of attribute.
     * @return attribute value or <b>null</b> if not found.
     */
    private static String getAttributeValue(Node item, String attrName) {
        Assert.assertNotNull("Cannot get attribute '" + attrName + "' for empty item.", item);
        NamedNodeMap attr = item.getAttributes();
        String attrValue = null;
        for (int j = 0; j < attr.getLength(); j ++) {
            if (attr.item(j).getNodeName().equals(attrName))
                attrValue = attr.item(j).getNodeValue();
        }
        return attrValue;
    }
}
