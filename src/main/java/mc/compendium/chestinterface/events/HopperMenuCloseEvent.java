package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.HopperMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class HopperMenuCloseEvent extends MenuCloseEvent<HopperMenu<?>> {

    public HopperMenuCloseEvent(InventoryCloseEvent bukkitEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public HopperMenuCloseEvent(InventoryCloseEvent bukkitEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }

}
