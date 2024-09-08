package mc.compendium.utils.bukkit;

import org.bukkit.inventory.ItemStack;

public class ItemStacks {

    public static String serialize(ItemStack itemStack) {
        return BukkitObjects.serialize(itemStack);
    }

    //

    public static ItemStack deserialize(String serialized) {
        return BukkitObjects.deserialize(serialized, ItemStack.class);
    }

}
