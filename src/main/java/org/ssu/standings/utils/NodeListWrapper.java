package org.ssu.standings.utils;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class NodeListWrapper implements Iterable<Node>, Iterator<Node>{
    private NodeList nodelist;
    private int pos = 0;

    public NodeListWrapper(NodeList nodelist) {
        this.nodelist = nodelist;
    }

    @Override
    public Iterator<Node> iterator() {
        return this;
    }

    @Override
    public void forEach(Consumer action) {
        for (int i = 0; i < nodelist.getLength(); i++) {
            action.accept(nodelist.item(i));
        }
    }

    @Override
    public boolean hasNext() {
        return pos < nodelist.getLength();
    }

    @Override
    public Node next() {
        if(hasNext()) {
            return nodelist.item(pos++);
        }
        throw new NoSuchElementException();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
