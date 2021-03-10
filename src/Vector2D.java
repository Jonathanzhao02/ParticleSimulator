/**
 * Basic object representing a 2D vector.
 */
public class Vector2D {
    public double x;
    public double y;

    /**
     * Constructor.
     * @param x The x position/magnitude
     * @param y The y position/magnitude
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates the dot product.
     * @param other The vector to dot with.
     * @return The dot product of the two vectors.
     */
    public double dot(Vector2D other) {
        return x * other.x + y * other.y;
    }

    /**
     * Adds another vector to this vector.
     * @param other The vector to add with.
     * @return The addition of the two vectors.
     */
    public Vector2D add(Vector2D other) {
        return new Vector2D(x + other.x, y + other.y);
    }

    /**
     * Multiplies this vector by a scalar.
     * @param scalar The scalar to multiply by.
     * @return The scaled vector.
     */
    public Vector2D multiply(double scalar) {
        return new Vector2D(x * scalar, y * scalar);
    }

    /**
     * Subtracts another vector from this vector.
     * @param other The vector to subtract.
     * @return The difference of the two vectors.
     */
    public Vector2D sub(Vector2D other) {
        return new Vector2D(x - other.x, y - other.y);
    }

    /**
     * Rotates this vector by an angle.
     * @param angle The angle in radians.
     * @return The rotation of this vector.
     */
    public Vector2D rotate(double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);

        return new Vector2D(
                x * cos - y * sin,
                x * sin + y * cos
        );
    }

    public String toString() {
        return String.format("{%.2f, %.2f}", x, y);
    }
}
