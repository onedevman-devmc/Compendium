package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.HopperMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class HopperMenuOpenEvent extends MenuOpenEvent<HopperMenu<?>> {

    public HopperMenuOpenEvent(InventoryOpenEvent originalEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu) {
        this(originalEvent, entity, inventory, menu, true);
    }

    public HopperMenuOpenEvent(InventoryOpenEvent originalEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu, boolean cancellable) {
        super(originalEvent, entity, inventory, menu, cancellable);
    }

}
