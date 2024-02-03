package org.blocovermelho.theodolite.core.octree;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import org.blocovermelho.theodolite.core.pos.Pos3D;

public enum OctDirection {
    /**
     * North-West Down <br>
     * Relative Coordinates: (0,0,0) <br>
     * Index: 0
     */
    NWD(0),
    /**
     * North-East Down <br>
     * Relative Coordinates: (0,0,1) <br>
     * Index: 1
     */
    NED(1),
    /**
     * North-West Up <br>
     * Relative Coordinates: (0,1,0) <br>
     * Index: 2
     */
    NWU(2),
    /**
     * North-East Up <br>
     * Relative Coordinates: (0,1,1) <br>
     * Index: 3
     */
    NEU(3),
    /**
     * South-West Down <br>
     * Relative Coordinates: (1,0,0) <br>
     * Index: 4
     */
    SWD(4),
    /**
     * South-East Down <br>
     * Relative Coordinates: (1,0,1) <br>
     * Index: 5
     */
    SED(5),
    /**
     * South-West Up <br>
     * Relative Coordinates: (1,1,0) <br>
     * Index: 6
     */
    SWU(6),
    /**
     * South-East Up <br>
     * Relative Coordinates: (1,1,1) <br>
     * Index: 7
     */
    SEU(7)
    ;

    public final int index;
    OctDirection(int i) {
        this.index = i;
    }


    /**
     * @return The current direction as a Pos3D vector.
     */
    public Pos3D asVector() {
        return switch (this) {
            case NWD -> Pos3D.ZERO;
            case NED -> new Pos3D(0,0, 1);
            case NWU -> new Pos3D(0,1, 0);
            case NEU -> new Pos3D(0,1, 1);
            case SWD -> new Pos3D(1,0, 0);
            case SED -> new Pos3D(1,0, 1);
            case SWU -> new Pos3D(1,1, 0);
            case SEU -> new Pos3D(1,1, 1);
        };
    }

    public int asColor() {
        return switch (this) {
            case NWD -> 0x000000;
            case NED -> 0x0000FF;
            case NWU -> 0x00FF00;
            case NEU -> 0x00FFFF;
            case SWD -> 0xFF0000;
            case SED -> 0xFF00FF;
            case SWU -> 0xFFFF00;
            case SEU -> 0xFFFFFF;
        };
    }

    public BlockState asState() {
        return switch (this) {
            case NWD -> Blocks.BLACK_CONCRETE.getDefaultState();
            case NED -> Blocks.BLUE_CONCRETE.getDefaultState();
            case NWU -> Blocks.GREEN_CONCRETE.getDefaultState();
            case NEU -> Blocks.CYAN_CONCRETE.getDefaultState();
            case SWD -> Blocks.RED_CONCRETE.getDefaultState();
            case SED -> Blocks.PURPLE_CONCRETE.getDefaultState();
            case SWU -> Blocks.YELLOW_CONCRETE.getDefaultState();
            case SEU -> Blocks.WHITE_CONCRETE.getDefaultState();
        };
    }
}
