package org.blocovermelho.theodolite.core.utils;

public class NumericalConstants {

    /** 512 blocks wide */
    public static final byte REGION_DETAIL_LEVEL = 9;
    /** 16 blocks wide */
    public static final byte CHUNK_DETAIL_LEVEL = 4;
    /** 1 block wide */
    public static final byte BLOCK_DETAIL_LEVEL = 0;

    /**
     * measured in Blocks <br>
     * detail level 9
     * 512 x 512 blocks
     */
    public static final short REGION_WIDTH = 512;
    /**
     * measured in Blocks <br>
     * detail level 4
     * 16 x 16 blocks
     */
    public static final short CHUNK_WIDTH = 16;


    /** number of chunks wide */
    public static final int REGION_WIDTH_IN_CHUNKS = REGION_WIDTH / CHUNK_WIDTH;
}
