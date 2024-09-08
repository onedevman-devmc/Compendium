package mc.compendium.utils.bukkit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemNames {

    public static String defaultName(ItemStack itemStack) {
        return defaultName(itemStack.getType());
    }

    public static String defaultName(Material material) {
        String rawName = material.name();
        String[] nameParts = rawName.toLowerCase().split("_");

        for(int i = 0; i < nameParts.length; i++) {
            String namePart = nameParts[i];
            nameParts[i] = namePart.substring(0, 1).toUpperCase() + namePart.substring(1);
        }

        return String.join(" ", nameParts);
    }

}
