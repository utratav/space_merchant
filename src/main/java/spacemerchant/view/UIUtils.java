package spacemerchant.view;

public class UIUtils {

    public static String drawProgressBar(int current, int max, int length) {
        if (max <= 0) {
            return "[ " + "░".repeat(length) + "]";
        }

        // Zabezpieczenie przed wyjściem poza skalę
        int safeCurrent = Math.max(0, Math.min(current, max));

        // Obliczenia proporcji
        double percentage = (double) safeCurrent / max;
        int filledLength = (int) Math.round(percentage * length);
        int emptyLength = length - filledLength;

        return "[ " +
                "█".repeat(Math.max(0, filledLength)) +
                "░".repeat(Math.max(0, emptyLength)) +
                "]";
    }

    public static String drawStars(int level) {
        int maxStars = 5;

        // Zabezpieczenie, aby poziom statystyk zawsze mieścił się w przedziale 0-5
        int safeLevel = Math.max(0, Math.min(level, maxStars));

        return "[ " +
                "★".repeat(safeLevel) +
                "☆".repeat(maxStars - safeLevel) +
                "]";
    }
}