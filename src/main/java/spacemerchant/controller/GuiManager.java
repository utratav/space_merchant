package spacemerchant.controller;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.io.IOException;

public class GuiManager {
    private Terminal terminal;
    private Screen screen;
    private MultiWindowTextGUI gui;

    public GuiManager() {
        try {
            // Konfiguracja rozmiarów terminala
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setInitialTerminalSize(new TerminalSize(170, 50));
            factory.setTerminalEmulatorFontConfiguration(SwingTerminalFontConfiguration.getDefaultOfSize(20));

            // Utworzenie terminala i buforowanego ekranu
            this.terminal = factory.createTerminal();
            this.screen = new TerminalScreen(this.terminal);
            this.screen.startScreen(); // Uruchamia tryb pełnoekranowy (TUI)

            // Utworzenie wielookienkowego menedżera GUI
            this.gui = new MultiWindowTextGUI(this.screen);
            applyMonochromeTheme();

        } catch (IOException e) {
            throw new RuntimeException("Krytyczny błąd: Nie udało się zainicjować interfejsu Lanterna!", e);
        }
    }

    private void applyMonochromeTheme() {
        TextColor white = TextColor.ANSI.WHITE;
        TextColor black = TextColor.ANSI.BLACK;

        SimpleTheme theme = SimpleTheme.makeTheme(
                false,
                white,
                black,
                white,
                black,
                black,
                white,
                black
        );

        theme.getDefaultDefinition()
                .setPreLight(white, black)
                .setActive(black, white)
                .setSelected(black, white)
                .setInsensitive(white, black)
                .setCursorVisible(false);

        theme.addOverride(Button.class, white, black)
                .setPreLight(white, black)
                .setActive(black, white)
                .setSelected(black, white)
                .setInsensitive(white, black)
                .setCursorVisible(false)
                .setRenderer(Button.class, type -> new Button.FlatButtonRenderer());

        this.gui.setTheme(theme);
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