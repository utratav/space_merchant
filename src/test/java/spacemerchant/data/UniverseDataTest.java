package spacemerchant.data;

import org.junit.jupiter.api.Test;
import spacemerchant.model.Location;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UniverseDataTest {

    @Test
    void shouldKeepEveryLocationReachableFromStartingOrbit() {
        Set<Location> visited = collectReachableLocations(UniverseData.getStartingLocation());

        assertEquals(UniverseData.getLocations().size(), visited.size());
    }

    @Test
    void shouldRequireMultipleRouteNodesBeforeMarsColony() {
        Location marsColony = findLocation("Kolonia Mars");
        Map<Location, Integer> hopCounts = calculateHopCounts(UniverseData.getStartingLocation());

        assertTrue(hopCounts.get(marsColony) >= 4);
    }

    private Set<Location> collectReachableLocations(Location start) {
        return calculateHopCounts(start).keySet();
    }

    private Map<Location, Integer> calculateHopCounts(Location start) {
        Map<Location, Integer> hopCounts = new HashMap<>();
        Queue<Location> queue = new ArrayDeque<>();

        hopCounts.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            Location current = queue.poll();
            int nextHopCount = hopCounts.get(current) + 1;

            for (Location next : current.getConnectedPaths().keySet()) {
                if (hopCounts.containsKey(next)) {
                    continue;
                }

                hopCounts.put(next, nextHopCount);
                queue.add(next);
            }
        }

        return hopCounts;
    }

    private Location findLocation(String name) {
        for (Location location : UniverseData.getLocations()) {
            if (location.getName().equals(name)) {
                return location;
            }
        }

        throw new IllegalArgumentException("Nie znaleziono lokacji: " + name);
    }
}
