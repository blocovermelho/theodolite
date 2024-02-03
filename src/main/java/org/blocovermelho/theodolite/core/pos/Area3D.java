package org.blocovermelho.theodolite.core.pos;

import net.fabricmc.loader.impl.lib.tinyremapper.extension.mixin.common.data.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import org.blocovermelho.theodolite.core.octree.OctDirection;
import org.blocovermelho.theodolite.core.utils.NumericalConstants;
import org.blocovermelho.theodolite.core.utils.arithmetic.BitShift;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Area3D {

    /**
     * ABSOLUTE coordinates for the MAXIMUM (+++) corner of the area.
     */
    protected Pos3D maxCornerPos;

    /**
     * ABSOLUTE coordinates for the minimum (---) corner of the area.
     */
    protected Pos3D minCornerPos;

    public Area3D (Pos3D minCornerPos, Pos3D maxCornerPos) {
        this.minCornerPos = minCornerPos;
        this.maxCornerPos = maxCornerPos;
    }

    public Area3D (Region2D region2D) {
        // Region files have 32 chunks, 0-indexed.
        // Min Chunk: X*32, Max Chunk: ((X+1)*32 - 1)
        // Min Block: C*16, Max Block: ((C+1)*16 - 1)

        // 32 * 16 = 512 (9)
        int minX = BitShift.pow(region2D.x, NumericalConstants.REGION_DETAIL_LEVEL);
        int minZ = BitShift.pow(region2D.z, NumericalConstants.REGION_DETAIL_LEVEL);

        int maxX = (BitShift.pow(region2D.x + 1, NumericalConstants.REGION_DETAIL_LEVEL) - 1);
        int maxZ = (BitShift.pow(region2D.z + 1, NumericalConstants.REGION_DETAIL_LEVEL) - 1);

        this.minCornerPos = new Pos3D(minX, -64, minZ);
        this.maxCornerPos = new Pos3D(maxX, 447, maxZ);
    }

    public Area3D (ChunkPos chunkPos, int y) {
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

        this.minCornerPos = new Pos3D(minX, minY, minZ);
        this.maxCornerPos = new Pos3D(maxX, maxY, maxZ);
    }

    public Area3D (BlockPos blockPos) {
        this.minCornerPos = Pos3D.of(blockPos);
        this.maxCornerPos = Pos3D.of(blockPos);
    }

    public Box toBox() {
        return new Box(this.minCornerPos.x, this.minCornerPos.y, this.minCornerPos.z, this.maxCornerPos.x, this.maxCornerPos.y, this.maxCornerPos.z);
    }

    public int getBlockWidth() { return 0; }

    /** @return the corner with the smallest X and Z coordinate */
    public Pos3D getMinCornerPos()
    {
        return this.minCornerPos;
    }

    public Pos3D getMaxCornerPos() { return  this.maxCornerPos;}
    public boolean contains(Area3D area3D) {
        return this.minCornerPos.x <= area3D.minCornerPos.x && area3D.minCornerPos.x <= this.maxCornerPos.x &&
               this.minCornerPos.y <= area3D.minCornerPos.y && area3D.minCornerPos.y <= this.maxCornerPos.y &&
               this.minCornerPos.z <= area3D.minCornerPos.z && area3D.minCornerPos.z <= this.maxCornerPos.z;
    }


    @Override
    public String toString() {
        return " -- " + minCornerPos + ", ++" + maxCornerPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area3D area3D = (Area3D) o;

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
