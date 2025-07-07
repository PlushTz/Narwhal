package com.eetrust.narwhal.runtime.utils;

import android.os.Bundle;

/**
 * Desc:
 *
 * @author lijt
 * Created on 2025/7/7
 * Email: tao351992257@gmail.com
 */
public class BundleUtil {
    public static <T> T get(Bundle bundle, String key) {
        return (T) bundle.get(key);
    }

    public static <T> T get(Bundle bundle, String key, Object defaultValue) {
        Object object = bundle.get(key);
        if (object == null) {
            object = defaultValue;
        }
        return (T) object;
    }
}
