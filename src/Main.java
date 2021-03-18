import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Entry class of the program.
 */
public class Main extends Application {
    /**
     * Start entry method for JavaFX Application.
     * @param mainStage The stage everything is displayed on.
     */
    public void start(Stage mainStage) {
        BorderPane root = new BorderPane();

        AnimatedCanvas canvas = new AnimatedCanvas(800, 800);
        Animator animator = new Animator(canvas);

        VBox controlBox = new VBox();
        controlBox.setAlignment(Pos.TOP_CENTER);
        controlBox.setSpacing(10);
        controlBox.setPrefWidth(300);
        createControls(controlBox, canvas, animator);

        root.setCenter(canvas);
        root.setRight(controlBox);

        Scene scene = new Scene(root, 1100, 800);
        mainStage.setResizable(false);
        mainStage.setScene(scene);
        mainStage.setTitle("Particle Simulator");
        mainStage.show();
    }

    /**
     * Set up all the controls on the right side.
     * @param root The VBox where the controls should be located.
     * @param canvas The animated canvas on which the animation occurs.
     * @param animator The animator which controls animation play state.
     */
    private void createControls(VBox root, AnimatedCanvas canvas, Animator animator) {
        AnimationDriver driver = canvas.getDriver();

        Label title = new Label("Settings");

        Label shapeLabel = new Label("Shape");
        GridPane shapePane = new GridPane();
        shapePane.setHgap(5);
        shapePane.setAlignment(Pos.CENTER);

        Label shapeRadiusLabel = new Label("Radius");
        TextField shapeRadiusField = new TextField();
        shapeRadiusField.setText("300");

        Label shapeAngleLabel = new Label("Angle");
        TextField shapeAngleField = new TextField();
        shapeAngleField.setText("0");

        Label shapeForceLabel = new Label("Force");
        TextField shapeForceField = new TextField();
        shapeForceField.setText("1.2");

        Label shapeElasticityLabel = new Label("Elasticity");
        TextField shapeElasticityField = new TextField();
        shapeElasticityField.setText("0.03");

        Label shapeExponentLabel = new Label("Exponent");
        TextField shapeExponentField = new TextField();
        shapeExponentField.setText("0.2");

        shapePane.add(shapeRadiusLabel, 0, 0);
        shapePane.add(shapeRadiusField, 1, 0);

        shapePane.add(shapeAngleLabel, 0, 1);
        shapePane.add(shapeAngleField, 1, 1);

        shapePane.add(shapeForceLabel, 0, 2);
        shapePane.add(shapeForceField, 1, 2);

        shapePane.add(shapeElasticityLabel, 0, 3);
        shapePane.add(shapeElasticityField, 1, 3);

        shapePane.add(shapeExponentLabel, 0, 4);
        shapePane.add(shapeExponentField, 1, 4);

        Label magnetLabel = new Label("Magnet");
        GridPane magnetPane = new GridPane();
        magnetPane.setHgap(5);
        magnetPane.setAlignment(Pos.CENTER);

        Label magnetRadiusLabel = new Label("Radius");
        TextField magnetRadiusField = new TextField();
        magnetRadiusField.setText("100");

        Label magnetForceLabel = new Label("Force");
        TextField magnetForceField = new TextField();
        magnetForceField.setText("0.3");

        magnetPane.add(magnetRadiusLabel, 0, 0);
        magnetPane.add(magnetRadiusField, 1, 0);

        magnetPane.add(magnetForceLabel, 0, 1);
        magnetPane.add(magnetForceField, 1, 1);

        Label generalLabel = new Label("Particles");
        GridPane generalPane = new GridPane();
        generalPane.setHgap(5);
        generalPane.setAlignment(Pos.CENTER);

        Label generalParticlesLabel = new Label("Number");
        TextField generalParticlesField = new TextField();
        generalParticlesField.setText("500");

        Label generalParticleSizeLabel = new Label("Size");
        TextField generalParticleSizeField = new TextField();
        generalParticleSizeField.setText("1");

        Label generalElasticityLabel = new Label("Elasticity");
        TextField generalElasticityField = new TextField();
        generalElasticityField.setText("0.999");

        Label generalStartingVelocityLabel = new Label("Initial Velocity");
        TextField generalStartingVelocityField = new TextField();
        generalStartingVelocityField.setText("1");

        Label generalFrameOpacityLabel = new Label("Frame Opacity");
        TextField generalFrameOpacityField = new TextField();
        generalFrameOpacityField.setText("0.5");

        generalPane.add(generalParticlesLabel, 0, 0);
        generalPane.add(generalParticlesField, 1, 0);

        generalPane.add(generalParticleSizeLabel, 0, 1);
        generalPane.add(generalParticleSizeField, 1, 1);

        generalPane.add(generalElasticityLabel, 0, 2);
        generalPane.add(generalElasticityField, 1, 2);

        generalPane.add(generalStartingVelocityLabel, 0, 3);
        generalPane.add(generalStartingVelocityField, 1, 3);

        generalPane.add(generalFrameOpacityLabel, 0, 4);
        generalPane.add(generalFrameOpacityField, 1, 4);

        CheckBox collisonsEnabled = new CheckBox("Collisions");
        collisonsEnabled.setSelected(true);

        // Click to apply all settings
        Button applyBtn = new Button("Apply");
        applyBtn.setOnAction(e -> {
            try {
                double shapeRadius = Double.parseDouble(shapeRadiusField.getText());
                double shapeAngle = Double.parseDouble(shapeAngleField.getText());
                double shapeForce = Double.parseDouble(shapeForceField.getText());
                double shapeElasticity = Double.parseDouble(shapeElasticityField.getText());
                double shapeExponent = Double.parseDouble(shapeExponentField.getText());

                double magnetRadius = Double.parseDouble(magnetRadiusField.getText());
                double magnetForce = Double.parseDouble(magnetForceField.getText());

                int particleNum = Integer.parseInt(generalParticlesField.getText());
                double particleSize = Double.parseDouble(generalParticleSizeField.getText());
                double particleElasticity = Double.parseDouble(generalElasticityField.getText());
                double startingVelocity = Double.parseDouble(generalStartingVelocityField.getText());
                double frameOpacity = Double.parseDouble(generalFrameOpacityField.getText());

                driver.setShapeRadius(shapeRadius);
                driver.setShapeAngle(Math.toRadians(shapeAngle));
                driver.setShapeForce(shapeForce);
                driver.setShapeElasticity(shapeElasticity);
                driver.setShapeExponent(shapeExponent);

                driver.setMagnetRadius(magnetRadius);
                driver.setMagnetForce(magnetForce);

                driver.setParticleNum(particleNum);
                driver.setParticleSize(particleSize);
                driver.setParticleElasticity(particleElasticity);
                driver.setStartingVelocity(startingVelocity);
                driver.setParticleCollisions(collisonsEnabled.isSelected());

                canvas.setFrameOpacity(frameOpacity);
            } catch (NumberFormatException ex) {
                System.out.println("[ERROR]: bad number format");
            }
        });

        AnimationActionHandler handler = new AnimationActionHandler(animator);

        Button startBtn = new Button("Start");
        startBtn.setOnAction(handler);

        Button stepBtn = new Button("Step");
        stepBtn.setOnAction(e -> {
            canvas.step();
        });

        Button restartBtn = new Button("Restart");
        restartBtn.setOnAction(e -> {
            animator.reinitialize();
        });

        root.getChildren().addAll(
                title,
                shapeLabel,
                shapePane,
                magnetLabel,
                magnetPane,
                generalLabel,
                generalPane,
                collisonsEnabled,
                applyBtn,
                startBtn,
                stepBtn,
                restartBtn
        );

        VBox.setMargin(title, new Insets(20, 0, 0, 0));
    }

    /**
     * Little tiny main method.
     * @param args The useless command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Private inner class for start/stop button.
     */
    private static class AnimationActionHandler implements EventHandler<ActionEvent> {
        private boolean playing = false;
        private Animator animator;

        /**
         * Constructor.
         * @param animator The animator to start/stop on button actions.
         */
        public AnimationActionHandler(Animator animator) {
            this.animator = animator;
        }

        /**
         * Handles the event.
         * @param e The event.
         */
        public void handle(ActionEvent e) {
            Button src = (Button) e.getSource();
            playing = !playing;

            // Switch between playing/not playing states.
            if (playing) {
                src.setText("Stop");
                animator.resume();
            } else {
                src.setText("Start");
                animator.stop();
            }
        }
    }
}
