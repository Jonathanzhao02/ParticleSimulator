import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Controller class for all animation visuals.
 */
public class AnimationDriver implements Runnable {
    private final Vector2D SHAPE_CENTER = new Vector2D(400, 400);

    /** User-controllable Parameters **/
    private double shapeRadius = 300;           // Radius of the shape created on double-left/right-click
    private double shapeAngle = 0;              // Angle to rotate the square
    private double shapeForce = 1.2;            // Force of particle attraction to shape location
    private double shapeElasticity = 0.03;      // How strongly the shape force is applied to particles
    private double shapeExponent = 0.2;         // Controls how distance affects the shape force

    private double magnetRadius = 100;          // Radius of the magnet on left/right-click
    private double magnetForce = 0.3;           // Force of particle attraction to mouse location

    private int particleNum = 500;              // Number of particles in the scene
    private double particleSize = 1;            // Size of the particles in pixels
    private double particleElasticity = 0.999;  // How elastic particle collisions are (1 = fully elastic)
    private double startingVelocity = 1;        // The maximum magnitude of initial velocities
    private boolean particleCollisions = true;  // Whether particle collisions are enabled

    private AnimatedCanvas canvas;
    private ArrayList<Particle> particles = new ArrayList<>(particleNum);
    private Vector2D mousePos = new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    private boolean magnetized = false;
    private boolean circlize = false;
    private boolean squarize = false;

