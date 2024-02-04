package org.blocovermelho.theodolite.core.octree;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.blocovermelho.theodolite.core.octree.iter.OctNodeDirectChildIterator;
import org.blocovermelho.theodolite.core.pos.Area3I;
import org.blocovermelho.theodolite.core.pos.Pos3I;
import org.blocovermelho.theodolite.core.pos.Region2I;
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

    public OctNode(Area3I centerPos, byte depth) {
        this.depth = depth;
        this.sectionPos = centerPos;
    }

    public static<A> OctNode<A> of(ChunkPos chunkPos, int y) {
        return new OctNode<A>(new Area3I(chunkPos, y), NumericalConstants.CHUNK_DETAIL_LEVEL);
    }

    public static<A> OctNode<A> of(Region2I region2I) {
        return new OctNode<A>(new Area3I(region2I), NumericalConstants.REGION_DETAIL_LEVEL);
    }

    public static<A> OctNode<A> of(BlockPos blockPos) {
        return new OctNode<A>(new Area3I(blockPos), NumericalConstants.BLOCK_DETAIL_LEVEL);
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
        if (!inputSectionPos.intersects(this.sectionPos)) {
            // LOGGER.info("INPUT: "+ inputSectionPos + " does not intersect with NODE: "+ this +" . Skipping.");
            return this;
        } else {
            if (this.sectionPos.totallyInsideOf(inputSectionPos) || this.depth <= this.minimumDetailLevel) {
                LOGGER.info("[LeaF] INPUT: "+ inputSectionPos + " is totally contained inside NODE: "+ this +".");
                // Make it into a leaf
                if (replace) {
                    this.value = newValue;
                }
                LOGGER.info("Leaf Node @ " + this);
                return this;
            }

            LOGGER.info("[Subdivide] "+ this +".");
            this.subdivide();
            OctNodeDirectChildIterator<T> childIterator = new OctNodeDirectChildIterator<>(this);
            childIterator.forEachRemaining(child -> {
                child.getOrSetValue(inputSectionPos, replace, newValue);
            });
        }
        return null;
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
    public String toString() { return "pos: " + this.sectionPos + ", children #: " + this.getChildCount() + ", value: " + this.value  + ", depth: " + this.depth; }

}
