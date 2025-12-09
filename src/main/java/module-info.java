module com.solarsystem {
    // REQUIRED MODULES
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;  // ← THIS IS FOR 3D SUPPORT!

    // ALLOW JAVAFX 3D TO ACCESS YOUR CLASSES
    opens com.solarsystem to javafx.graphics;

    // EXPORT YOUR PACKAGE
    exports com.solarsystem;
}