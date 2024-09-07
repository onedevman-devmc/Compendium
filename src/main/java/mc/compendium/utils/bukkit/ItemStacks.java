package mc.compendium.utils.bukkit;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemStacks {

    public static String serialize(ItemStack itemStack) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try(final BukkitObjectOutputStream bukkitOutStream = new BukkitObjectOutputStream(outStream)) {
            bukkitOutStream.writeObject(itemStack);
            bukkitOutStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(outStream.toByteArray());
    }

    //

    public static ItemStack deserialize(String serialized) {
        ByteArrayInputStream inStream = new ByteArrayInputStream(Base64.getDecoder().decode(serialized));

        ItemStack result;

        try(final BukkitObjectInputStream bukkitInStream = new BukkitObjectInputStream(inStream)) {
            result = (ItemStack) bukkitInStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

}
