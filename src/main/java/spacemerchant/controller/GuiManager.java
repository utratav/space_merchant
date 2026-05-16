package spacemerchant.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class GuiManager {
    private Terminal terminal;
    private Screen screen;
    private MultiWindowTextGUI gui;

    public GuiManager() {
        try {
            // Konfiguracja rozmiarów terminala
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setInitialTerminalSize(new TerminalSize(100, 30));

            // Utworzenie terminala i buforowanego ekranu
            this.terminal = factory.createTerminal();
            this.screen = new TerminalScreen(this.terminal);
            this.screen.startScreen(); // Uruchamia tryb pełnoekranowy (TUI)

            // Utworzenie wielookienkowego menedżera GUI
            this.gui = new MultiWindowTextGUI(this.screen);

        } catch (IOException e) {
            throw new RuntimeException("Krytyczny błąd: Nie udało się zainicjować interfejsu Lanterna!", e);
        }
    }

    public void showWindow(Window window) {
        this.gui.addWindowAndWait(window);
    }

    public void stop() {
        try {
            if (this.screen != null) {
                this.screen.stopScreen();
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas zamykania ekranu: " + e.getMessage());
        }
    }
}