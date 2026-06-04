package dev.keystonemc.core;

import java.util.Collection;
import java.util.Map;

public final class SimpleJson {
    private SimpleJson() {
    }

    public static String stringify(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String string) {
            return "\"" + escape(string) + "\"";
        }
        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }
        if (value instanceof Map<?, ?> map) {
            StringBuilder builder = new StringBuilder("{");
            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    builder.append(',');
                }
                first = false;
                builder.append(stringify(String.valueOf(entry.getKey()))).append(':').append(stringify(entry.getValue()));
            }
            return builder.append('}').toString();
        }
        if (value instanceof Collection<?> collection) {
            StringBuilder builder = new StringBuilder("[");
            boolean first = true;
            for (Object item : collection) {
                if (!first) {
                    builder.append(',');
                }
                first = false;
                builder.append(stringify(item));
            }
            return builder.append(']').toString();
        }
        return stringify(value.toString());
    }

    private static String escape(String value) {
        return value.replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
            .replace("\t", "\\t");
    }
}
