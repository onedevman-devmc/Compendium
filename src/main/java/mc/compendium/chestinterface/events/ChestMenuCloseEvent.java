package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class ChestMenuCloseEvent extends MenuCloseEvent<ChestMenu<?>> {

    public ChestMenuCloseEvent(InventoryCloseEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu<?> menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public ChestMenuCloseEvent(InventoryCloseEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu<?> menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }

}
