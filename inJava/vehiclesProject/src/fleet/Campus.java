package fleet;
/**
 * This enum class represents the campuses along with their city locations.
 *
 * @author Carly Chick
 */
public enum Campus {
    Busch("New Brunswick"),
    Livingston("New Brunswick"),
    Cook("New Brunswick"),
    Newark("Newark"),
    Camden("Camden");

    private final String city;

    /**
     * Constructor for a Campus in a city
     *
     * @param city the city
     */
    Campus(String city) {
        this.city = city;
    }

    /**
     * Get the city of the campus
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Turns a string into the matching campus constant.
     * The match is case-insensitive.
     *
     * @param campusStr the campus string
     * @return the campus; null otherwise
     */
    public static Campus parseCampus(String campusStr) {
        Campus[] allCampus = Campus.values();
        for (int i = 0; i < allCampus.length; i++) {
            if (allCampus[i].name().equalsIgnoreCase(campusStr)) {
                return allCampus[i];
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
