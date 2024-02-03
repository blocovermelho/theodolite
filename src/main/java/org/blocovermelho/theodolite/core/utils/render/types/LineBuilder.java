package org.blocovermelho.theodolite.core.utils.render.types;

import net.minecraft.client.render.BufferBuilder;
import org.blocovermelho.theodolite.core.pos.Pos3D;

public class LineBuilder {
    Pos3D from;
    Pos3D to;
    BufferBuilder buffer;
    Color4f color;

    public LineBuilder (BufferBuilder buffer, Color4f color) {
        this.buffer = buffer;
        this.color = color;
    }
    public LineBuilder from(int x, int y, int z) {
        this.from = new Pos3D(x, y, z);
        return this;
    }

    public LineBuilder to(int x, int y, int z) {
        this.to = new Pos3D(x, y, z);
        return this;
    }

    /**
     * Consumes the LineBuilder batching it to the VBO.
     */
    public void batch() {
        this.buffer.vertex(from.getX(), from.getY(), from.getZ()).color(color.red, color.green, color.blue, color.alpha).next();
        this.buffer.vertex(to.getX(), to.getY(), to.getZ()).color(color.red, color.green, color.blue, color.alpha).next();
    }
}
