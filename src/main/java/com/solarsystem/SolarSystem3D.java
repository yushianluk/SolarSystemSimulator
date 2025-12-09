package com.solarsystem;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class SolarSystem3D extends Application {

    // ========== SCALE CONSTANTS ==========
    // Adjusted for VISIBILITY (planets will be visible but still to scale)
    private static final double SIZE_SCALE = 1.0 / 10000.0;       // Make planets visible
    private static final double DISTANCE_SCALE = 1.0 / 20000000.0; // Compress distances more
    private static final double AU_IN_SIM_UNITS = 100.0;          // 1 AU = 100 sim units

    // ========== PLANET RADII (SCALED & VISIBLE) ==========
    private static final double SUN_RADIUS = 696340 * SIZE_SCALE * 2;    // ≈ 139.27 (make Sun bigger)
    private static final double MERCURY_RADIUS = 2439.7 * SIZE_SCALE * 20;  // ≈ 4.88
    private static final double VENUS_RADIUS = 6051.8 * SIZE_SCALE * 20;    // ≈ 12.10
    private static final double EARTH_RADIUS = 6371 * SIZE_SCALE * 20;      // ≈ 12.74
    private static final double MARS_RADIUS = 3389.5 * SIZE_SCALE * 20;     // ≈ 6.78
    private static final double JUPITER_RADIUS = 69911 * SIZE_SCALE * 20;   // ≈ 139.82
    private static final double SATURN_RADIUS = 58232 * SIZE_SCALE * 20;    // ≈ 116.46
    private static final double URANUS_RADIUS = 25362 * SIZE_SCALE * 20;    // ≈ 50.72
    private static final double NEPTUNE_RADIUS = 24622 * SIZE_SCALE * 20;   // ≈ 49.24

    // ========== PLANET DISTANCES (SCALED) ==========
    private static final double MERCURY_DISTANCE = 0.387 * AU_IN_SIM_UNITS;  // ≈ 38.7
    private static final double VENUS_DISTANCE = 0.723 * AU_IN_SIM_UNITS;    // ≈ 72.3
    private static final double EARTH_DISTANCE = 1.0 * AU_IN_SIM_UNITS;      // = 100.0
    private static final double MARS_DISTANCE = 1.524 * AU_IN_SIM_UNITS;     // ≈ 152.4
    private static final double JUPITER_DISTANCE = 5.203 * AU_IN_SIM_UNITS;  // ≈ 520.3
    private static final double SATURN_DISTANCE = 9.537 * AU_IN_SIM_UNITS;   // ≈ 953.7
    private static final double URANUS_DISTANCE = 19.191 * AU_IN_SIM_UNITS;  // ≈ 1919.1
    private static final double NEPTUNE_DISTANCE = 30.069 * AU_IN_SIM_UNITS; // ≈ 3006.9

    // Window dimensions
    private static final int WIDTH = 1400;
    private static final int HEIGHT = 900;

    // 3D Objects
    private Sphere sun;
    private Sphere mercury, venus, earth, mars;
    private Sphere jupiter, saturn, uranus, neptune;

    // Camera controls
    private double mouseX, mouseY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    // Animation
    private AnimationTimer animationTimer;
    private double time = 0;
    private boolean isPaused = false;
    private double timeSpeed = 1.0;

    @Override
    public void start(Stage primaryStage) {
        // Create root container for 3D objects
        Group root = new Group();

        // Create 3D scene
        Scene scene = new Scene(root, WIDTH, HEIGHT, true);
        scene.setFill(Color.BLACK); // Space is black

        // Create 3D camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(50000);
        camera.setFieldOfView(70);

        // Apply camera transformations (for rotation/zoom)
        camera.getTransforms().addAll(rotateX, rotateY);
        camera.setTranslateZ(-800); // Start position to see inner planets

        // Create ALL celestial bodies
        createAllCelestialBodies(root);

        // Add everything to scene
        root.getChildren().add(camera);
        scene.setCamera(camera);

        // Setup controls
        setupMouseControls(scene, camera);
        setupKeyboardControls(scene, camera);

        // Create and start animation
        createAnimation();

        // Setup stage
        primaryStage.setTitle("Solar System Simulator - All 8 Planets");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("✅ Solar System with all 8 planets running!");
        System.out.println("Controls: Mouse drag=rotate, Scroll=zoom, Space=pause");
        System.out.println("1-4: Camera presets, +/-: Speed, R: Reset camera, T: Reset time");
    }

    private void createAllCelestialBodies(Group root) {
        // Create Sun
        sun = createCelestialBody("Sun", SUN_RADIUS, Color.YELLOW, 0, true);

        // Create all 8 planets
        mercury = createCelestialBody("Mercury", MERCURY_RADIUS, Color.GRAY, MERCURY_DISTANCE, false);
        venus = createCelestialBody("Venus", VENUS_RADIUS, Color.ORANGERED, VENUS_DISTANCE, false);
        earth = createCelestialBody("Earth", EARTH_RADIUS, Color.DODGERBLUE, EARTH_DISTANCE, false);
        mars = createCelestialBody("Mars", MARS_RADIUS, Color.RED, MARS_DISTANCE, false);
        jupiter = createCelestialBody("Jupiter", JUPITER_RADIUS, Color.SANDYBROWN, JUPITER_DISTANCE, false);
        saturn = createCelestialBody("Saturn", SATURN_RADIUS, Color.GOLD, SATURN_DISTANCE, false);
        uranus = createCelestialBody("Uranus", URANUS_RADIUS, Color.LIGHTBLUE, URANUS_DISTANCE, false);
        neptune = createCelestialBody("Neptune", NEPTUNE_RADIUS, Color.DARKBLUE, NEPTUNE_DISTANCE, false);

        // Add all to scene
        root.getChildren().addAll(sun, mercury, venus, earth, mars,
                jupiter, saturn, uranus, neptune);

        // Print planet info
        printPlanetInfo();
    }

    private Sphere createCelestialBody(String name, double radius, Color color,
                                       double distance, boolean isSun) {
        Sphere body = new Sphere(radius);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(color);

        if (isSun) {
            // Sun glows
            material.setSelfIlluminationMap(material.getDiffuseMap());
            material.setSpecularColor(Color.WHITE);
            material.setSpecularPower(30);
        } else {
            // Planets have some shine
            material.setSpecularColor(Color.WHITE);
            material.setSpecularPower(10);
        }

        body.setMaterial(material);
        body.setTranslateX(distance);

        return body;
    }

    private void printPlanetInfo() {
        System.out.println("\n=== Solar System Info ===");
        System.out.println("Sun radius: " + SUN_RADIUS + " units");
        System.out.println("\nPlanet distances from Sun:");
        System.out.printf("Mercury: %.1f units\n", MERCURY_DISTANCE);
        System.out.printf("Venus: %.1f units\n", VENUS_DISTANCE);
        System.out.printf("Earth: %.1f units\n", EARTH_DISTANCE);
        System.out.printf("Mars: %.1f units\n", MARS_DISTANCE);
        System.out.printf("Jupiter: %.1f units\n", JUPITER_DISTANCE);
        System.out.printf("Saturn: %.1f units\n", SATURN_DISTANCE);
        System.out.printf("Uranus: %.1f units\n", URANUS_DISTANCE);
        System.out.printf("Neptune: %.1f units\n", NEPTUNE_DISTANCE);
        System.out.println("========================\n");
    }

    private void createAnimation() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    // Update time (scaled by timeSpeed)
                    time += 0.01 * timeSpeed;

                    // Update orbits for ALL planets (realistic relative speeds)
                    updateOrbit(mercury, MERCURY_DISTANCE, 4.15); // Mercury: 4.15x Earth speed
                    updateOrbit(venus, VENUS_DISTANCE, 1.62);     // Venus: 1.62x Earth speed
                    updateOrbit(earth, EARTH_DISTANCE, 1.0);      // Earth: baseline
                    updateOrbit(mars, MARS_DISTANCE, 0.53);       // Mars: 0.53x Earth speed
                    updateOrbit(jupiter, JUPITER_DISTANCE, 0.084); // Jupiter: 0.084x
                    updateOrbit(saturn, SATURN_DISTANCE, 0.034);  // Saturn: 0.034x
                    updateOrbit(uranus, URANUS_DISTANCE, 0.012);  // Uranus: 0.012x
                    updateOrbit(neptune, NEPTUNE_DISTANCE, 0.006); // Neptune: 0.006x

                    // Rotate planets on their axes
                    rotateBody(mercury, 0.5);
                    rotateBody(venus, 0.3);
                    rotateBody(earth, 0.5);
                    rotateBody(mars, 0.5);
                    rotateBody(jupiter, 2.0); // Gas giants spin faster
                    rotateBody(saturn, 2.0);
                    rotateBody(uranus, 1.5);
                    rotateBody(neptune, 1.5);
                    rotateBody(sun, 0.1); // Sun rotates slowly
                }
            }
        };
        animationTimer.start();
    }

    private void updateOrbit(Sphere planet, double orbitRadius, double speedFactor) {
        // Realistic orbital mechanics: inner planets move faster
        double orbitSpeed = 0.3 * speedFactor; // Base speed adjusted by planet factor

        double x = orbitRadius * Math.cos(time * orbitSpeed);
        double z = orbitRadius * Math.sin(time * orbitSpeed);

        planet.setTranslateX(x);
        planet.setTranslateZ(z);
    }

    private void rotateBody(Sphere body, double speed) {
        body.setRotate(body.getRotate() + speed * timeSpeed);
    }

    private void setupMouseControls(Scene scene, Camera camera) {
        // Mouse press: remember position
        scene.setOnMousePressed((MouseEvent event) -> {
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });

        // Mouse drag: rotate camera
        scene.setOnMouseDragged((MouseEvent event) -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

            double deltaX = mouseX - mouseOldX;
            double deltaY = mouseY - mouseOldY;

            // Rotate around X and Y axes
            rotateX.setAngle(rotateX.getAngle() - deltaY * 0.3);
            rotateY.setAngle(rotateY.getAngle() + deltaX * 0.3);

            mouseOldX = mouseX;
            mouseOldY = mouseY;
        });

        // Mouse scroll: zoom in/out
        scene.setOnScroll((ScrollEvent event) -> {
            double delta = event.getDeltaY();
            double currentZ = camera.getTranslateZ();
            camera.setTranslateZ(currentZ + delta * 0.5);
        });
    }

    private void setupKeyboardControls(Scene scene, Camera camera) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case SPACE:
                    // Toggle pause/play
                    isPaused = !isPaused;
                    System.out.println(isPaused ? "⏸️ Paused" : "▶️ Playing");
                    break;

                case PLUS:
                case EQUALS:
                    // Increase speed
                    timeSpeed = Math.min(timeSpeed + 0.5, 50.0);
                    System.out.printf("Speed: %.1fx\n", timeSpeed);
                    break;

                case MINUS:
                    // Decrease speed
                    timeSpeed = Math.max(timeSpeed - 0.5, 0.1);
                    System.out.printf("Speed: %.1fx\n", timeSpeed);
                    break;

                case R:
                    // Reset camera
                    rotateX.setAngle(0);
                    rotateY.setAngle(0);
                    camera.setTranslateZ(-800);
                    System.out.println("Camera reset");
                    break;

                case T:
                    // Reset time
                    time = 0;
                    System.out.println("Time reset");
                    break;

                case DIGIT1:
                    // Inner planets view
                    camera.setTranslateZ(-300);
                    System.out.println("View: Inner planets");
                    break;

                case DIGIT2:
                    // Outer planets view
                    camera.setTranslateZ(-2000);
                    System.out.println("View: Outer planets");
                    break;

                case DIGIT3:
                    // Whole solar system view
                    camera.setTranslateZ(-6000);
                    System.out.println("View: Entire solar system");
                    break;

                case DIGIT4:
                    // Sun close-up
                    camera.setTranslateZ(-100);
                    System.out.println("View: Sun close-up");
                    break;

                case I:
                    // Info
                    printPlanetInfo();
                    break;
            }
        });
    }
}