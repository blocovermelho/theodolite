package org.blocovermelho.theodolite.core.octree.iter;

import org.blocovermelho.theodolite.core.octree.OctDirection;
import org.blocovermelho.theodolite.core.octree.OctNode;
import org.blocovermelho.theodolite.core.pos.Area3I;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class OctNodeDirectChildAreaIterator<T> implements Iterator<Area3I> {
    private final OctNodeDirectionIterator<T> childDirectionIterator;
    private final OctNode<T> parentNode;

    public OctNodeDirectChildAreaIterator(OctNode<T> parentNode) {
        this.parentNode = parentNode;
        this.childDirectionIterator = new OctNodeDirectionIterator<>(this.parentNode,true);
    }

    @Override
    public boolean hasNext() {
        return this.childDirectionIterator.hasNext();
    }

    @Override
    public Area3I next() {
        if (!this.hasNext())
        {
            throw new NoSuchElementException();
        }

        OctDirection dir = this.childDirectionIterator.next();
        return this.parentNode.getChildArea(dir);
    }
}
