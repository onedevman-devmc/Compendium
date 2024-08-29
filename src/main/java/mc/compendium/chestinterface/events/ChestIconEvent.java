package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestIcon;
import mc.compendium.chestinterface.components.ChestInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestIconEvent<
    BukkitEventType extends InventoryEvent
> extends ChestInterfaceEvent<BukkitEventType> {

    private final ChestIcon icon;
    private final ItemStack item;

    //

    public ChestIconEvent(BukkitEventType bukkitEvent, HumanEntity player, Inventory inventory, ChestInterface<?, ?> chestInterface, ChestIcon icon, ItemStack item) {
        super(bukkitEvent, player, inventory, chestInterface);

        this.icon = icon;
        this.item = item;
    }

    //

    public BukkitEventType bukkitEvent() { return super.bukkitEvent(); }

    //

    public ChestIcon icon() { return this.icon; }

    public ItemStack item() { return this.item; }
}
