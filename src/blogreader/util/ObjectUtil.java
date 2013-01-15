package blogreader.util;

import checkers.nullness.quals.Nullable;

/**
 *
 * @author kristof
 */
public class ObjectUtil {

    private ObjectUtil() {
    }

    /**
     * Returns the first non-null parameter.
     *
     * @param <T>
     * @param first
     * @param second
     * @return
     */
    public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
        if (first != null) {
            return first;
        } else if (second != null) {
            return second;
        } else {
            throw new NullPointerException();
        }
    }
}
