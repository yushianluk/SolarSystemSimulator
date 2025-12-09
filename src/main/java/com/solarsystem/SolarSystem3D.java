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

    // Window dimensions
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    // 3D Objects
    private Sphere sun;
    private Sphere earth;

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
        camera.setFarClip(10000);
        camera.setFieldOfView(60);

        // Apply camera transformations (for rotation/zoom)
        camera.getTransforms().addAll(rotateX, rotateY);
        camera.setTranslateZ(-200); // Start position

        // Create celestial bodies
        createCelestialBodies(root);

        // Add everything to scene
        root.getChildren().add(camera);
        scene.setCamera(camera);

        // Setup controls
        setupMouseControls(scene, camera);
        setupKeyboardControls(scene);

        // Create and start animation
        createAnimation();

        // Setup stage
        primaryStage.setTitle("Solar System Simulator - Earth & Sun");
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("Solar System running!");
    }

    private void createCelestialBodies(Group root) {
        // Create Sun
        sun = new Sphere(15);
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseColor(Color.YELLOW);
        sunMaterial.setSpecularColor(Color.WHITE);
        sunMaterial.setSpecularPower(20);
        sun.setMaterial(sunMaterial);

        // Create Earth
        earth = new Sphere(5);
        PhongMaterial earthMaterial = new PhongMaterial();
        earthMaterial.setDiffuseColor(Color.DODGERBLUE);
        earthMaterial.setSpecularColor(Color.WHITE);
        earthMaterial.setSpecularPower(10);
        earth.setMaterial(earthMaterial);

        // Position Earth (will be animated)
        earth.setTranslateX(60);

        // Add to scene
        root.getChildren().addAll(sun, earth);
    }

    private void createAnimation() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    // Update time (scaled by timeSpeed)
                    time += 0.01 * timeSpeed;

                    // Earth circular orbit
                    double orbitRadius = 60;
                    double orbitSpeed = 0.5;

                    double earthX = orbitRadius * Math.cos(time * orbitSpeed);
                    double earthZ = orbitRadius * Math.sin(time * orbitSpeed);

                    earth.setTranslateX(earthX);
                    earth.setTranslateZ(earthZ);

                    // Optional: Earth rotation (spins on its axis)
                    earth.setRotate(earth.getRotate() + 0.5 * timeSpeed);
                }
            }
        };
        animationTimer.start();
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
            camera.setTranslateZ(currentZ + delta * 0.3);
        });
    }

    private void setupKeyboardControls(Scene scene) {
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
                    timeSpeed = Math.min(timeSpeed + 0.5, 10.0);
                    System.out.println("Speed: " + timeSpeed + "x");
                    break;

                case MINUS:
                    // Decrease speed
                    timeSpeed = Math.max(timeSpeed - 0.5, 0.1);
                    System.out.println("Speed: " + timeSpeed + "x");
                    break;

                case R:
                    // Reset camera
                    rotateX.setAngle(0);
                    rotateY.setAngle(0);
                    break;

                case T:
                    // Reset time
                    time = 0;
                    break;
            }
        });
    }
}