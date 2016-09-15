package org.ssu.standings.parser;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Parser {
    private DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    protected Document document;

    public Parser(File file) throws ParserConfigurationException, FileNotFoundException {
        try {
            document = dBuilder.parse(file);
            document.getDocumentElement().normalize();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public Parser(String uri) throws ParserConfigurationException {
        try {
            document = dBuilder.parse(uri);
            document.getDocumentElement().normalize();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public NodeList getNodeList(String tag) {
        return document.getElementsByTagName(tag);
    }

    protected NodeList getSingleNode(String tag) {
        return getNodeList(tag).item(0).getChildNodes();
    }
}
