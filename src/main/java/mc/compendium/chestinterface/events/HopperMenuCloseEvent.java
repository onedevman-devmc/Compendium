package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.HopperMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class HopperMenuCloseEvent extends MenuCloseEvent<HopperMenu<?>> {

    public HopperMenuCloseEvent(InventoryCloseEvent originalEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu) {
        this(originalEvent, entity, inventory, menu, true);
    }

    public HopperMenuCloseEvent(InventoryCloseEvent originalEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu, boolean cancellable) {
        super(originalEvent, entity, inventory, menu, cancellable);
    }

}
