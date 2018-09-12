package com.baidu.ocr.demo.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author by kaka
 *         created at 16/6/12 16:09
 *         IO工具类
 */
public class IOUtils {
    private IOUtils() {
        throw new AssertionError();
    }


    /**
     * Close closable object and wrap {@link IOException} with {@link RuntimeException}
     *
     * @param closeable closeable object
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
        }
    }

    /**
     * Close closable and hide possible {@link IOException}
     *
     * @param closeable closeable object
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }
}
