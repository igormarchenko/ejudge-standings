package org.ssu.standings.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Parser {
    private Document document;

    public Parser(File file) {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            document.getDocumentElement().normalize();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public Parser(String uri) {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri);
            document.getDocumentElement().normalize();
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    protected NodeList getCurrentNode(String tag) {
        return document.getElementsByTagName(tag);
    }

    protected NodeList getChildNodes(String tag) {
        return getCurrentNode(tag).item(0).getChildNodes();
    }

    protected String getAttributeValue(Node node, String attributeName) {
        return node.getAttributes()
                .getNamedItem(attributeName)
                .getNodeValue();
    }
}
