import javafx.scene.paint.Color;

/**
 * Basic object representing one particle.
 */
public class Particle {
    public Vector2D pos;
    public Vector2D vel;
    public Vector2D stickyPos;

    public Color color;

    /**
     * Constructor.
     */
    public Particle() {
        pos = new Vector2D(0, 0);
        vel = new Vector2D(0, 0);
        stickyPos = new Vector2D(0, 0);
    }

    /**
     * Step forward one time step.
     */
    public void update() {
        pos = pos.add(vel);
    }

    public String toString() {
        return String.format("[Pos: %s, Vel: %s]", pos, vel);
    }
}
