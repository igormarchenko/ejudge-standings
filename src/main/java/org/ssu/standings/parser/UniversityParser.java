package org.ssu.standings.parser;

import org.ssu.standings.entity.University;
import org.ssu.standings.utils.XmlStream;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

public class UniversityParser extends Parser {

    private static final String UNIVERSITY_TAG = "university";
    private static final String UNIVERSITY_NAME_ATTRIBUTE = "name";
    private static final String UNIVERSITY_TYPE_ATTRIBUTE = "type";
    private static final String UNIVERSITY_REGION_ATTRIBUTE = "region";

    public UniversityParser(File file) {
        super(file);
    }

    public UniversityParser(String uri) {
        super(uri);
    }

    public Map<String, University> universityInfo() {
        return XmlStream.of(getCurrentNode(UNIVERSITY_TAG))
                .map(university -> new University()
                        .setName(getAttributeValue(university, UNIVERSITY_NAME_ATTRIBUTE))
                        .setType(getAttributeValue(university, UNIVERSITY_TYPE_ATTRIBUTE))
                        .setRegion(getAttributeValue(university, UNIVERSITY_REGION_ATTRIBUTE)))
                .collect(Collectors.toMap(University::getName,
                        item -> item,
                        (university1, university2) -> university2));
    }

}
