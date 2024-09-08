package mc.compendium.utils.bukkit;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Base64;

public class BukkitObjects {

    public static <T> String serialize(T object) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try(final BukkitObjectOutputStream bukkitOutStream = new BukkitObjectOutputStream(outStream)) {
            bukkitOutStream.writeObject(object);
            bukkitOutStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(outStream.toByteArray());
    }

    //

    public static <T> String serialize(T[] array) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try(final BukkitObjectOutputStream bukkitOutStream = new BukkitObjectOutputStream(outStream)) {
            bukkitOutStream.writeInt(array.length);
            for(T object : array) bukkitOutStream.writeObject(object);
            bukkitOutStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(outStream.toByteArray());
    }

    //

    public static <T> T deserialize(String serialized, Class<T> clazz) {
        ByteArrayInputStream inStream = new ByteArrayInputStream(Base64.getDecoder().decode(serialized));

        T result;

        try (final BukkitObjectInputStream bukkitInStream = new BukkitObjectInputStream(inStream)) {
            result = clazz.cast(bukkitInStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    //

    public static <T> T[] deserializeArray(String serialized, Class<T> elementClass) {
        ByteArrayInputStream inStream = new ByteArrayInputStream(Base64.getDecoder().decode(serialized));

        T[] result;

        try(final BukkitObjectInputStream bukkitInStream = new BukkitObjectInputStream(inStream)) {
            int length = bukkitInStream.readInt();
            result = (T[]) Array.newInstance(elementClass, length);
            for(int i = 0; i < length; i++) result[i] = elementClass.cast(bukkitInStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
