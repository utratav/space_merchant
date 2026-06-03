package spacemerchant.data;

import spacemerchant.model.EconomyType;
import spacemerchant.model.Location;

import java.util.List;

public class UniverseData {
    private static final Location EARTH_ORBIT = new Location("Orbita Ziemi", 0, 0, EconomyType.INDUSTRIAL, true);
    private static final Location LUNA_PORT = new Location("Port Luna", 2, 1, EconomyType.HIGH_TECH, true);
    private static final Location MARS_COLONY = new Location("Kolonia Mars", 5, 2, EconomyType.MINING, true);
    private static final Location LAGRANGE_GATE = new Location("Wezel Lagrange L1", 1, 0, EconomyType.HIGH_TECH, false);
    private static final Location LUNA_BEACON = new Location("Boja Nawigacyjna Luna", 2, 0, EconomyType.HIGH_TECH, false);
    private static final Location MARS_APPROACH = new Location("Podejscie Marsjanskie", 4, 1, EconomyType.MINING, false);
    private static final Location PHOBOS_RELAY = new Location("Przekaznik Fobos", 5, 1, EconomyType.MINING, false);
    private static final Location ASTEROID_GATE = new Location("Brama Pasa Asteroid", 6, 0, EconomyType.MINING, false);
    private static final Location CERES_RELAY = new Location("Przekaźnik Ceres", 7, -1, EconomyType.MINING, false);
    private static final Location JOVIAN_CORRIDOR = new Location("Korytarz Jowiszowy", 8, 0, EconomyType.INDUSTRIAL, false);
    private static final Location CALLISTO_BUOY = new Location("Boja Kallisto", 9, 2, EconomyType.INDUSTRIAL, false);
    private static final Location GANYMEDE_DOCK = new Location("Dok Ganimedes", 10, 1, EconomyType.INDUSTRIAL, true);
    private static final Location EUROPA_LANE = new Location("Korytarz Europy", 10, -1, EconomyType.HIGH_TECH, false);
    private static final Location EUROPA_RESEARCH = new Location("Europa Research", 11, -2, EconomyType.HIGH_TECH, true);
    private static final Location SATURN_INBOUND = new Location("Podejscie Saturna", 13, 0, EconomyType.INDUSTRIAL, false);
    private static final Location TITAN_ORBIT = new Location("Orbita Tytana", 14, -1, EconomyType.INDUSTRIAL, false);
    private static final Location TITAN_REFINERY = new Location("Rafineria Tytan", 15, 0, EconomyType.INDUSTRIAL, true);
    private static final Location SATURN_RINGS = new Location("Pierścienie Saturna", 14, 3, EconomyType.MINING, false);
    private static final Location ORION_GATE = new Location("Brama Oriona", 18, 2, EconomyType.HIGH_TECH, false);
    private static final Location DEEP_SPACE_ANCHOR = new Location("Kotwica Glebokiej Przestrzeni", 20, 2, EconomyType.HIGH_TECH, false);
    private static final Location EDEN_TRAIL = new Location("Szlak Edenu", 21, 2, EconomyType.AGRICULTURAL, false);
    private static final Location NEW_EDEN = new Location("Nowy Eden", 22, 1, EconomyType.AGRICULTURAL, true);
    private static final Location HELIOS_BEACON = new Location("Latarnia Heliosa", 20, -2, EconomyType.AGRICULTURAL, false);
    private static final Location HELIOS_FARM = new Location("Farmy Heliosa", 21, -3, EconomyType.AGRICULTURAL, true);
    private static final Location VEGA_LANE = new Location("Korytarz Vega", 24, 1, EconomyType.HIGH_TECH, false);
    private static final Location VEGA_MARKET = new Location("Giełda Vega", 26, 0, EconomyType.HIGH_TECH, true);
    private static final Location KEPLER_GATE = new Location("Brama Keplera", 27, 2, EconomyType.MINING, false);
    private static final Location KEPPLER_OUTPOST = new Location("Posterunek Kepler", 28, 4, EconomyType.MINING, true);
    private static final Location DARK_NEBULA = new Location("Ciemna Mgławica", 24, 5, EconomyType.MINING, false);

