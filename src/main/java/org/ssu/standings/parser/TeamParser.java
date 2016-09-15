package org.ssu.standings.parser;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TeamParser extends Parser{

    public TeamParser(File file) throws ParserConfigurationException, FileNotFoundException {
        super(file);
    }

    public TeamParser(String uri) throws ParserConfigurationException {
        super(uri);
    }

    public Map<String, String> teamList() {
        NodeList teams = getNodeList("team");
        Map<String, String> result = new HashMap<>();
        for(int i = 0; i < teams.getLength(); i++) {
            NamedNodeMap attributes = teams.item(i).getAttributes();
            String name = attributes.getNamedItem("name").getNodeValue();
            String university = attributes.getNamedItem("university").getNodeValue();
            result.put(name, university);
        }
        return result;
    }
}
