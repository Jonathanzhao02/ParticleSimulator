import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

/**
 * Custom Canvas class for displaying animation visuals.
 */
public class AnimatedCanvas extends Canvas {
    private static final Color OPAQUE_COLOR = Color.color(0, 0, 0, 0.5);
    private AnimationDriver driver;

    /**
     * Constructor.
     * @param width The width in pixels..
     * @param height The height in pixels.
     */
    public AnimatedCanvas(double width, double height) {
        super(width, height);
        driver = new AnimationDriver(this);
        clear();

        // Map mouse events on the canvas to the animation driver.
        setOnMouseClicked(e -> driver.onClicked(e));
        setOnMouseDragged(e -> driver.onDragged(e));
        setOnMousePressed(e -> driver.onPressed(e));
        setOnMouseReleased(e -> driver.onReleased(e));
        setOnMouseMoved(e -> driver.onMoved(e));
    }

    /**
     * Clear the entire canvas.
     */
    public void clear() {
        getGraphicsContext2D().setStroke(Color.TRANSPARENT);
        getGraphicsContext2D().setFill(Color.BLACK);
        getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Clear the entire canvas with a slightly-transparent background.
     */
    private void partialClear() {
        getGraphicsContext2D().setStroke(Color.TRANSPARENT);
        getGraphicsContext2D().setFill(OPAQUE_COLOR);
        getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Set up the canvas and animation driver for playing from scratch.
     */
    public void initialize() {
        clear();
        driver.initialize();
    }

    /**
     * Step the animation forward by one frame.
     */
    public void step() {
        partialClear();
        driver.run();
    }

    /**
     * Get the animation driver.
     * @return The animation driver.
     */
    public AnimationDriver getDriver() {
        return driver;
    }
}