    private static final List<Location> LOCATIONS = List.of(
            EARTH_ORBIT,
            LAGRANGE_GATE,
            LUNA_BEACON,
            LUNA_PORT,
            MARS_APPROACH,
            PHOBOS_RELAY,
            MARS_COLONY,
            ASTEROID_GATE,
            CERES_RELAY,
            JOVIAN_CORRIDOR,
            CALLISTO_BUOY,
            GANYMEDE_DOCK,
            EUROPA_LANE,
            EUROPA_RESEARCH,
            SATURN_INBOUND,
            TITAN_ORBIT,
            TITAN_REFINERY,
            SATURN_RINGS,
            ORION_GATE,
            DEEP_SPACE_ANCHOR,
            EDEN_TRAIL,
            NEW_EDEN,
            HELIOS_BEACON,
            HELIOS_FARM,
            VEGA_LANE,
            VEGA_MARKET,
            KEPLER_GATE,
            KEPPLER_OUTPOST,
            DARK_NEBULA
    );

    static {
        EARTH_ORBIT.addPath(LAGRANGE_GATE, 4.0);
        LAGRANGE_GATE.addPath(LUNA_BEACON, 3.0);
        LUNA_BEACON.addPath(LUNA_PORT, 2.0);
        LUNA_BEACON.addPath(MARS_APPROACH, 7.0);
        MARS_APPROACH.addPath(PHOBOS_RELAY, 3.0);
        PHOBOS_RELAY.addPath(MARS_COLONY, 2.0);
        PHOBOS_RELAY.addPath(ASTEROID_GATE, 6.0);
        ASTEROID_GATE.addPath(CERES_RELAY, 4.0);
        CERES_RELAY.addPath(JOVIAN_CORRIDOR, 5.0);
        JOVIAN_CORRIDOR.addPath(CALLISTO_BUOY, 5.0);
        CALLISTO_BUOY.addPath(GANYMEDE_DOCK, 3.0);
        JOVIAN_CORRIDOR.addPath(EUROPA_LANE, 4.0);
        EUROPA_LANE.addPath(EUROPA_RESEARCH, 3.0);
        EUROPA_LANE.addPath(SATURN_INBOUND, 7.0);
        SATURN_INBOUND.addPath(TITAN_ORBIT, 3.0);
        TITAN_ORBIT.addPath(TITAN_REFINERY, 2.0);
        SATURN_INBOUND.addPath(SATURN_RINGS, 4.0);
        SATURN_RINGS.addPath(ORION_GATE, 8.0);
        TITAN_REFINERY.addPath(ORION_GATE, 9.0);
        ORION_GATE.addPath(DEEP_SPACE_ANCHOR, 6.0);
        DEEP_SPACE_ANCHOR.addPath(EDEN_TRAIL, 4.0);
        EDEN_TRAIL.addPath(NEW_EDEN, 3.0);
        DEEP_SPACE_ANCHOR.addPath(HELIOS_BEACON, 6.0);
        HELIOS_BEACON.addPath(HELIOS_FARM, 3.0);
        NEW_EDEN.addPath(VEGA_LANE, 6.0);
        HELIOS_FARM.addPath(VEGA_LANE, 7.0);
        VEGA_LANE.addPath(VEGA_MARKET, 4.0);
        VEGA_MARKET.addPath(KEPLER_GATE, 5.0);
        KEPLER_GATE.addPath(KEPPLER_OUTPOST, 5.0);
        KEPLER_GATE.addPath(DARK_NEBULA, 7.0);
        DARK_NEBULA.addPath(EDEN_TRAIL, 8.0);
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
