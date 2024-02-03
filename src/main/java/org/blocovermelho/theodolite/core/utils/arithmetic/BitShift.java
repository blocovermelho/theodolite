package org.blocovermelho.theodolite.core.utils.arithmetic;

public class BitShift {
    /**
     * Equivalent to: <br>
     * {@literal 1 << value, } <br>
     * 2^value, <br>
     * Math.pow(2, value) <br><br>
     *
     * Note: Math.pow() isn't identical for large values where bits would be lost in the shift, however for medium to small values they function the same. <br><br>
     *
     * Can also be used to replace bit shifts in the format: <br>
     * {@literal multiplier << value; } <br>
     * multiplier * powerOfTwo(value);
     */

    public static int powerOfTwo(int value) { return 1 << value; }
    public static long powerOfTwo(long value) { return 1L << value; }

    /**
     * Equivalent to: <br>
     * value >> power, <br>
     * value / 2^power <br><br>
     *
     * Note: value / 2^power isn't identical for negative values
     */

    public static int half(int value) { return value >> 1; }
    public static long half(long value) { return value >> 1; }

    /**
     * Equivalent to: <br>
     * value >> power, <br>
     * value / 2^power <br><br>
     *
     * Note: value / 2^power isn't identical for negative values
     */

    public static int divideByPowerOfTwo(int value, int power) { return value >> power; }
    public static long divideByPowerOfTwo(long value, long power) { return value >> power; }


    /**
     * Equivalent to: <br>
     * {@literal value << 1, } <br>
     * value^2, <br>
     * Math.pow(value, 2) <br><br>
     *
     * Note: Math.pow() isn't identical for large values where bits would be lost in the shift, however for medium to small values they function the same.
     */

    public static int square(int value) { return value << 1; }
    public static long square(long value) { return value << 1; }


    /**
     * Equivalent to: <br>
     * {@literal value << power, } <br>
     * value^power, <br>
     * Math.pow(value, power) <br><br>
     *
     * Note: Math.pow() isn't identical for large values where bits would be lost in the shift, however for medium to small values they function the same.
     */
    public static int pow(int value, int power) { return value << power; }
    public static long pow(long value, long power) { return value << power; }




}
