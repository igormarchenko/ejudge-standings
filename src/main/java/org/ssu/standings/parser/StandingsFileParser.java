package org.ssu.standings.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.ssu.standings.parser.entity.ContestNode;

import java.io.IOException;
import java.util.Optional;

public class StandingsFileParser {
    private static Optional<StandingsFileParser> parser = Optional.empty();
    private XmlMapper mapper;

    private StandingsFileParser() {
        mapper = (XmlMapper) new XmlMapper().registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
    }

    public static StandingsFileParser getInstance() {
        if(!parser.isPresent())
            parser = Optional.of(new StandingsFileParser());

        return parser.get();
    }

    public Optional<ContestNode> parse(String content) {
        try {
            return Optional.of(mapper.readValue(content, ContestNode.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
