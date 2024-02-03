package org.blocovermelho.theodolite.core.pos;

import net.minecraft.util.math.ChunkPos;

public class Region2D {
    protected int x;
    protected int z;

    public Region2D(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static Region2D of(ChunkPos pos) {
        return new Region2D(pos.getRegionX(), pos.getRegionZ());
    }
}
