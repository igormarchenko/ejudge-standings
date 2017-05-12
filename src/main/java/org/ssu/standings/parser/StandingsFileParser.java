package org.ssu.standings.parser;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.ssu.standings.parser.entity.ContestNode;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Optional;

@Component
public class StandingsFileParser {
    private XmlMapper mapper;

    @PostConstruct
    private void init() {
        mapper = (XmlMapper) new XmlMapper().registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
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
