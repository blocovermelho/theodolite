package org.blocovermelho.theodolite.core.octree.iter;

import org.blocovermelho.theodolite.core.octree.OctDirection;
import org.blocovermelho.theodolite.core.octree.OctNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Consumer;

public class OctNodeDirectionIterator<T> implements Iterator<OctDirection> {
    private final Queue<OctDirection> directionQueue = new LinkedList<>();

    public OctNodeDirectionIterator(OctNode<T> parentNode, boolean returnLeaf) {
        if (parentNode.sectionPos.getDetail() > parentNode.minimumDetailLevel) {
            for (OctDirection value : OctDirection.values()) {
                if (returnLeaf || parentNode.getChild(value) != null) {
                    this.directionQueue.add(value);
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !this.directionQueue.isEmpty();
    }

    @Override
    public OctDirection next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }

        OctDirection dir = this.directionQueue.poll();
        return dir;
    }

    /** Unimplemented */
    @Override
    public void remove() { throw new UnsupportedOperationException("remove"); }

    @Override
    public void forEachRemaining(Consumer<? super OctDirection> action) {
        Iterator.super.forEachRemaining(action);
    }
}
