package org.blocovermelho.theodolite.core.pos;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import org.blocovermelho.theodolite.core.utils.NumericalConstants;
import org.blocovermelho.theodolite.core.utils.arithmetic.BitShift;

public class Area3I {

    /**
     * ABSOLUTE coordinates for the MAXIMUM (+++) corner of the area.
     */
    protected Pos3I maxCornerPos;

    /**
     * ABSOLUTE coordinates for the minimum (---) corner of the area.
     */
    protected Pos3I minCornerPos;

    public Area3I(Pos3I minCornerPos, Pos3I maxCornerPos) {
        // We can't know for sure if those corners are right;
        int minX = Math.min(minCornerPos.x, maxCornerPos.x);
        int minY = Math.min(minCornerPos.y, maxCornerPos.y);
        int minZ = Math.min(minCornerPos.z, maxCornerPos.z);
        int maxX = Math.max(minCornerPos.x, maxCornerPos.x);
        int maxY = Math.max(minCornerPos.y, maxCornerPos.y);
        int maxZ = Math.max(minCornerPos.z, maxCornerPos.z);


        this.minCornerPos = new Pos3I(minX, minY, minZ);
        this.maxCornerPos = new Pos3I(maxX, maxY, maxZ);
    }

    public Area3I(Region2I region2I) {
        // Region files have 32 chunks, 0-indexed.
        // Min Chunk: X*32, Max Chunk: ((X+1)*32 - 1)
        // Min Block: C*16, Max Block: ((C+1)*16 - 1)

        // 32 * 16 = 512 (9)
        int minX = BitShift.pow(region2I.x, NumericalConstants.REGION_DETAIL_LEVEL);
        int minZ = BitShift.pow(region2I.z, NumericalConstants.REGION_DETAIL_LEVEL);

        int maxX = (BitShift.pow(region2I.x + 1, NumericalConstants.REGION_DETAIL_LEVEL) - 1);
        int maxZ = (BitShift.pow(region2I.z + 1, NumericalConstants.REGION_DETAIL_LEVEL) - 1);

        this.minCornerPos = new Pos3I(minX, -64, minZ);
        this.maxCornerPos = new Pos3I(maxX, 447, maxZ);
    }

    public Area3I(ChunkPos chunkPos, int y) {
        // Chunks are 16 * Build Limit * 16 Blocks
        // In our case, a chunk is a 16*16*16 area, similar to a "sub chunk"
        // This is done since we're dealing with an octree, which all components are cubes.
        // This is why we need to pass the y component.

        // Getting which sub-chunk the y coordinate is part of
        int yIndex = BitShift.divideByPowerOfTwo(y, NumericalConstants.CHUNK_DETAIL_LEVEL);

        int minX = BitShift.pow(chunkPos.x, NumericalConstants.CHUNK_DETAIL_LEVEL);
        int minY = BitShift.pow(yIndex, NumericalConstants.CHUNK_DETAIL_LEVEL);
        int minZ = BitShift.pow(chunkPos.z, NumericalConstants.CHUNK_DETAIL_LEVEL);

        int maxX = (BitShift.pow(chunkPos.x + 1, NumericalConstants.CHUNK_DETAIL_LEVEL) - 1);
        int maxY = (BitShift.pow(yIndex + 1, NumericalConstants.CHUNK_DETAIL_LEVEL) - 1);
        int maxZ = (BitShift.pow(chunkPos.z + 1, NumericalConstants.CHUNK_DETAIL_LEVEL) - 1);

        this.minCornerPos = new Pos3I(minX, minY, minZ);
        this.maxCornerPos = new Pos3I(maxX, maxY, maxZ);
    }

    public Area3I(BlockPos blockPos) {
        this.minCornerPos = Pos3I.of(blockPos);
        this.maxCornerPos = Pos3I.of(blockPos);
    }

    public Box toBox() {
        return new Box(this.minCornerPos.x, this.minCornerPos.y, this.minCornerPos.z, this.maxCornerPos.x, this.maxCornerPos.y, this.maxCornerPos.z);
    }

    public int getBlockWidth() { return 0; }

    /** @return the corner with the smallest X and Z coordinate */
    public Pos3I getMinCornerPos()
    {
        return this.minCornerPos;
    }

    public Pos3I getMaxCornerPos() { return  this.maxCornerPos;}
    public boolean contains(Area3I area3D) {
        return this.minCornerPos.x <= area3D.minCornerPos.x && area3D.minCornerPos.x <= this.maxCornerPos.x &&
               this.minCornerPos.y <= area3D.minCornerPos.y && area3D.minCornerPos.y <= this.maxCornerPos.y &&
               this.minCornerPos.z <= area3D.minCornerPos.z && area3D.minCornerPos.z <= this.maxCornerPos.z;
    }

    public boolean intersects(Area3I other) {
        return  (this.maxCornerPos.x >= other.minCornerPos.x && minCornerPos.x <= other.maxCornerPos.x) &&
                (this.maxCornerPos.y >= other.minCornerPos.y && minCornerPos.y <= other.maxCornerPos.y) &&
                (this.maxCornerPos.z >= other.minCornerPos.z && minCornerPos.z <= other.maxCornerPos.z);
    }

    public boolean totallyContains(Area3I other) {
        return other.minCornerPos.x >= this.minCornerPos.x
                && other.minCornerPos.y >= this.minCornerPos.y
                && other.minCornerPos.z >= this.minCornerPos.z
                && other.maxCornerPos.x <= this.maxCornerPos.x
                && other.maxCornerPos.y <= this.maxCornerPos.y
                && other.maxCornerPos.z <= this.maxCornerPos.z;
    }

    public boolean totallyInsideOf(Area3I other) {
        return other.totallyContains(this);
    }


    @Override
    public String toString() {
        return " -- " + minCornerPos + ", ++" + maxCornerPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area3I area3D = (Area3I) o;

        return // this.detail == area3D.detail &&
                maxCornerPos.equals(area3D.maxCornerPos) &&
                minCornerPos.equals(area3D.minCornerPos);
    }

    @Override
    public int hashCode()
    {
        return // Integer.hashCode(this.detail) ^ // XOR
                this.maxCornerPos.hashCode() ^ // XOR
                this.minCornerPos.hashCode();
    }
}