    /**
     * Constructor.
     * @param canvas The canvas on which to draw all animation visuals.
     */
    public AnimationDriver(AnimatedCanvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Generate all particles and reset internal parameters.
     */
    public void initialize() {
        particles.clear();
        magnetized = false;
        circlize = false;
        squarize = false;

        for (int i = 0; i < particleNum; i++) {
            particles.add(generateParticle());
        }
    }

    /**
     * Applies all frame-by-frame logic.
     */
    public void run() {
        // Draw and update each and every particle
        // and check for boundary collisions
        for (Particle p : particles) {
            // Apply velocity to position
            p.update();

            // Check for x/y collisions, reversing velocity as needed
            // and resetting positions
            if (p.pos.x > canvas.getWidth() - 1 || p.pos.x < 0) {
                p.pos.x = Math.min(canvas.getWidth() - 1, Math.max(0, p.pos.x));
                p.vel.x *= -particleElasticity;
            }

            if (p.pos.y > canvas.getHeight() - 1 || p.pos.y < 0) {
                p.pos.y = Math.min(canvas.getHeight() - 1, Math.max(0, p.pos.y));
                p.vel.y *= -particleElasticity;
            }

            // Draw the particle, using a single pixel if it is too small
            if (particleSize >= 1) {
                canvas.getGraphicsContext2D().setFill(p.color);
                canvas.getGraphicsContext2D().fillOval(p.pos.x - particleSize, p.pos.y - particleSize,
                        2 * particleSize, 2 * particleSize);
            } else {
                canvas.getGraphicsContext2D().getPixelWriter().setColor(
                        (int)p.pos.x,
                        (int)p.pos.y,
                        p.color
                );
            }
        }

        // If shape formation is active, apply a force pulling
        // particles to their designated positions within the shape.
        if (circlize || squarize) {
            for (Particle p : particles) {
                // Calculate "optimal" velocity vector
                Vector2D posDiff = p.stickyPos.sub(p.pos);

                // Partially normalize velocity vector
                posDiff = posDiff.multiply(shapeForce / Math.pow(posDiff.dot(posDiff), shapeExponent));

                // Find difference between desired velocity and current particle velocity
                Vector2D velDiff = posDiff.sub(p.vel);

                // Add a portion of the difference
                p.vel = p.vel.add(velDiff.multiply(shapeElasticity));
            }
        }

        // If magnetism is active, apply a force pulling
        // particles to the user's current mouse position.
        if (magnetized) {
            for (Particle p : particles) {
                // Calculate "optimal" velocity vector
                Vector2D posDiff = mousePos.sub(p.pos);

                // Find distance squared between particle and mouse position
                double distanceSq = posDiff.dot(posDiff);

                // If within radius, apply a normalized velocity towards the mouse, independent of distance
                if (distanceSq < magnetRadius * magnetRadius) {
                    p.vel = p.vel.add(posDiff.multiply(magnetForce / Math.sqrt(distanceSq)));
                }
            }
        }

        // If collisions enabled, check all particles for collisions
        if (particleCollisions) {
            checkCollisions();
        }
    }

    /**
     * Generate a single particle.
     * @return The generated particle.
     */
    private Particle generateParticle() {
        Particle p = new Particle();
        p.pos.x = Math.random() * canvas.getWidth();
        p.pos.y = Math.random() * canvas.getHeight();
        p.vel = new Vector2D(
                (Math.random() - 0.5) * 2 * startingVelocity,
                (Math.random() - 0.5) * 2 * startingVelocity
        );
        p.color = Color.color(Math.random(), Math.random(), Math.random());
        return p;
    }

    /**
     * Iterate over the list and compare every pair of particles.
     */
    private void checkCollisions() {
        // Keeping list of compared particles reduces a bit of runtime
        ArrayList<Particle> toQuery = new ArrayList<>(particles);

        for (Particle p1 : particles) {
            toQuery.remove(p1);
            boolean collided = false;

            for (Particle p2 : toQuery) {
                Vector2D posDiff = p1.pos.sub(p2.pos);
                double distanceSq = posDiff.dot(posDiff);

                // If other particle is close enough to current particle,
                // do a bunch of velocity calculations (assuming same mass and whatnot)
                if (distanceSq <= (particleSize * particleSize * 4) && distanceSq > 0) {
                    collided = true;
                    double dot = p1.vel.sub(p2.vel).dot(posDiff) / distanceSq;
                    Vector2D velDiff = posDiff.multiply(dot);

                    Vector2D newVel1 = p1.vel.sub(velDiff);
                    Vector2D newVel2 = p2.vel.add(velDiff);

                    p1.vel = newVel1;
                    p2.vel = newVel2;
                }
            }

            // If a collision occurred at any point, apply particleElasticity
            if (collided) {
                p1.vel = p1.vel.multiply(particleElasticity);
            }
        }
    }

    /**
     * Generate the circle shape for every particle.
     */
    private void generateCircle() {
        double currentAngle = 0;
        double deltaTheta = 2 * Math.PI / particleNum;

        // Just loop a full cycle around a circle and set stickyPos
        // of each particle to the corresponding position on the circle.
        for (int i = 0; i < particleNum; i++) {
            particles.get(i).stickyPos = new Vector2D(
                    Math.sin(currentAngle),
                    Math.cos(currentAngle)
            ).multiply(shapeRadius).add(SHAPE_CENTER);
            currentAngle -= deltaTheta;
        }
    }

    /**
     * Generate the square shape for every particle.
     */
    private void generateSquare() {
        // Start at the bottom right corner
        Vector2D currentPos = new Vector2D(shapeRadius, shapeRadius);
        double deltaLength = 8 * shapeRadius / particleNum;

        for (int i = 0; i < particleNum; i++) {
            // Rotate position by shapeAngle to get the rotated square shape
            particles.get(i).stickyPos = currentPos.rotate(shapeAngle).add(SHAPE_CENTER);

            // Start moving left, up, right, down, to make full square
            switch (i * 4 / particleNum) {
                case 0 -> currentPos.x -= deltaLength;
                case 1 -> currentPos.y -= deltaLength;
                case 2 -> currentPos.x += deltaLength;
                case 3 -> currentPos.y += deltaLength;
            }

        }
    }

    /**
     * Handles click events for the canvas.
     * @param e The event.
     */
    public void onClicked(MouseEvent e) {
        // If detected double/quadruple/etc. click, handle shape logic
        if (e.getClickCount() % 2 == 0) {
            // If LMB, prepare to create circle out of particles
            if (e.getButton() == MouseButton.PRIMARY) {
                circlize = !circlize;
                squarize = false;
            // If RMB, prepare to create square out of particles
            } else if (e.getButton() == MouseButton.SECONDARY) {
                squarize = !squarize;
                circlize = false;
            }

            if (circlize) {
                generateCircle();
            } else if (squarize) {
                generateSquare();
            }
        }
    }

    /**
     * Handles pressed events for the canvas.
     * @param e The event.
     */
    public void onPressed(MouseEvent e) {
        magnetized = true;
    }

    /**
     * Handles released events for the canvas.
     * @param e The event.
     */
    public void onReleased(MouseEvent e) {
        magnetized = false;
    }

    /**
     * Handles dragged events for the canvas.
     * @param e The event.
     */
    public void onDragged(MouseEvent e) {
        mousePos.x = e.getX();
        mousePos.y = e.getY();
    }

    /**
     * Handles moved events for the canvas.
     * @param e The event.
     */
    public void onMoved(MouseEvent e) {
        mousePos.x = e.getX();
        mousePos.y = e.getY();
    }

    /** SETTERS **/
    public void setShapeRadius(double shapeRadius) {
        this.shapeRadius = shapeRadius;

        // Regenerate the shape, since radius has changed
        if (circlize) {
            generateCircle();
        } else if (squarize) {
            generateSquare();
        }
    }

    public void setShapeAngle(double shapeAngle) {
        this.shapeAngle = shapeAngle;

        // Regenerate the square if selected, since angle has changed
        if (squarize) {
            generateSquare();
        }
    }

    public void setShapeForce(double shapeForce) {
        this.shapeForce = shapeForce;
    }

    public void setShapeElasticity(double shapeElasticity) {
        this.shapeElasticity = shapeElasticity;
    }

    public void setShapeExponent(double shapeExponent) {
        this.shapeExponent = shapeExponent;
    }

    public void setMagnetRadius(double magnetRadius) {
        this.magnetRadius = magnetRadius;
    }

    public void setMagnetForce(double magnetForce) {
        this.magnetForce = magnetForce;
    }

    public void setParticleNum(int particleNum) {
        this.particleNum = particleNum;

        // Generate or trim particles according to new particleNum
        while (particles.size() < particleNum) {
            particles.add(generateParticle());
        }

        while (particles.size() > particleNum) {
            particles.remove(particles.get(0));
        }

        // Regenerate shape, since particles have been modified
        if (circlize) {
            generateCircle();
        } else if (squarize) {
            generateSquare();
        }
    }

    public void setParticleSize(double particleSize) {
        this.particleSize = particleSize;
    }

    public void setParticleElasticity(double particleElasticity) {
        this.particleElasticity = particleElasticity;
    }

    public void setParticleCollisions(boolean particleCollisions) {
        this.particleCollisions = particleCollisions;
    }

    public void setStartingVelocity(double startingVelocity) {
        this.startingVelocity = startingVelocity;
    }
}
