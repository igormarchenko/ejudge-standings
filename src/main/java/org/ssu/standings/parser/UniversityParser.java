package org.ssu.standings.parser;

import org.ssu.standings.entity.University;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class UniversityParser extends Parser{
    public UniversityParser(File file) throws ParserConfigurationException, FileNotFoundException {
        super(file);
    }

    public UniversityParser(String uri) throws ParserConfigurationException {
        super(uri);
    }

    public Map<String, University> universityInfo() {
        NodeList universities = getNodeList("university");
        Map<String, University> result = new HashMap<>();
        for(int i = 0; i < universities.getLength(); i++) {
            NamedNodeMap attributes = universities.item(i).getAttributes();
            String name = attributes.getNamedItem("name").getNodeValue();
            String type = attributes.getNamedItem("type").getNodeValue();
            String region = attributes.getNamedItem("region").getNodeValue();
            result.put(name, new University().setName(name).setType(type).setRegion(region));
        }
        return result;
    }

}
