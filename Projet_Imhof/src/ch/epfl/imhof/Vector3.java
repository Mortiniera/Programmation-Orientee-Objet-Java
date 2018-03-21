package ch.epfl.imhof;

/**
 * a 3D vector representation
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public class Vector3 {
    public final double x, y, z;

    /**
     * 
     * @param x
     *            the x double value
     * @param y
     *            the y double value
     * @param z
     *            the z double value
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 
     * @return the norm of the vector
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * 
     * @return the normalized vector
     */
    public Vector3 normalized() {
        return new Vector3(x / norm(), y / norm(), z / norm());
    }

    /**
     * 
     * @param that
     *            another vector3 vector
     * @return the scalar product between the current vector3 and that vector3
     */
    public double scalarProduct(Vector3 that) {
        return x * that.x + y * that.y + z * that.z;
    }
}
