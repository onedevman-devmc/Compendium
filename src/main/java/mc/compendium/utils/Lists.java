package mc.compendium.utils;

import java.util.*;

public class Lists {

    public static <V> boolean isModifiable(Collection<V> collection) {
        return !collection.getClass().getName().contains("Unmodifiable");
    }

    public static <K, V> boolean isModifiable(Map<K, V> map) {
        return !map.getClass().getName().contains("Unmodifiable");
    }

}
