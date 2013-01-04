package blogreader.util;

import javax.annotation.Nullable;

/**
 *
 * @author kristof
 */
public class ObjectUtil {

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
