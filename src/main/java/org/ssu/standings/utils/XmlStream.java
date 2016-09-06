package org.ssu.standings.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class XmlStream {
    public static Stream<Node> of(NodeList list) {
        return IntStream.range(0, list.getLength()).mapToObj(list::item);
    }
}