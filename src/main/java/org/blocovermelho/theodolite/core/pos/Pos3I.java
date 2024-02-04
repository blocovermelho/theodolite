package org.blocovermelho.theodolite.core.pos;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Pos3I {
    protected int x;
    protected int y;
    protected int z;

    public Pos3I(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Pos3I of(BlockPos pos) {
        return new Pos3I(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Pos3I of(Vec3d vec3d) {
        return new Pos3I((int) vec3d.getX(), (int) vec3d.getY(), (int) vec3d.getZ());
    }
    public static Pos3I ZERO = new Pos3I(0,0,0);


    public Pos3I floorDiv(int scalar) {
        return new Pos3I(Math.floorDiv( this.getX(), scalar) , Math.floorDiv( this.getY(), scalar), Math.floorDiv( this.getZ(), scalar));
    }

    public Pos3I scale(int scalar) {
        return new Pos3I(scalar * this.getX(), scalar * this.getY(), scalar * this.getZ());
    }

    public Pos3I translate(int scalar) {
        return new Pos3I(this.getX() + scalar, this.getY() + scalar, this.getZ() + scalar);
    }

    public Pos3I sub(int scalar) {
        return new Pos3I(this.getX() - scalar, this.getY() - scalar, this.getZ() - scalar);
    }

    public Pos3I add(Pos3I pos) {
        return new Pos3I(this.getX() + pos.getX(), this.getY() + pos.getY(), this.getZ() + pos.getZ());
    }

    public Pos3I subtract(Pos3I pos) {
        return new Pos3I(this.getX() - pos.getX(), this.getY() - pos.getY(), this.getZ() - pos.getZ());
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
        Pos3I pos3I = (Pos3I) o;
        return x == pos3I.x && y == pos3I.y && z == pos3I.z;
    }

    @Override
    public int hashCode() {
        return  Integer.hashCode(this.x) ^
                Integer.hashCode(this.y) ^
                Integer.hashCode(this.z);
    }

}
