package org.blocovermelho.theodolite.core.utils.render.types;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.util.math.Vec3d;
import org.blocovermelho.theodolite.core.pos.Pos3D;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LinkedLineBuilder {
    LinkedList<Pos3D> points = new LinkedList<>();
    BufferBuilder buffer;
    Color4f color;

    public LinkedLineBuilder(BufferBuilder buffer, Color4f color) {
        this.buffer = buffer;
        this.color = color;
    }

    public LinkedLineBuilder andThen(double x, double y, double z) {
        this.points.add(new Pos3D(x, y , z));
        return this;
    }

    public void batch() {
        if (this.points.size() < 2) {
            throw new UnsupportedOperationException("You need at least two points to form a LinkedLine.");
        }

        var head = this.points.poll();
        var next = this.points.poll();

        while(!this.points.isEmpty()) {
            this.buffer.vertex(head.getX(), head.getY(), head.getZ()).color(color.red, color.green, color.blue, color.alpha).next();
            assert next != null;
            this.buffer.vertex(next.getX(), next.getY(), next.getZ()).color(color.red, color.green, color.blue, color.alpha).next();

            head = next;
            next = this.points.poll();
        }
    }


}
