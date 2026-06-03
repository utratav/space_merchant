package spacemerchant.view;

import com.googlecode.lanterna.gui2.*;
import spacemerchant.controller.GuiManager;
import spacemerchant.data.UniverseData;
import spacemerchant.model.Location;
import spacemerchant.model.Ship;
import spacemerchant.service.CrewService;
import spacemerchant.service.NavigationService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StarMapWindow extends BasicWindow {
    private GuiManager guiManager;
    private Ship ship;
    private NavigationService navigationService;
    private String errorMessage = "";

    public StarMapWindow(GuiManager guiManager, Ship ship) {
        super("Mapa Gwiezdna - Nawigacja");
        this.guiManager = guiManager;
        this.ship = ship;
        // NavigationService potrzebuje CrewService do opłacania załogi podczas podróży
        this.navigationService = new NavigationService(new CrewService());

        refreshUI();
    }

    private void refreshUI() {
        Location currentLoc = ship.getCurrentLocation();
        Map<Location, Character> routeMarkers = buildRouteMarkers(currentLoc);
        Panel rootPanel = new Panel(new GridLayout(1));

        // Górny panel informacyjny
        Panel headerPanel = new Panel(new GridLayout(1));
        headerPanel.addComponent(new Label(String.format("Dostępne paliwo: %.2f j.", ship.getCurrentFuel())));
        headerPanel.addComponent(new Label("Obecny sektor: " + (currentLoc != null ? currentLoc.getName() : "Lot w toku...")));
        headerPanel.addComponent(new Label("Wybierz skok po literze z mapy: [A], [B], [C]..."));
        rootPanel.addComponent(headerPanel.withBorder(Borders.singleLine("STATUS NAWIGACJI")));
        rootPanel.addComponent(new EmptySpace());

        Panel contentPanel = new Panel(new GridLayout(2));
        contentPanel.addComponent(createMapPanel(currentLoc, routeMarkers).withBorder(Borders.singleLine("MAPA SZLAKÓW 2.0")));

        // Panel dostępnych szlaków
        Panel destinationsPanel = new Panel(new GridLayout(1));

        if (currentLoc != null) {
            Map<Location, Double> paths = currentLoc.getConnectedPaths();

            if (paths.isEmpty()) {
                destinationsPanel.addComponent(new Label("Brak zbadanych szlaków z tego sektora. Jesteś uwięziony!"));
            } else {
                for (Map.Entry<Location, Double> entry : paths.entrySet()) {
                    Location destination = entry.getKey();
                    Double fuelCost = entry.getValue();
                    char routeMarker = routeMarkers.getOrDefault(destination, '?');
                    String destinationType = destination.hasStation() ? "STACJA" : "PUNKT SZLAKU";

                    Panel pathPanel = new Panel(new GridLayout(2));
                    pathPanel.addComponent(new Label("[" + routeMarker + "] " + destination.getName()
                            + " [" + destinationType + "] - " + destination.getEconomy()));

                    Button jumpButton = new Button(String.format("Skok [%s] (%.1f paliwa)", routeMarker, fuelCost), () -> {
                        try {
                            navigationService.travel(ship, destination, guiManager);
                            this.close();
                        } catch (RuntimeException e) {
                            if (ship.isDefeated()) {
                                this.close();
                                return;
                            }
                            errorMessage = e.getMessage();
                            refreshUI();
                        }
                    });

                    // Zabezpieczenie wizualne - blokada jeśli brakuje paliwa
                    if (ship.getCurrentFuel() < fuelCost) {
                        jumpButton.setEnabled(false);
                        jumpButton.setLabel("Brak paliwa");
                    }

                    pathPanel.addComponent(jumpButton);
                    destinationsPanel.addComponent(pathPanel);
                }
            }
        }

        contentPanel.addComponent(destinationsPanel.withBorder(Borders.singleLine("ZNANE SZLAKI NADPRZESTRZENNE")));
        rootPanel.addComponent(contentPanel);
        rootPanel.addComponent(new EmptySpace());

        if (!errorMessage.isEmpty()) {
            rootPanel.addComponent(new Label("BŁĄD KOMPUTERA POKŁADOWEGO: " + errorMessage));
            rootPanel.addComponent(new EmptySpace());
        }

        rootPanel.addComponent(new Button("Wróć do kokpitu", this::close));
        this.setComponent(rootPanel);
    }

    private Panel createMapPanel(Location currentLoc, Map<Location, Character> routeMarkers) {
        Panel mapPanel = new Panel(new GridLayout(1));
        char[][] map = renderUniverseMap(currentLoc, routeMarkers);

        for (char[] row : map) {
            mapPanel.addComponent(new Label(new String(row)));
        }

        mapPanel.addComponent(new EmptySpace());
        mapPanel.addComponent(new Label("@ statek | A/B/C cele z listy"));
        mapPanel.addComponent(new Label("O stacja | o punkt szlaku | + skrzyzowanie"));
        mapPanel.addComponent(new EmptySpace());
        mapPanel.addComponent(new Label("--- CELE W ZASIEGU ---"));
        if (routeMarkers.isEmpty()) {
            mapPanel.addComponent(new Label("Brak aktywnych tras z tego sektora."));
        } else {
            for (Map.Entry<Location, Character> entry : routeMarkers.entrySet()) {
                Location location = entry.getKey();
                mapPanel.addComponent(new Label("[" + entry.getValue() + "] " + location.getName()));
            }
        }
        return mapPanel;
    }

    private char[][] renderUniverseMap(Location currentLoc, Map<Location, Character> routeMarkers) {
        List<Location> locations = UniverseData.getLocations();
        int minX = locations.stream().mapToInt(Location::getX).min().orElse(0);
        int maxX = locations.stream().mapToInt(Location::getX).max().orElse(0);
        int minY = locations.stream().mapToInt(Location::getY).min().orElse(0);
        int maxY = locations.stream().mapToInt(Location::getY).max().orElse(0);
        int width = (maxX - minX) * 2 + 1;
        int height = (maxY - minY) * 2 + 1;

        char[][] map = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = ' ';
            }
        }

        Map<Location, StarMapPoint> points = new HashMap<>();
        Map<Location, Integer> locationIndexes = new HashMap<>();
        for (int i = 0; i < locations.size(); i++) {
            Location location = locations.get(i);
            points.put(location, new StarMapPoint((location.getX() - minX) * 2, (location.getY() - minY) * 2));
            locationIndexes.put(location, i);
        }

        for (Location location : locations) {
            for (Location destination : location.getConnectedPaths().keySet()) {
                if (locationIndexes.get(location) < locationIndexes.get(destination)) {
                    drawConnection(map, points.get(location), points.get(destination));
                }
            }
        }

        Set<Location> reachableNow = currentLoc != null
                ? currentLoc.getConnectedPaths().keySet()
                : new HashSet<>();

        for (Location location : locations) {
            StarMapPoint point = points.get(location);
            if (location.equals(currentLoc)) {
                map[point.y()][point.x()] = '@';
            } else if (routeMarkers.containsKey(location)) {
                map[point.y()][point.x()] = routeMarkers.get(location);
            } else if (reachableNow.contains(location)) {
                map[point.y()][point.x()] = '*';
            } else {
                map[point.y()][point.x()] = location.hasStation() ? 'O' : 'o';
            }
        }

        return map;
    }

    private Map<Location, Character> buildRouteMarkers(Location currentLoc) {
        Map<Location, Character> routeMarkers = new LinkedHashMap<>();
        if (currentLoc == null) {
            return routeMarkers;
        }

        int index = 0;
        for (Location destination : currentLoc.getConnectedPaths().keySet()) {
            routeMarkers.put(destination, routeMarker(index));
            index++;
        }

        return routeMarkers;
    }

    private char routeMarker(int index) {
        if (index < 26) {
            return (char) ('A' + index);
        }

        return (char) ('0' + (index - 26) % 10);
    }

    private void drawConnection(char[][] map, StarMapPoint start, StarMapPoint end) {
        int x = start.x();
        int y = start.y();
        int dx = Math.abs(end.x() - start.x());
        int dy = Math.abs(end.y() - start.y());
        int stepX = Integer.compare(end.x(), start.x());
        int stepY = Integer.compare(end.y(), start.y());
        int error = dx - dy;

        while (x != end.x() || y != end.y()) {
            int previousX = x;
            int previousY = y;
            int doubledError = error * 2;

            if (doubledError > -dy) {
                error -= dy;
                x += stepX;
            }

            if (doubledError < dx) {
                error += dx;
                y += stepY;
            }

            if (x == end.x() && y == end.y()) {
                break;
            }

            placeConnectionChar(map, x, y, connectionChar(x - previousX, y - previousY));
        }
    }

    private void placeConnectionChar(char[][] map, int x, int y, char pathChar) {
        if (map[y][x] == ' ') {
            map[y][x] = pathChar;
        } else if (map[y][x] != pathChar) {
            map[y][x] = '+';
        }
    }

    private char connectionChar(int deltaX, int deltaY) {
        if (deltaX == 0) {
            return '|';
        }

        if (deltaY == 0) {
            return '-';
        }

        return Integer.signum(deltaX) == Integer.signum(deltaY) ? '\\' : '/';
    }

    private record StarMapPoint(int x, int y) {
    }
}