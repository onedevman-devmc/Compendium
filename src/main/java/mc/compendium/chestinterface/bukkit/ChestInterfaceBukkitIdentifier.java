package mc.compendium.chestinterface.bukkit;

import mc.compendium.chestinterface.components.ChestInterface;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public record ChestInterfaceBukkitIdentifier(ChestInterface<?, ?> chestInterface) implements InventoryHolder {

    @Override
    public Inventory getInventory() {
        return this.chestInterface().toBukkit();
    }

}
