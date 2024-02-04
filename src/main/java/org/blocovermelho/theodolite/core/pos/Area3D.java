package org.blocovermelho.theodolite.core.pos;

public class Area3D {
    /**
     * ABSOLUTE coordinates for the MAXIMUM (+++) corner of the area.
     */
    protected Pos3D maxCornerPos;


    /**
     * ABSOLUTE coordinates for the minimum (---) corner of the area.
     */
    protected Pos3D minCornerPos;
    public Area3D(Pos3D minCornerPos, Pos3D maxCornerPos) {
        this.minCornerPos = minCornerPos;
        this.maxCornerPos = maxCornerPos;
    }

    public static Area3D of(Area3I area3D) {
        return new Area3D(Pos3D.of(area3D.minCornerPos), Pos3D.of(area3D.maxCornerPos));
    }

    public Pos3D getMaxCornerPos() {
        return maxCornerPos;
    }

    public Pos3D getMinCornerPos() {
        return minCornerPos;
    }

}
