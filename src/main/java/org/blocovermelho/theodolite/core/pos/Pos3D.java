package org.blocovermelho.theodolite.core.pos;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.Objects;

public class Pos3D {
    protected int x;
    protected int y;
    protected int z;

    public Pos3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Pos3D of(BlockPos pos) {
        return new Pos3D(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Pos3D of(Vec3d vec3d) {
        return new Pos3D((int) vec3d.getX(), (int) vec3d.getY(), (int) vec3d.getZ());
    }
    public static Pos3D ZERO = new Pos3D(0,0,0);


    public Pos3D floorDiv(int scalar) {
        return new Pos3D(Math.floorDiv( this.getX(), scalar) , Math.floorDiv( this.getY(), scalar), Math.floorDiv( this.getZ(), scalar));
    }

    public Pos3D scale(int scalar) {
        return new Pos3D(scalar * this.getX(), scalar * this.getY(), scalar * this.getZ());
    }

    public Pos3D translate(int scalar) {
        return new Pos3D(this.getX() + scalar, this.getY() + scalar, this.getZ() + scalar);
    }

    public Pos3D sub(int scalar) {
        return new Pos3D(this.getX() - scalar, this.getY() - scalar, this.getZ() - scalar);
    }

    public Pos3D add(Pos3D pos) {
        return new Pos3D(this.getX() + pos.getX(), this.getY() + pos.getY(), this.getZ() + pos.getZ());
    }

    public Pos3D subtract(Pos3D pos) {
        return new Pos3D(this.getX() - pos.getX(), this.getY() - pos.getY(), this.getZ() - pos.getZ());
    }


    public BlockPos toBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "{x: " + x + ",y: " + y + ",z: " + z +  "}";
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos3D pos3D = (Pos3D) o;
        return x == pos3D.x && y == pos3D.y && z == pos3D.z;
    }

    @Override
    public int hashCode() {
        return  Integer.hashCode(this.x) ^
                Integer.hashCode(this.y) ^
                Integer.hashCode(this.z);
    }
}
