package com.solarsystem;

import javafx.scene.paint.Color;

public class PlanetData {
    public static class CelestialBody {
        String name;
        double radiusKm;          // Real radius in km
        double orbitRadiusAU;     // Distance from Sun in Astronomical Units
        double orbitalPeriodYears; // Years to orbit Sun
        Color color;
        double axialTilt;         // Degrees
        double rotationPeriodHours; // Hours for one rotation

        public CelestialBody(String name, double radiusKm, double orbitRadiusAU,
                             double orbitalPeriodYears, Color color,
                             double axialTilt, double rotationPeriodHours) {
            this.name = name;
            this.radiusKm = radiusKm;
            this.orbitRadiusAU = orbitRadiusAU;
            this.orbitalPeriodYears = orbitalPeriodYears;
            this.color = color;
            this.axialTilt = axialTilt;
            this.rotationPeriodHours = rotationPeriodHours;
        }
    }

    // REAL SOLAR SYSTEM DATA (NASA values)
    public static final CelestialBody SUN = new CelestialBody(
            "Sun", 696340, 0, 0, Color.YELLOW, 7.25, 609.6
    );

    public static final CelestialBody MERCURY = new CelestialBody(
            "Mercury", 2439.7, 0.387, 0.241, Color.GRAY, 0.034, 1407.6
    );

    public static final CelestialBody VENUS = new CelestialBody(
            "Venus", 6051.8, 0.723, 0.615, Color.ORANGE, 177.4, 5832.5
    );

    public static final CelestialBody EARTH = new CelestialBody(
            "Earth", 6371, 1.0, 1.0, Color.BLUE, 23.44, 23.9
    );

    public static final CelestialBody MARS = new CelestialBody(
            "Mars", 3389.5, 1.524, 1.881, Color.RED, 25.19, 24.6
    );

    public static final CelestialBody JUPITER = new CelestialBody(
            "Jupiter", 69911, 5.203, 11.86, Color.ORANGE, 3.13, 9.9
    );

    public static final CelestialBody SATURN = new CelestialBody(
            "Saturn", 58232, 9.537, 29.46, Color.GOLD, 26.73, 10.7
    );

    public static final CelestialBody URANUS = new CelestialBody(
            "Uranus", 25362, 19.191, 84.01, Color.LIGHTBLUE, 97.77, 17.2
    );

    public static final CelestialBody NEPTUNE = new CelestialBody(
            "Neptune", 24622, 30.069, 164.8, Color.DARKBLUE, 28.32, 16.1
    );
}