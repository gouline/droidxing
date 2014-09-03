/*
 * Copyright (C) 2014 Mike Gouline
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gouline.droidxing.data;

import net.gouline.droidxing.camera.FrontLightMode;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple preference retrieval mechanism for supplying either developer-static values or plugging
 * an external source of values.
 *
 * @author Mike Gouline
 */
public class DroidXingPreferences {

    public static final String KEY_DECODE_1D_PRODUCT = "preferences_decode_1D_product";
    public static final String KEY_DECODE_1D_INDUSTRIAL = "preferences_decode_1D_industrial";
    public static final String KEY_DECODE_QR = "preferences_decode_QR";
    public static final String KEY_DECODE_DATA_MATRIX = "preferences_decode_Data_Matrix";
    public static final String KEY_DECODE_AZTEC = "preferences_decode_Aztec";
    public static final String KEY_DECODE_PDF417 = "preferences_decode_PDF417";

    public static final String KEY_FRONT_LIGHT_MODE = "preferences_front_light_mode";
    public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";
    public static final String KEY_INVERT_SCAN = "preferences_invert_scan";

    public static final String KEY_DISABLE_CONTINUOUS_FOCUS = "preferences_disable_continuous_focus";
    public static final String KEY_DISABLE_EXPOSURE = "preferences_disable_exposure";
    public static final String KEY_DISABLE_METERING = "preferences_disable_metering";
    public static final String KEY_DISABLE_BARCODE_SCENE_MODE =
            "preferences_disable_barcode_scene_mode";

    private static final Provider defaultProvider = new Provider() {
        private Map<String, Object> values = new HashMap<String, Object>();

        {
            values.put(KEY_DECODE_1D_PRODUCT, true);
            values.put(KEY_DECODE_1D_INDUSTRIAL, true);
            values.put(KEY_DECODE_QR, true);
            values.put(KEY_DECODE_DATA_MATRIX, true);
            values.put(KEY_DECODE_AZTEC, false);
            values.put(KEY_DECODE_PDF417, false);

            values.put(KEY_FRONT_LIGHT_MODE, FrontLightMode.OFF.toString());
            values.put(KEY_AUTO_FOCUS, true);
            values.put(KEY_INVERT_SCAN, false);

            values.put(KEY_DISABLE_CONTINUOUS_FOCUS, true);
            values.put(KEY_DISABLE_EXPOSURE, true);
            values.put(KEY_DISABLE_METERING, true);
            values.put(KEY_DISABLE_BARCODE_SCENE_MODE, true);
        }

        @Override
        public Object getValue(String key) {
            return values.get(key);
        }
    };
    private static Provider provider;

    /**
     * Sets a custom provider instance to use. Values not found in that will fall back on the
     * default provider.
     *
     * @param provider Provider instance.
     */
    public static synchronized void setProvider(Provider provider) {
        DroidXingPreferences.provider = provider;
    }

    public static String getString(String key) {
        return getValue(key, String.class);
    }

    public static Integer getInt(String key) {
        return getValue(key, Integer.class);
    }

    public static Boolean getBoolean(String key) {
        return getValue(key, Boolean.class);
    }

    private static <T> T getValue(String key, Class<T> clazz) {
        final T value = (provider != null) ? getTypedValue(provider, key, clazz) : null;
        final T defaultValue = getTypedValue(defaultProvider, key, clazz);
        if (value != null) {
            return value;
        } else if (defaultValue != null) {
            return defaultValue;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T getTypedValue(Provider provider, String key, Class<T> clazz) {
        final Object value = provider.getValue(key);
        if (value != null && clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Custom preference value provider.
     */
    public static interface Provider {
        /**
         * Retrieves preference by key.
         *
         * @param key Preference key.
         * @return Preference value (String, Integer or Boolean).
         */
        public Object getValue(String key);
    }

}
