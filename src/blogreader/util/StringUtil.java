package blogreader.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author kristof
 */
public abstract class StringUtil {

    private StringUtil() {
    }
    
    /**
     * converts string to InputStream
     * @param str
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static InputStream inputStreamFromString(String str, String encoding) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes(encoding);
        return new ByteArrayInputStream(bytes);
    }
}
