
package com.solarsystem;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;

public class SolarSystem3D extends Application {

    @Override
    public void start(Stage stage) {
        // 1. Create container for 3D objects
        Group root = new Group();

        // 2. Create 3D scene (true = enable 3D)
        Scene scene = new Scene(root, 1200, 800, true);
        scene.setFill(Color.BLACK); // Space background

        // 3. Create 3D camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-100); // Move camera back to see objects
        scene.setCamera(camera);

        // 4. Create Sun
        Sphere sun = new Sphere(10);
        PhongMaterial sunMaterial = new PhongMaterial();
        sunMaterial.setDiffuseColor(Color.YELLOW);
        sun.setMaterial(sunMaterial);

        // 5. Add Sun to scene
        root.getChildren().add(sun);

        // 6. Show window
        stage.setTitle("Solar System Simulator");
        stage.setScene(scene);
        stage.show();

        System.out.println(" Solar System is running!");
    }
}