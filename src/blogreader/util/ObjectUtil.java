package blogreader.util;

import javax.annotation.Nullable;

/**
 *
 * @author kristof
 */
public class ObjectUtil {

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
