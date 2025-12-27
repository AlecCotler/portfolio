package fleet;
/**
 * An enum class that enumerates through different vehicle makes.
 * Provides methods for parsing and comparing make strings.
 *
 * @author Carly Chick
 */
public enum Make {
    FORD,
    CHEVY,
    TOYOTA,
    HONDA;

    /**
     * Constructor for Make
     */
    private Make() {
    }

    /**
     * Parses a string into its corresponding Make value (case-insensitive).
     *
     * @param makeStr the make string to parse
     * @return the matching Make constant if found; null if no match exists
     */
    public static Make parseMake(String makeStr) {
        Make[] allMakes = Make.values();
        for (int i = 0; i < allMakes.length; i++) {
            if (allMakes[i].name().equalsIgnoreCase(makeStr)) {
                return allMakes[i];
            }
        }
        return null;
    }

    /**
     * Compares two strings lexicographically, ignoring case differences.
     *
     * @param first the first string to compare
     * @param second the second string to compare
     * @return a negative integer if a comes before b;
     * a positive integer if a comes after b;
     * or zero if the strings are equal ignoring case
     */
    public static int compareStringsIgnoreCase(String first, String second) {
        int len1 = first.length();
        int len2 = second.length();
        int lim = Math.min(len1, len2);
        for (int i = 0; i < lim; i++) {
            char c1 = Character.toLowerCase(first.charAt(i));
            char c2 = Character.toLowerCase(second.charAt(i));
            if (c1 != c2) {
                return c1 - c2;
            }
        }
        return len1 - len2;
    }
}
