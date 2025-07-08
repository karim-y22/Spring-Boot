package at.boot.util;

public class InputSanitizer {

    private InputSanitizer() {
        // private Konstruktor, damit die Klasse nicht instanziiert wird
        throw new UnsupportedOperationException("Utility class");
    }

    public static String trim(String input) {
        return input != null ? input.trim() : null;
    }

    public static String trimAndLowercaseEmail(String email) {
        return email != null ? email.trim().toLowerCase() : null;
    }

    public static String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            // Wenn null reinkommt, einfach null zurückgeben
            return null;
        }

        // alle Leerzeichen, Bindestriche und Klammern entfernen
        String normalized = phoneNumber.replaceAll("[\\s\\-()]", "");

        // ob die Nummer mit '+' beginnt
        if (normalized.startsWith("+")) {
            // Plus am Anfang behalten --> Danach alle Nicht-Ziffern entfernen
            // substring(1) nimmt die Nummer ohne das Plus-Zeichen vorne
            normalized = "+" + normalized.substring(1).replaceAll("[^\\d]", "");
        } else {
            // kein Plus am Anfang, entferne alle Nicht-Ziffern komplett
            normalized = normalized.replaceAll("[^\\d]", "");
        }

        return normalized;
    }
}
