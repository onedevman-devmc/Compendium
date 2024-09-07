package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class ChestMenuOpenEvent extends MenuOpenEvent<ChestMenu<?>> {

    public ChestMenuOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu<?> menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public ChestMenuOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu<?> menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }

}
