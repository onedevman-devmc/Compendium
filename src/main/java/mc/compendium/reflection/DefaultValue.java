package mc.compendium.reflection;

import java.lang.reflect.Array;

public class DefaultValue {

    public static <T> T get(Class<T> clazz) {
        return (T) Array.get(Array.newInstance(clazz, 1), 0);
    }

}
