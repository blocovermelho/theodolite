package org.blocovermelho.theodolite.core.octree.iter;

import org.blocovermelho.theodolite.core.octree.OctDirection;
import org.blocovermelho.theodolite.core.octree.OctNode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.function.Consumer;

public class OctTreeNodeIterator<T> implements Iterator<OctNode<T>> {
    private final byte highestDetailLevel;

    private final Queue<OctNode<T>> validNodesForDetailLevel = new LinkedList<>();
    private final Queue<OctNode<T>> iteratorNodeQueue = new LinkedList<>();
    private byte iteratorDetailLevel = 0;

    private final boolean onlyReturnLeafValues;

    public OctTreeNodeIterator(OctNode<T> rootNode, boolean onlyReturnLeafValues) {
        this.onlyReturnLeafValues = onlyReturnLeafValues;
        this.highestDetailLevel = rootNode.minimumDetailLevel;
        // this.iteratorDetailLevel = rootNode.sectionPos.getDetail();


        if (!this.onlyReturnLeafValues) {
            this.validNodesForDetailLevel.add(rootNode);
            this.iteratorNodeQueue.add(rootNode);
        } else {
            Queue<OctNode<T>> parentNodeQueue = new LinkedList<>();
            parentNodeQueue.add(rootNode);

            while (parentNodeQueue.peek() != null) {
                OctNode<T> parentNode = parentNodeQueue.poll();
                for (OctDirection dir : OctDirection.values()) {
                    OctNode<T> child = parentNode.getChild(dir);
                    if (child != null && child.getChildCount() == 0) {
                        this.iteratorNodeQueue.add(child);
                    } else {
                        parentNodeQueue.add(child);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        return !this.iteratorNodeQueue.isEmpty();
    }

    @Override
    public OctNode<T> next() {
        if (this.iteratorDetailLevel < this.highestDetailLevel)
        {
            throw new NoSuchElementException("Highest detail level reached [" + this.highestDetailLevel + "].");
        }
        if (this.iteratorNodeQueue.isEmpty())
        {
            throw new NoSuchElementException();
        }


        OctNode<T> currentNode = this.iteratorNodeQueue.poll();

        if (this.iteratorNodeQueue.isEmpty() && !onlyReturnLeafValues) {
            this.iteratorDetailLevel--;
            if (this.iteratorDetailLevel >= this.highestDetailLevel)
            {
                Queue<OctNode<T>> parentNodes = new LinkedList<>(this.validNodesForDetailLevel);
                this.validNodesForDetailLevel.clear();

                for (OctNode<T> parentNode : parentNodes)
                {
                    for (OctDirection dir : OctDirection.values()) {
                        OctNode<T> child = parentNode.getChild(dir);
                        if (child != null) {
                            this.iteratorNodeQueue.add(child);
                            this.validNodesForDetailLevel.add(child);

                        }
                    }
                }

            }
        }

        return currentNode;
    }

    /** Unimplemented */
    @Override
    public void remove() { throw new UnsupportedOperationException("remove"); }

    @Override
    public void forEachRemaining(Consumer<? super OctNode<T>> action) { Iterator.super.forEachRemaining(action); }


}
