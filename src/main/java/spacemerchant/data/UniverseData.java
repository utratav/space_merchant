package spacemerchant.data;

import spacemerchant.model.EconomyType;
import spacemerchant.model.Location;

import java.util.List;

public class UniverseData {
    private static final Location EARTH_ORBIT = new Location("Orbita Ziemi", 0, 0, EconomyType.INDUSTRIAL, true);
    private static final Location LUNA_PORT = new Location("Port Luna", 2, 1, EconomyType.HIGH_TECH, true);
    private static final Location MARS_COLONY = new Location("Kolonia Mars", 5, 2, EconomyType.MINING, true);
    private static final Location CERES_RELAY = new Location("Przekaźnik Ceres", 7, -1, EconomyType.MINING, false);
    private static final Location GANYMEDE_DOCK = new Location("Dok Ganimedes", 10, 1, EconomyType.INDUSTRIAL, true);
    private static final Location EUROPA_RESEARCH = new Location("Europa Research", 11, -2, EconomyType.HIGH_TECH, true);
    private static final Location TITAN_REFINERY = new Location("Rafineria Tytan", 15, 0, EconomyType.INDUSTRIAL, true);
    private static final Location SATURN_RINGS = new Location("Pierścienie Saturna", 14, 3, EconomyType.MINING, false);
    private static final Location ORION_GATE = new Location("Brama Oriona", 18, 2, EconomyType.HIGH_TECH, false);
    private static final Location NEW_EDEN = new Location("Nowy Eden", 22, 1, EconomyType.AGRICULTURAL, true);
    private static final Location HELIOS_FARM = new Location("Farmy Heliosa", 21, -3, EconomyType.AGRICULTURAL, true);
    private static final Location VEGA_MARKET = new Location("Giełda Vega", 26, 0, EconomyType.HIGH_TECH, true);
    private static final Location KEPPLER_OUTPOST = new Location("Posterunek Kepler", 28, 4, EconomyType.MINING, true);
    private static final Location DARK_NEBULA = new Location("Ciemna Mgławica", 24, 5, EconomyType.MINING, false);

    private static final List<Location> LOCATIONS = List.of(
            EARTH_ORBIT,
            LUNA_PORT,
            MARS_COLONY,
            CERES_RELAY,
            GANYMEDE_DOCK,
            EUROPA_RESEARCH,
            TITAN_REFINERY,
            SATURN_RINGS,
            ORION_GATE,
            NEW_EDEN,
            HELIOS_FARM,
            VEGA_MARKET,
            KEPPLER_OUTPOST,
            DARK_NEBULA
    );

    static {
        EARTH_ORBIT.addPath(LUNA_PORT, 8.0);
        EARTH_ORBIT.addPath(MARS_COLONY, 14.0);
        LUNA_PORT.addPath(MARS_COLONY, 10.0);
        LUNA_PORT.addPath(CERES_RELAY, 18.0);
        MARS_COLONY.addPath(CERES_RELAY, 11.0);
        MARS_COLONY.addPath(GANYMEDE_DOCK, 20.0);
        CERES_RELAY.addPath(GANYMEDE_DOCK, 13.0);
        CERES_RELAY.addPath(EUROPA_RESEARCH, 16.0);
        GANYMEDE_DOCK.addPath(EUROPA_RESEARCH, 7.0);
        GANYMEDE_DOCK.addPath(TITAN_REFINERY, 19.0);
        EUROPA_RESEARCH.addPath(TITAN_REFINERY, 17.0);
        TITAN_REFINERY.addPath(SATURN_RINGS, 6.0);
        SATURN_RINGS.addPath(ORION_GATE, 21.0);
        TITAN_REFINERY.addPath(ORION_GATE, 24.0);
        ORION_GATE.addPath(NEW_EDEN, 18.0);
        ORION_GATE.addPath(HELIOS_FARM, 20.0);
        NEW_EDEN.addPath(HELIOS_FARM, 9.0);
        NEW_EDEN.addPath(VEGA_MARKET, 15.0);
        HELIOS_FARM.addPath(VEGA_MARKET, 17.0);
        VEGA_MARKET.addPath(KEPPLER_OUTPOST, 16.0);
        KEPPLER_OUTPOST.addPath(DARK_NEBULA, 12.0);
        DARK_NEBULA.addPath(NEW_EDEN, 22.0);
    }

    private UniverseData() {
    }

    public static Location getStartingLocation() {
        return EARTH_ORBIT;
    }

    public static List<Location> getLocations() {
        return LOCATIONS;
    }
}
