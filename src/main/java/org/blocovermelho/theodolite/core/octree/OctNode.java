package org.blocovermelho.theodolite.core.octree;

import org.blocovermelho.theodolite.core.pos.Area3I;
import org.blocovermelho.theodolite.core.pos.Pos3I;
import org.blocovermelho.theodolite.core.utils.NumericalConstants;
import org.blocovermelho.theodolite.core.utils.arithmetic.BitShift;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OctNode<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger("theodolite.OctNode<T>");

    public Area3I sectionPos;
    public byte minimumDetailLevel = NumericalConstants.BLOCK_DETAIL_LEVEL;
    public byte depth;
    public T value;

    public OctNode(Area3I centerPos, byte minimumDetailLevel) {
        this.minimumDetailLevel = minimumDetailLevel;
        this.sectionPos = centerPos;
    }

    /**
     * North-West Down <br>
     * Relative Coordinates: (0,0,0) <br>
     * Index: 0
     */
    public OctNode<T> nwdChild;

    /**
     * North-East Down <br>
     * Relative Coordinates: (0,0,1) <br>
     * Index: 1
     */
    public OctNode<T> nedChild;
    /**
     * North-West Up <br>
     * Relative Coordinates: (0,1,0) <br>
     * Index: 2
     */
    public OctNode<T> nwuChild;
    /**
     * North-East Up <br>
     * Relative Coordinates: (0,1,1) <br>
     * Index: 3
     */
    public OctNode<T> neuChild;
    /**
     * South-West Down <br>
     * Relative Coordinates: (1,0,0) <br>
     * Index: 4
     */
    public OctNode<T> swdChild;
    /**
     * South-East Down <br>
     * Relative Coordinates: (1,0,1) <br>
     * Index: 5
     */
    public OctNode<T> sedChild;
    /**
     * South-West Up <br>
     * Relative Coordinates: (1,1,0) <br>
     * Index: 6
     */
    public OctNode<T> swuChild;
    /**
     * South-East Up <br>
     * Relative Coordinates: (1,1,1) <br>
     * Index: 7
     */
    public OctNode<T> seuChild;

    public int getChildCount() {
        int count = 0;
        for (int idx = 0; idx < 8; idx++) {
            if (this.getChildByIndex(idx) != null) {
                count++;
            }
        }
        return count;
    }

    public int getNonNullChildCount() {
        int count = 0;
        for (int idx = 0; idx < 8; idx++) {
            var node = this.getChildByIndex(idx);
            if (node != null  && (node.value != null || node.getNonNullChildCount() != 0)) {
                count++;
            }
        }
        return count;
    }

    /**
     * @param inputArea Must be 1 detail level lower than this node's detail level
     * @return The node at the given position before the new value was set
     * @throws IllegalArgumentException if inputArea has the wrong detail level or is outside the bounds of this node
     */
    public OctNode<T> getNode(Area3I inputArea) throws IllegalArgumentException {
        return this.getOrSetValue(inputArea, false, null);
    }

    /**
     * @param inputArea Must be 1 detail level lower than this node's detail level
     * @return The value at the given position before the new value was set
     * @throws IllegalArgumentException if inputArea has the wrong detail level or is outside the bounds of this node
     */
    public T setValue(Area3I inputArea, T newValue) throws IllegalArgumentException {
        OctNode<T> previousNode = this.getNode(inputArea);
        if (previousNode != null) {
            T prevValue = previousNode.value;
            previousNode.value = newValue;
            return prevValue;
        } else {
            this.getOrSetValue(inputArea, true, newValue);
            return null;
        }
    }

    private OctNode<T> getOrSetValue(Area3I inputSectionPos, boolean replace, T newValue) {
        if (!this.sectionPos.contains(inputSectionPos)) {
            LOGGER.error((replace ? "set " : "get ") + "@ " + this.sectionPos + " doesn't contain " + inputSectionPos);
            throw new IllegalArgumentException("Input section pos outside of OctNode's sectionPos");
        }

        if (inputSectionPos.getDetail() > this.sectionPos.getDetail()) {
            throw new IllegalArgumentException("Input section detail higher than OctNode's detail level");
        }

        if (inputSectionPos.getDetail() == this.sectionPos.getDetail() && !inputSectionPos.equals(this.sectionPos)) {
            throw new IllegalArgumentException("Input section pos has the same detail as OctNode, but the positions aren't the same. Did you try setting a value to a neighbouring node?");
        }

        if (inputSectionPos.getDetail() < NumericalConstants.BLOCK_DETAIL_LEVEL) {
            throw new IllegalArgumentException("Input section with a detail lower than BLOCK_DETAIL_LEVEL.");
        }

        if (inputSectionPos.getDetail() == this.sectionPos.getDetail()) {
            if (replace) {
                this.value = newValue;
            }
            return this;
        } else {
            Area3I nwdArea = this.sectionPos.getChild(OctDirection.NWD);
            Area3I nedArea = this.sectionPos.getChild(OctDirection.NED);
            Area3I nwuArea = this.sectionPos.getChild(OctDirection.NWU);
            Area3I neuArea = this.sectionPos.getChild(OctDirection.NEU);
            Area3I swdArea = this.sectionPos.getChild(OctDirection.SWD);
            Area3I sedArea = this.sectionPos.getChild(OctDirection.SED);
            Area3I swuArea = this.sectionPos.getChild(OctDirection.SWU);
            Area3I seuArea = this.sectionPos.getChild(OctDirection.SEU);

            OctNode<T> childNode = null;
            if (nwdArea.contains(inputSectionPos)) {
                if (replace && this.nwdChild == null) {
                    this.nwdChild = new OctNode<>(nwdArea, this.minimumDetailLevel);
                }
                childNode = this.nwdChild;
            } else if (nedArea.contains(inputSectionPos)) {
                if (replace && this.nedChild == null) {
                    this.nedChild = new OctNode<>(nedArea, this.minimumDetailLevel);
                }
                childNode = this.nedChild;
            } else if (nwuArea.contains(inputSectionPos)) {
                if (replace && this.nwuChild == null) {
                    this.nwuChild = new OctNode<>(nwuArea, this.minimumDetailLevel);
                }
                childNode = this.nwuChild;
            } else if (neuArea.contains(inputSectionPos)) {
                if (replace && this.neuChild == null) {
                    this.neuChild = new OctNode<>(neuArea, this.minimumDetailLevel);
                }
                childNode = this.neuChild;
            } else if (swdArea.contains(inputSectionPos)) {
                if (replace && this.swdChild == null) {
                    this.swdChild = new OctNode<>(swdArea, this.minimumDetailLevel);
                }
                childNode = this.swdChild;
            } else if (sedArea.contains(inputSectionPos)) {
                if (replace && this.sedChild == null) {
                    this.sedChild = new OctNode<>(sedArea, this.minimumDetailLevel);
                }
                childNode = this.sedChild;
            } else if (swuArea.contains(inputSectionPos)) {
                if (replace && this.swuChild == null) {
                    this.swuChild = new OctNode<>(swuArea, this.minimumDetailLevel);
                }
                childNode = this.swuChild;
            } else if (seuArea.contains(inputSectionPos)) {
                if (replace && this.seuChild == null) {
                    this.seuChild = new OctNode<>(seuArea, this.minimumDetailLevel);
                }
            } else {
                throw new IllegalStateException("PANIC!!! Input position not contained by any children node. Should be Unreachable.");
            }
            return (childNode != null) ? childNode.getOrSetValue(inputSectionPos, replace, newValue) : null;
        }
    }

    public Pos3I getMinCornerPos() {
        return this.sectionPos.getMinCornerPos();
    }

    public Pos3I getMaxCornerPos() {
        return this.sectionPos.getMaxCornerPos();
    }

    public Area3I getChildArea(OctDirection direction) {

        if (this.depth == NumericalConstants.BLOCK_DETAIL_LEVEL) {
            return this.sectionPos;
        }

        int childSize = BitShift.powerOfTwo(this.depth - 1);

        Pos3I offset = direction.asVector().scale(childSize);

        return new Area3I
                (
                        this.getMinCornerPos().add(offset),
                        this.getMaxCornerPos().sub(childSize).add(offset)
                );
    }
    
    public OctNode<T> getChild(OctDirection direction) {
        return getChildByIndex(direction.index);
    }

    protected OctNode<T> getChildByIndex(int idx) {
        return switch (idx) {
            case 0 -> nwdChild;
            case 1 -> nedChild;
            case 2 -> nwuChild;
            case 3 -> neuChild;
            case 4 -> swdChild;
            case 5 -> sedChild;
            case 6 -> swuChild;
            case 7 -> seuChild;
            default -> throw new IllegalArgumentException("idx must be between 0 and 7");
        };
    }

    private void subdivide() {
        Area3I nwdArea = this.getChildArea(OctDirection.NWD);
        Area3I nedArea = this.getChildArea(OctDirection.NED);
        Area3I nwuArea = this.getChildArea(OctDirection.NWU);
        Area3I neuArea = this.getChildArea(OctDirection.NEU);
        Area3I swdArea = this.getChildArea(OctDirection.SWD);
        Area3I sedArea = this.getChildArea(OctDirection.SED);
        Area3I swuArea = this.getChildArea(OctDirection.SWU);
        Area3I seuArea = this.getChildArea(OctDirection.SEU);

        this.nwdChild = new OctNode<>(nwdArea, (byte) (this.depth - 1));
        this.nedChild = new OctNode<>(nedArea, (byte) (this.depth - 1));
        this.nwuChild = new OctNode<>(nwuArea, (byte) (this.depth - 1));
        this.neuChild = new OctNode<>(neuArea, (byte) (this.depth - 1));
        this.swdChild = new OctNode<>(swdArea, (byte) (this.depth - 1));
        this.sedChild = new OctNode<>(sedArea, (byte) (this.depth - 1));
        this.swuChild = new OctNode<>(swuArea, (byte) (this.depth - 1));
        this.seuChild = new OctNode<>(seuArea, (byte) (this.depth - 1));
    }

    @Override
    public String toString() { return "pos: " + this.sectionPos + ", children #: " + this.getChildCount() + ", value: " + this.value; }

}
