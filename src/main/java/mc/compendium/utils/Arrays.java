package mc.compendium.utils;

public class Arrays {

    public static <T> String join(T[] array, String separator) {
        return join(array, separator, 0);
    }

    public static <T> String join(T[] array, String separator, int includedStart) {
        return join(array, separator, includedStart, array.length);
    }

    public static <T> String join(T[] array, String separator, int includedStart, int excludedEnd) {
        StringBuilder result = new StringBuilder();

        for(int i = includedStart; i < excludedEnd-1; ++i)
            result.append(String.valueOf(array[i])).append(separator);

        if(excludedEnd > 0)
            result.append(String.valueOf(array[excludedEnd - 1]));

        return result.toString();
    }

}
