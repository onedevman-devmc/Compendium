package mc.compendium.utils.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
        checkInventory.setMaxStackSize(inventory.getMaxStackSize());

        Map<Integer, ItemStack> remainingMap = checkInventory.addItem(itemStack.clone());
        if(!remainingMap.isEmpty()) quantity = quantity - remainingMap.get(0).getAmount();

        return quantity;
    }

    //

    public static String serializeContent(Inventory inventory) {
        return serializeContent(inventory.getContents());
    }

    public static String serializeContent(ItemStack[] content) {
        return BukkitObjects.serialize(content);
    }

    //

    public static void deserializeContent(String serializedContent, Inventory target) {
        target.setContents(deserializeContent(serializedContent));
    }

    public static ItemStack[] deserializeContent(String serializedContent) {
        return BukkitObjects.deserializeArray(serializedContent, ItemStack.class);
    }

}
