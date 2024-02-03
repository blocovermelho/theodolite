package org.blocovermelho.theodolite.core.octree.iter;

import org.blocovermelho.theodolite.core.octree.OctDirection;
import org.blocovermelho.theodolite.core.octree.OctNode;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class OctNodeDirectChildIterator<T> implements Iterator<OctNode<T>> {
    private final OctNodeDirectionIterator<T> childIndexIterator;
    private final OctNode<T> parentNode;

    public OctNodeDirectChildIterator(OctNode<T> parentNode) {
        this.parentNode = parentNode;
        this.childIndexIterator = new OctNodeDirectionIterator<>(this.parentNode, false);
    }

    @Override
    public boolean hasNext() {
        return this.childIndexIterator.hasNext();
    }

    @Override
    public OctNode<T> next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }

        OctDirection dir = this.childIndexIterator.next();
        OctNode<T> node = this.parentNode.getChild(dir);
        return node;
    }

    /** Unimplemented */
    @Override
    public void remove() { throw new UnsupportedOperationException("remove"); }

    @Override
    public void forEachRemaining(Consumer<? super OctNode<T>> action) { Iterator.super.forEachRemaining(action); }

}
