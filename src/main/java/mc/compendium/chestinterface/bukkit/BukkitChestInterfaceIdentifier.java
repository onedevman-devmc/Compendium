package mc.compendium.chestinterface.bukkit;

import mc.compendium.chestinterface.components.ChestInterface;
import mc.compendium.chestinterface.events.ChestInterfaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BukkitChestInterfaceIdentifier implements InventoryHolder {

    private final ChestInterface<? extends ChestInterfaceEvent> _chest_interface;

    //

    public BukkitChestInterfaceIdentifier(ChestInterface<? extends ChestInterfaceEvent> chest_interface) {
        this._chest_interface = chest_interface;
    }

    //

    public ChestInterface<? extends ChestInterfaceEvent> chestInterface() { return this._chest_interface; }

    //

    @Override
    public Inventory getInventory() {
        return this.chestInterface().toBukkit();
    }

}
