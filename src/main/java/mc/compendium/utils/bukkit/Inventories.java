package mc.compendium.utils.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

public class Inventories {

    public static Inventory cloneInventory(Inventory inventory) {
        Inventory result = Bukkit.createInventory(null, inventory.getType());
        return copyInventoryInto(inventory, result);
    }

    public static Inventory copyInventoryInto(Inventory source, Inventory target) {
        ItemStack[] content = source.getContents();
        copyContentInto(content, target);

        return target;
    }

    public static Inventory copyContentInto(ItemStack[] content, Inventory target) {
        ItemStack[] contentsCopy = new ItemStack[content.length];
        System.arraycopy(content, 0, contentsCopy, 0, content.length);
        target.setContents(contentsCopy);

        return target;
    }

    public static int getContainableQuantityIn(Inventory inventory, ItemStack itemStack) {
        int quantity = itemStack.getAmount();

        Inventory checkInventory = copyInventoryInto(inventory, Bukkit.createInventory(null, inventory.getSize()));

        Map<Integer, ItemStack> remainingMap = checkInventory.addItem(itemStack.clone());
        if(!remainingMap.isEmpty()) quantity = quantity - remainingMap.get(0).getAmount();

        return quantity;
    }

    //

    public static String serializeContent(Inventory inventory) {
        return serializeContent(inventory.getContents());
    }

    public static String serializeContent(ItemStack[] content) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try(final BukkitObjectOutputStream bukkitOutStream = new BukkitObjectOutputStream(outStream)) {
            bukkitOutStream.writeInt(content.length);

            for(ItemStack itemStack : content) {
                bukkitOutStream.writeObject(itemStack == null ? new ItemStack(Material.AIR) : itemStack);
            }

            bukkitOutStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(outStream.toByteArray());
    }

    //

    public static void deserializeContent(String serializedContent, Inventory target) {
        target.setContents(deserializeContent(serializedContent));
    }

    public static ItemStack[] deserializeContent(String serializedContent) {
        ByteArrayInputStream inStream = new ByteArrayInputStream(Base64.getDecoder().decode(serializedContent));

        ItemStack[] content;

        try(final BukkitObjectInputStream bukkitInStream = new BukkitObjectInputStream(inStream)) {
            content = new ItemStack[bukkitInStream.readInt()];

            for(int i = 0; i < content.length; i++) {
                content[i] = (ItemStack) bukkitInStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return content;
    }

}
