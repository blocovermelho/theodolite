package org.blocovermelho.theodolite.core.pos;

import net.minecraft.util.math.ChunkPos;

public class Region2I {
    protected int x;
    protected int z;

    public Region2I(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static Region2I of(ChunkPos pos) {
        return new Region2I(pos.getRegionX(), pos.getRegionZ());
    }
}
