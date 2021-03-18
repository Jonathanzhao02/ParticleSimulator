import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * Controller class for animation playback state.
 */
public class Animator {
    private Timeline timeline;
    private KeyFrame keyFrame;
    private AnimatedCanvas canvas;
    private boolean initialized = false;

    /**
     * Constructor.
     * @param canvas The canvas on which all animation will occur.
     */
    public Animator(AnimatedCanvas canvas) {
        this.canvas = canvas;
        keyFrame = new KeyFrame(Duration.millis(10), e -> canvas.step());
        timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setRate(1);
    }

    /**
     * Reinitialize the canvas to start from scratch.
     */
    public void reinitialize() {
        initialized = true;
        canvas.initialize();
    }

    /**
     * Resume (or start) playback.
     */
    public void resume() {
        if (!initialized) {
            canvas.initialize();
            initialized = true;
        }
        timeline.play();
    }

    /**
     * Stop playback.
     */
    public void stop() {
        timeline.stop();
    }
}
