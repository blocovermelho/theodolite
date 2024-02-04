package org.blocovermelho.theodolite.core.pos;

import net.minecraft.util.math.Vec3d;

public class Pos3D {


    protected double x;
    protected double y;
    protected double z;

    public Pos3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Pos3D of (Pos3I pos3I) {
        return new Pos3D(pos3I.x, pos3I.y, pos3I.z);
    }

    public static Pos3D of (Vec3d vec3d) {
        return new Pos3D(vec3d.x, vec3d.y, vec3d.z);
    }

    public Pos3D scale(float scalar) {
        return new Pos3D(scalar * this.getX(), scalar * this.getY(), scalar * this.getZ());
    }

    public Pos3D translate(float scalar) {
        return new Pos3D(this.getX() + scalar, this.getY() + scalar, this.getZ() + scalar);
    }

    public Pos3D sub(float scalar) {
        return new Pos3D(this.getX() - scalar, this.getY() - scalar, this.getZ() - scalar);
    }

    public Pos3D add(Pos3D pos) {
        return new Pos3D(this.getX() + pos.getX(), this.getY() + pos.getY(), this.getZ() + pos.getZ());
    }

    public Pos3D subtract(Pos3D pos) {
        return new Pos3D(this.getX() - pos.getX(), this.getY() - pos.getY(), this.getZ() - pos.getZ());
    }


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
